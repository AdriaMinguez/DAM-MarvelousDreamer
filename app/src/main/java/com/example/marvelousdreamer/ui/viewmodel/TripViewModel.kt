package com.example.marvelousdreamer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.domain.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

data class TripFormState(
    val title: String = "",
    val description: String = "",
    val destination: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val budget: String = "",
    val titleError: String? = null,
    val startDateError: String? = null,
    val endDateError: String? = null,
    val isEditing: Boolean = false,
    val editingId: String? = null
)

data class ActivityFormState(
    val title: String = "",
    val description: String = "",
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val location: String = "",
    val cost: String = "",
    val type: ActivityType = ActivityType.OTHER,
    val titleError: String? = null,
    val dateError: String? = null,
    val timeError: String? = null,
    val isEditing: Boolean = false,
    val editingId: String? = null
)

@HiltViewModel
class TripViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    companion object { private const val TAG = "TripViewModel" }

    private val _currentUserId = MutableStateFlow("")
    private val _refreshTrigger = MutableStateFlow(0L)

    val trips: StateFlow<List<Trip>> = combine(_currentUserId, _refreshTrigger) { uid, _ -> uid }
        .flatMapLatest { uid ->
            if (uid.isEmpty()) flowOf(emptyList())
            else repository.getTrips(uid)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedTrip = MutableStateFlow<Trip?>(null)
    val selectedTrip: StateFlow<Trip?> = _selectedTrip.asStateFlow()

    private val _tripForm = MutableStateFlow(TripFormState())
    val tripForm: StateFlow<TripFormState> = _tripForm.asStateFlow()

    private val _activityForm = MutableStateFlow(ActivityFormState())
    val activityForm: StateFlow<ActivityFormState> = _activityForm.asStateFlow()

    fun setUserId(uid: String) {
        _currentUserId.value = uid
        Log.d(TAG, "setUserId: $uid")
    }

    fun selectTrip(tripId: String) {
        viewModelScope.launch {
            _selectedTrip.value = repository.getTripById(tripId)
            Log.d(TAG, "selectTrip: $tripId")
        }
    }

    fun prepareAddTrip() { _tripForm.value = TripFormState() }

    fun prepareEditTrip(trip: Trip) {
        _tripForm.value = TripFormState(
            title = trip.title, description = trip.description, destination = trip.destination,
            startDate = trip.startDate, endDate = trip.endDate,
            budget = if (trip.budget > 0) trip.budget.toString() else "",
            isEditing = true, editingId = trip.id
        )
    }

    fun updateTripForm(update: TripFormState.() -> TripFormState) {
        _tripForm.update { it.update() }
    }

    fun saveTrip(onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val form = _tripForm.value
            var updated = form.copy(titleError = null, startDateError = null, endDateError = null)
            var hasError = false

            if (form.title.isBlank()) {
                updated = updated.copy(titleError = "Title is required")
                hasError = true
                Log.e(TAG, "saveTrip: title is empty")
            }
            if (form.startDate == null) {
                updated = updated.copy(startDateError = "Select a start date")
                hasError = true
            }
            if (form.endDate == null) {
                updated = updated.copy(endDateError = "Select an end date")
                hasError = true
            }
            if (form.startDate != null && form.endDate != null && !form.startDate.isBefore(form.endDate)) {
                updated = updated.copy(endDateError = "End date must be after start date")
                hasError = true
            }
            val userId = _currentUserId.value
            if (!hasError && repository.isTitleDuplicate(form.title.trim(), userId, form.editingId ?: "")) {
                updated = updated.copy(titleError = "A trip with this title already exists")
                hasError = true
                Log.e(TAG, "saveTrip: duplicate title")
            }

            _tripForm.value = updated
            if (hasError) { onResult(false); return@launch }

            val trip = Trip(
                id = if (form.isEditing) form.editingId!! else UUID.randomUUID().toString(),
                title = form.title.trim(), description = form.description.trim(),
                destination = form.destination.trim(), startDate = form.startDate!!,
                endDate = form.endDate!!, budget = form.budget.toDoubleOrNull() ?: 0.0,
                activities = if (form.isEditing) repository.getActivitiesForTrip(form.editingId!!).let { it } else emptyList()
            )

            if (form.isEditing) {
                repository.updateTrip(trip)
                Log.i(TAG, "saveTrip: updated '${trip.title}'")
            } else {
                repository.addTrip(trip, userId)
                Log.i(TAG, "saveTrip: added '${trip.title}'")
            }
            _refreshTrigger.value = System.currentTimeMillis()
            onResult(true)
        }
    }

    fun deleteTrip(tripId: String) {
        viewModelScope.launch {
            repository.deleteTrip(tripId)
            _refreshTrigger.value = System.currentTimeMillis()
            Log.i(TAG, "deleteTrip: $tripId deleted")
        }
    }

    fun prepareAddActivity() { _activityForm.value = ActivityFormState() }

    fun prepareEditActivity(activity: Activity) {
        _activityForm.value = ActivityFormState(
            title = activity.title, description = activity.description,
            date = activity.date, time = activity.time, location = activity.location,
            cost = if (activity.cost > 0) activity.cost.toString() else "",
            type = activity.type, isEditing = true, editingId = activity.id
        )
    }

    fun updateActivityForm(update: ActivityFormState.() -> ActivityFormState) {
        _activityForm.update { it.update() }
    }

    fun saveActivity(tripId: String, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val form = _activityForm.value
            val trip = repository.getTripById(tripId)
            var updated = form.copy(titleError = null, dateError = null, timeError = null)
            var hasError = false

            if (form.title.isBlank()) {
                updated = updated.copy(titleError = "Title is required"); hasError = true
            }
            if (form.date == null) {
                updated = updated.copy(dateError = "Select a date"); hasError = true
            }
            if (form.time == null) {
                updated = updated.copy(timeError = "Select a time"); hasError = true
            }
            if (trip != null && form.date != null) {
                if (form.date.isBefore(trip.startDate) || form.date.isAfter(trip.endDate)) {
                    updated = updated.copy(dateError = "Date must be within trip range (${trip.startDate} – ${trip.endDate})")
                    hasError = true
                }
            }

            _activityForm.value = updated
            if (hasError) { onResult(false); return@launch }

            val activity = Activity(
                id = if (form.isEditing) form.editingId!! else UUID.randomUUID().toString(),
                title = form.title.trim(), description = form.description.trim(),
                date = form.date!!, time = form.time!!, location = form.location.trim(),
                cost = form.cost.toDoubleOrNull() ?: 0.0, type = form.type
            )

            if (form.isEditing) repository.updateActivity(tripId, activity)
            else repository.addActivity(tripId, activity)

            selectTrip(tripId)
            _refreshTrigger.value = System.currentTimeMillis()
            Log.i(TAG, "saveActivity: '${activity.title}' saved")
            onResult(true)
        }
    }

    fun deleteActivity(tripId: String, activityId: String) {
        viewModelScope.launch {
            repository.deleteActivity(activityId)
            selectTrip(tripId)
            _refreshTrigger.value = System.currentTimeMillis()
            Log.i(TAG, "deleteActivity: $activityId deleted")
        }
    }
}