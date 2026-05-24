package com.example.marvelousdreamer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelousdreamer.data.local.dao.ReservationDao
import com.example.marvelousdreamer.data.local.dao.TripDao
import com.example.marvelousdreamer.data.local.entity.ReservationEntity
import com.example.marvelousdreamer.data.local.entity.TripEntity
import com.example.marvelousdreamer.domain.model.Hotel
import com.example.marvelousdreamer.domain.model.Reservation
import com.example.marvelousdreamer.domain.model.ReserveRequest
import com.example.marvelousdreamer.domain.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HotelSearchState(
    val hotels: List<Hotel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HotelViewModel @Inject constructor(
    private val hotelRepository: HotelRepository,
    private val reservationDao: ReservationDao,
    private val tripDao: TripDao
) : ViewModel() {

    companion object {
        private const val TAG = "HotelViewModel"
        const val GROUP_ID = "G11"
    }

    private val _searchState = MutableStateFlow(HotelSearchState())
    val searchState: StateFlow<HotelSearchState> = _searchState.asStateFlow()

    private val _searchStartDate = MutableStateFlow("")
    private val _searchEndDate = MutableStateFlow("")
    val searchStartDate: StateFlow<String> = _searchStartDate.asStateFlow()
    val searchEndDate: StateFlow<String> = _searchEndDate.asStateFlow()

    private val _selectedHotel = MutableStateFlow<Hotel?>(null)
    val selectedHotel: StateFlow<Hotel?> = _selectedHotel.asStateFlow()

    private val _bookingResult = MutableStateFlow<String?>(null)
    val bookingResult: StateFlow<String?> = _bookingResult.asStateFlow()

    private val _userId = MutableStateFlow("")

    val reservations: StateFlow<List<ReservationEntity>> = _userId
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else reservationDao.getReservationsByUser(uid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setUserId(uid: String) { _userId.value = uid }

    fun searchHotels(city: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            _searchStartDate.value = startDate
            _searchEndDate.value = endDate
            _searchState.value = HotelSearchState(isLoading = true)
            try {
                val hotels = hotelRepository.getAvailability(GROUP_ID, startDate, endDate, city)
                _searchState.value = HotelSearchState(hotels = hotels)
                Log.i(TAG, "searchHotels: found ${hotels.size} hotels in $city")
            } catch (e: Exception) {
                _searchState.value = HotelSearchState(error = e.message ?: "Search failed")
                Log.e(TAG, "searchHotels: ${e.message}")
            }
        }
    }

    fun selectHotel(hotel: Hotel) { _selectedHotel.value = hotel }

    fun bookRoom(
        hotel: Hotel,
        roomId: String,
        startDate: String,
        endDate: String,
        guestName: String,
        guestEmail: String,
        userId: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val request = ReserveRequest(
                    hotelId = hotel.id, roomId = roomId,
                    startDate = startDate, endDate = endDate,
                    guestName = guestName, guestEmail = guestEmail
                )
                val reservation = hotelRepository.reserve(GROUP_ID, request)
                val room = hotel.rooms.find { it.id == roomId }

                // Create a trip for this reservation
                val tripId = "hotel_${reservation.id}"
                val startParts = startDate.split("-")
                val endParts = endDate.split("-")
                val start = LocalDate.of(startParts[0].toInt(), startParts[1].toInt(), startParts[2].toInt())
                val end = LocalDate.of(endParts[0].toInt(), endParts[1].toInt(), endParts[2].toInt())

                tripDao.insert(TripEntity(
                    id = tripId,
                    title = "🏨 ${hotel.name}",
                    description = "Hotel reservation in ${hotel.address}",
                    destination = hotel.address,
                    startDate = start,
                    endDate = end,
                    budget = (room?.price ?: 0f).toDouble() * (end.toEpochDay() - start.toEpochDay()),
                    userId = userId
                ))

                // Save reservation locally
                reservationDao.insert(ReservationEntity(
                    id = reservation.id,
                    tripId = tripId,
                    hotelId = hotel.id,
                    hotelName = hotel.name,
                    hotelAddress = hotel.address,
                    hotelImageUrl = hotel.imageUrl,
                    roomId = roomId,
                    roomType = room?.roomType ?: "",
                    price = room?.price ?: 0f,
                    roomImages = room?.images?.joinToString(",") ?: "",
                    startDate = startDate,
                    endDate = endDate,
                    guestName = guestName,
                    guestEmail = guestEmail,
                    userId = userId
                ))

                _bookingResult.value = "Booked! Reservation: ${reservation.id}"
                Log.i(TAG, "bookRoom: success, id=${reservation.id}")
                onSuccess()
            } catch (e: Exception) {
                _bookingResult.value = "Error: ${e.message}"
                Log.e(TAG, "bookRoom: ${e.message}")
            }
        }
    }

    fun cancelReservation(reservationId: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                hotelRepository.cancelById(reservationId)
                Log.i(TAG, "cancelReservation: API cancel OK")
            } catch (e: Exception) {
                Log.e(TAG, "cancelReservation API failed: ${e.message}")
            }
            // Always delete locally
            val local = reservationDao.getReservationsByUser(_userId.value).first()
                .find { it.id == reservationId }
            if (local != null) {
                tripDao.deleteById(local.tripId)
                reservationDao.deleteById(reservationId)
            }
            Log.i(TAG, "cancelReservation: local delete done")
            onDone()
        }
    }

    fun clearBookingResult() { _bookingResult.value = null }
}
