package com.example.marvelousdreamer.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.marvelousdreamer.data.repository.TripRepositoryImpl
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.domain.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

// ── Form state data classes ───────────────────────────────────────────────────

data class TripFormState(
    val title        : String     = "",
    val description  : String     = "",
    val destination  : String     = "",
    val startDate    : LocalDate? = null,
    val endDate      : LocalDate? = null,
    val budget       : String     = "",
    // Validation errors — null means no error
    val titleError   : String?    = null,
    val startDateError: String?   = null,
    val endDateError : String?    = null,
    // Edit mode
    val isEditing    : Boolean    = false,
    val editingId    : String?    = null
)

data class ActivityFormState(
    val title      : String       = "",
    val description: String       = "",
    val date       : LocalDate?   = null,
    val time       : LocalTime?   = null,
    val location   : String       = "",
    val cost       : String       = "",
    val type       : ActivityType = ActivityType.OTHER,
    // Validation errors
    val titleError : String?      = null,
    val dateError  : String?      = null,
    val timeError  : String?      = null,
    // Edit mode
    val isEditing  : Boolean      = false,
    val editingId  : String?      = null
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

/**
 * Manages Trip and Activity CRUD operations.
 * Architecture: UI → TripViewModel → TripRepository → FakeTripDataSource
 */
class TripViewModel : ViewModel() {

    companion object {
        private const val TAG = "TripViewModel"
    }

    private val repository: TripRepository = TripRepositoryImpl()

    // Exposed state
    private val _trips        = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    private val _selectedTrip = MutableStateFlow<Trip?>(null)
    val selectedTrip: StateFlow<Trip?> = _selectedTrip.asStateFlow()

    private val _tripForm     = MutableStateFlow(TripFormState())
    val tripForm: StateFlow<TripFormState> = _tripForm.asStateFlow()

    private val _activityForm = MutableStateFlow(ActivityFormState())
    val activityForm: StateFlow<ActivityFormState> = _activityForm.asStateFlow()

    init { loadTrips() }

    // ── Trip CRUD ─────────────────────────────────────────────────────────────

    fun loadTrips() {
        _trips.value = repository.getTrips()
        Log.d(TAG, "loadTrips: ${_trips.value.size} trips loaded")
    }

    fun selectTrip(tripId: String) {
        _selectedTrip.value = repository.getTripById(tripId)
        Log.d(TAG, "selectTrip: $tripId -> found=${_selectedTrip.value != null}")
    }

    /** Prepares the form for adding a new trip. */
    fun prepareAddTrip() {
        _tripForm.value = TripFormState()
    }

    /** Prepares the form pre-filled for editing an existing trip. */
    fun prepareEditTrip(trip: Trip) {
        _tripForm.value = TripFormState(
            title       = trip.title,
            description = trip.description,
            destination = trip.destination,
            startDate   = trip.startDate,
            endDate     = trip.endDate,
            budget      = if (trip.budget > 0) trip.budget.toString() else "",
            isEditing   = true,
            editingId   = trip.id
        )
    }

    /** Updates a single field in the trip form. */
    fun updateTripForm(update: TripFormState.() -> TripFormState) {
        _tripForm.update { it.update() }
    }

    /**
     * Validates the form and persists the trip.
     * @return true if saved successfully, false if validation failed.
     */
    fun saveTrip(): Boolean {
        val form = _tripForm.value
        var updated = form.copy(titleError = null, startDateError = null, endDateError = null)
        var hasError = false

        if (form.title.isBlank()) {
            updated = updated.copy(titleError = "El títol és obligatori")
            hasError = true
            Log.e(TAG, "saveTrip: title is empty")
        }
        if (form.startDate == null) {
            updated = updated.copy(startDateError = "Selecciona una data d'inici")
            hasError = true
            Log.e(TAG, "saveTrip: startDate is null")
        }
        if (form.endDate == null) {
            updated = updated.copy(endDateError = "Selecciona una data de fi")
            hasError = true
            Log.e(TAG, "saveTrip: endDate is null")
        }
        if (form.startDate != null && form.endDate != null &&
            !form.startDate.isBefore(form.endDate)) {
            updated = updated.copy(endDateError = "La data de fi ha de ser posterior a la d'inici")
            hasError = true
            Log.e(TAG, "saveTrip: endDate not after startDate")
        }

        _tripForm.value = updated
        if (hasError) return false

        val trip = Trip(
            id          = if (form.isEditing) form.editingId!! else UUID.randomUUID().toString(),
            title       = form.title.trim(),
            description = form.description.trim(),
            destination = form.destination.trim(),
            startDate   = form.startDate!!,
            endDate     = form.endDate!!,
            budget      = form.budget.toDoubleOrNull() ?: 0.0,
            activities  = if (form.isEditing) repository.getTripById(form.editingId!!)
                ?.activities ?: emptyList()
            else emptyList()
        )

        if (form.isEditing) {
            repository.updateTrip(trip)
            Log.i(TAG, "saveTrip: updated '${trip.title}'")
        } else {
            repository.addTrip(trip)
            Log.i(TAG, "saveTrip: added '${trip.title}'")
        }

        loadTrips()
        return true
    }

    fun deleteTrip(tripId: String) {
        repository.deleteTrip(tripId)
        loadTrips()
        Log.i(TAG, "deleteTrip: $tripId deleted")
    }

    // ── Activity CRUD ─────────────────────────────────────────────────────────

    /** Prepares the form for adding a new activity. */
    fun prepareAddActivity() {
        _activityForm.value = ActivityFormState()
    }

    /** Prepares the form pre-filled for editing an existing activity. */
    fun prepareEditActivity(activity: Activity) {
        _activityForm.value = ActivityFormState(
            title       = activity.title,
            description = activity.description,
            date        = activity.date,
            time        = activity.time,
            location    = activity.location,
            cost        = if (activity.cost > 0) activity.cost.toString() else "",
            type        = activity.type,
            isEditing   = true,
            editingId   = activity.id
        )
    }

    /** Updates a single field in the activity form. */
    fun updateActivityForm(update: ActivityFormState.() -> ActivityFormState) {
        _activityForm.update { it.update() }
    }

    /**
     * Validates the form and persists the activity inside the given trip.
     * @return true if saved successfully, false if validation failed.
     */
    fun saveActivity(tripId: String): Boolean {
        val form = _activityForm.value
        val trip = repository.getTripById(tripId)
        var updated = form.copy(titleError = null, dateError = null, timeError = null)
        var hasError = false

        if (form.title.isBlank()) {
            updated = updated.copy(titleError = "El títol és obligatori")
            hasError = true
            Log.e(TAG, "saveActivity: title is empty")
        }
        if (form.date == null) {
            updated = updated.copy(dateError = "Selecciona una data")
            hasError = true
            Log.e(TAG, "saveActivity: date is null")
        }
        if (form.time == null) {
            updated = updated.copy(timeError = "Selecciona una hora")
            hasError = true
            Log.e(TAG, "saveActivity: time is null")
        }
        if (trip != null && form.date != null) {
            if (form.date.isBefore(trip.startDate) || form.date.isAfter(trip.endDate)) {
                updated = updated.copy(dateError = "La data ha d'estar dins del rang del viatge " +
                        "(${trip.startDate} – ${trip.endDate})")
                hasError = true
                Log.e(TAG, "saveActivity: date outside trip range")
            }
        }

        _activityForm.value = updated
        if (hasError) return false

        val activity = Activity(
            id          = if (form.isEditing) form.editingId!! else UUID.randomUUID().toString(),
            title       = form.title.trim(),
            description = form.description.trim(),
            date        = form.date!!,
            time        = form.time!!,
            location    = form.location.trim(),
            cost        = form.cost.toDoubleOrNull() ?: 0.0,
            type        = form.type
        )

        if (form.isEditing) {
            repository.updateActivity(tripId, activity)
            Log.i(TAG, "saveActivity: updated '${activity.title}'")
        } else {
            repository.addActivity(tripId, activity)
            Log.i(TAG, "saveActivity: added '${activity.title}'")
        }

        selectTrip(tripId)
        loadTrips()
        return true
    }

    fun deleteActivity(tripId: String, activityId: String) {
        repository.deleteActivity(tripId, activityId)
        selectTrip(tripId)
        loadTrips()
        Log.i(TAG, "deleteActivity: $activityId deleted from trip $tripId")
    }
}
