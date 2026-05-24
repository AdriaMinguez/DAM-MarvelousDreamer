package com.example.marvelousdreamer.data.repository

import android.util.Log
import com.example.marvelousdreamer.data.remote.api.HotelApiService
import com.example.marvelousdreamer.data.remote.mapper.toDomain
import com.example.marvelousdreamer.data.remote.mapper.toDto
import com.example.marvelousdreamer.domain.model.Hotel
import com.example.marvelousdreamer.domain.model.Reservation
import com.example.marvelousdreamer.domain.model.ReserveRequest
import com.example.marvelousdreamer.domain.repository.HotelRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    private val api: HotelApiService
) : HotelRepository {

    companion object { private const val TAG = "HotelRepositoryImpl" }

    override suspend fun getAvailability(groupId: String, start: String, end: String, city: String?): List<Hotel> {
        Log.d(TAG, "getAvailability: group=$groupId, $start-$end, city=$city")
        return api.getAvailability(groupId, start, end, city).available_hotels.map { it.toDomain() }
    }

    override suspend fun reserve(groupId: String, request: ReserveRequest): Reservation {
        Log.i(TAG, "reserve: ${request.hotelId}/${request.roomId}")
        return api.reserveRoom(groupId, request.toDto()).reservation.toDomain()
    }

    override suspend fun getGroupReservations(groupId: String, guestEmail: String?): List<Reservation> {
        Log.d(TAG, "getGroupReservations: group=$groupId, email=$guestEmail")
        return api.getGroupReservations(groupId, guestEmail).reservations.map { it.toDomain() }
    }

    override suspend fun cancelById(resId: String): Reservation {
        Log.i(TAG, "cancelById: $resId")
        return api.deleteReservationById(resId).toDomain()
    }
}
