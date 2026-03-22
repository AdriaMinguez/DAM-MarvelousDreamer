package com.example.marvelousdreamer

import com.example.marvelousdreamer.data.fakeDB.FakeTripDataSource
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Unit tests for TripViewModel validation and CRUD logic (T3.1, T3.2).
 * Validates input constraints: empty fields, incorrect dates, date ranges.
 */
class TripViewModelTest {

    private lateinit var viewModel: TripViewModel

    @Before
    fun setUp() {
        FakeTripDataSource.clearAll()
        viewModel = TripViewModel()
    }

    // ── Trip validation: empty title ──────────────────────────────────────────

    @Test
    fun `saveTrip fails with blank title`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "   ",
                startDate = LocalDate.of(2026, 7, 1),
                endDate   = LocalDate.of(2026, 7, 10)
            )
        }

        val result = viewModel.saveTrip()
        assertFalse("Should fail with blank title", result)
        assertNotNull("Title error should be set", viewModel.tripForm.value.titleError)
    }

    // ── Trip validation: missing dates ────────────────────────────────────────

    @Test
    fun `saveTrip fails with null startDate`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title   = "Valid Title",
                endDate = LocalDate.of(2026, 7, 10)
            )
        }

        val result = viewModel.saveTrip()
        assertFalse("Should fail without start date", result)
        assertNotNull(viewModel.tripForm.value.startDateError)
    }

    @Test
    fun `saveTrip fails with null endDate`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Valid Title",
                startDate = LocalDate.of(2026, 7, 1)
            )
        }

        val result = viewModel.saveTrip()
        assertFalse("Should fail without end date", result)
        assertNotNull(viewModel.tripForm.value.endDateError)
    }

    // ── Trip validation: date order ───────────────────────────────────────────

    @Test
    fun `saveTrip fails when endDate is before startDate`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Valid Title",
                startDate = LocalDate.of(2026, 7, 10),
                endDate   = LocalDate.of(2026, 7, 1)
            )
        }

        val result = viewModel.saveTrip()
        assertFalse("Should fail when end < start", result)
        assertNotNull(viewModel.tripForm.value.endDateError)
    }

    @Test
    fun `saveTrip fails when endDate equals startDate`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Valid Title",
                startDate = LocalDate.of(2026, 7, 5),
                endDate   = LocalDate.of(2026, 7, 5)
            )
        }

        val result = viewModel.saveTrip()
        assertFalse("Should fail when end == start", result)
    }

    // ── Trip validation: success ──────────────────────────────────────────────

    @Test
    fun `saveTrip succeeds with valid data`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Barcelona Weekend",
                startDate = LocalDate.of(2026, 9, 1),
                endDate   = LocalDate.of(2026, 9, 4),
                budget    = "500"
            )
        }

        val result = viewModel.saveTrip()
        assertTrue("Should succeed with valid data", result)
        assertEquals(1, viewModel.trips.value.size)
        assertEquals("Barcelona Weekend", viewModel.trips.value[0].title)
    }

    @Test
    fun `saveTrip clears previous errors on success`() {
        // First attempt: fail
        viewModel.prepareAddTrip()
        viewModel.updateTripForm { copy(title = "") }
        viewModel.saveTrip()
        assertNotNull(viewModel.tripForm.value.titleError)

        // Second attempt: succeed
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Valid",
                startDate = LocalDate.of(2026, 9, 1),
                endDate   = LocalDate.of(2026, 9, 5)
            )
        }
        viewModel.saveTrip()

        assertNull("Errors should be cleared", viewModel.tripForm.value.titleError)
    }

    // ── Edit trip ─────────────────────────────────────────────────────────────

    @Test
    fun `editing trip updates existing trip`() {
        // Add initial trip
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "Original",
                startDate = LocalDate.of(2026, 9, 1),
                endDate   = LocalDate.of(2026, 9, 5)
            )
        }
        viewModel.saveTrip()
        val tripId = viewModel.trips.value[0].id

        // Edit it
        viewModel.prepareEditTrip(viewModel.trips.value[0])
        viewModel.updateTripForm { copy(title = "Edited") }
        viewModel.saveTrip()

        assertEquals(1, viewModel.trips.value.size)
        assertEquals("Edited", viewModel.trips.value[0].title)
    }

    // ── Delete trip ───────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip removes trip from list`() {
        viewModel.prepareAddTrip()
        viewModel.updateTripForm {
            copy(
                title     = "To Delete",
                startDate = LocalDate.of(2026, 9, 1),
                endDate   = LocalDate.of(2026, 9, 5)
            )
        }
        viewModel.saveTrip()
        val tripId = viewModel.trips.value[0].id

        viewModel.deleteTrip(tripId)
        assertEquals(0, viewModel.trips.value.size)
    }

    // ── Activity validation ───────────────────────────────────────────────────

    @Test
    fun `saveActivity fails with blank title`() {
        addSampleTrip("trip_act")
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "",
                date  = LocalDate.of(2026, 9, 3),
                time  = LocalTime.of(10, 0)
            )
        }

        val result = viewModel.saveActivity("trip_act")
        assertFalse("Should fail with blank title", result)
        assertNotNull(viewModel.activityForm.value.titleError)
    }

    @Test
    fun `saveActivity fails with null date`() {
        addSampleTrip("trip_act2")
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "Valid Activity",
                time  = LocalTime.of(10, 0)
            )
        }

        val result = viewModel.saveActivity("trip_act2")
        assertFalse("Should fail without date", result)
        assertNotNull(viewModel.activityForm.value.dateError)
    }

    @Test
    fun `saveActivity fails with null time`() {
        addSampleTrip("trip_act3")
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "Valid Activity",
                date  = LocalDate.of(2026, 9, 3)
            )
        }

        val result = viewModel.saveActivity("trip_act3")
        assertFalse("Should fail without time", result)
        assertNotNull(viewModel.activityForm.value.timeError)
    }

    @Test
    fun `saveActivity fails when date outside trip range`() {
        addSampleTrip("trip_range")  // Sep 1 - Sep 5
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "Out of range",
                date  = LocalDate.of(2026, 10, 1),  // October, outside Sep range
                time  = LocalTime.of(10, 0)
            )
        }

        val result = viewModel.saveActivity("trip_range")
        assertFalse("Should fail with date outside trip range", result)
        assertNotNull(viewModel.activityForm.value.dateError)
    }

    @Test
    fun `saveActivity succeeds with valid data within trip range`() {
        addSampleTrip("trip_ok")
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "Museum Visit",
                date  = LocalDate.of(2026, 9, 3),
                time  = LocalTime.of(14, 0),
                cost  = "25"
            )
        }

        val result = viewModel.saveActivity("trip_ok")
        assertTrue("Should succeed with valid data", result)
    }

    // ── Delete activity ───────────────────────────────────────────────────────

    @Test
    fun `deleteActivity removes activity from trip`() {
        addSampleTrip("trip_del_act")
        viewModel.prepareAddActivity()
        viewModel.updateActivityForm {
            copy(
                title = "To Remove",
                date  = LocalDate.of(2026, 9, 2),
                time  = LocalTime.of(10, 0)
            )
        }
        viewModel.saveActivity("trip_del_act")

        val trip = viewModel.trips.value.find { it.id == "trip_del_act" }
        assertNotNull(trip)
        assertEquals(1, trip?.activities?.size)

        val actId = trip!!.activities[0].id
        viewModel.deleteActivity("trip_del_act", actId)

        val updatedTrip = viewModel.trips.value.find { it.id == "trip_del_act" }
        assertEquals(0, updatedTrip?.activities?.size)
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private fun addSampleTrip(id: String = "sample") {
        FakeTripDataSource.addTrip(
            Trip(
                id        = id,
                title     = "Sample",
                startDate = LocalDate.of(2026, 9, 1),
                endDate   = LocalDate.of(2026, 9, 5),
                budget    = 1000.0
            )
        )
        viewModel.loadTrips()
    }
}
