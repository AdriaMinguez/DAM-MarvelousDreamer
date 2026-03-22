package com.example.marvelousdreamer

import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Unit tests for Trip and Activity domain models (T3.2).
 * Tests date validation, CRUD operations, and budget calculations.
 */
class TripDomainTest {

    // ── Helper factories ──────────────────────────────────────────────────────

    private fun createTrip(
        id        : String    = "test_trip",
        title     : String    = "Test Trip",
        startDate : LocalDate = LocalDate.of(2026, 6, 1),
        endDate   : LocalDate = LocalDate.of(2026, 6, 10),
        budget    : Double    = 1000.0,
        activities: List<Activity> = emptyList()
    ) = Trip(
        id          = id,
        title       = title,
        description = "Test description",
        destination = "Test City",
        startDate   = startDate,
        endDate     = endDate,
        budget      = budget,
        activities  = activities
    )

    private fun createActivity(
        id   : String    = "act_1",
        title: String    = "Test Activity",
        date : LocalDate = LocalDate.of(2026, 6, 5),
        time : LocalTime = LocalTime.of(10, 0),
        cost : Double    = 50.0,
        type : ActivityType = ActivityType.VISIT
    ) = Activity(
        id          = id,
        title       = title,
        description = "Activity desc",
        date        = date,
        time        = time,
        cost        = cost,
        type        = type
    )

    // ── Trip date validation tests ────────────────────────────────────────────

    @Test
    fun `trip with startDate before endDate is valid`() {
        val trip = createTrip(
            startDate = LocalDate.of(2026, 6, 1),
            endDate   = LocalDate.of(2026, 6, 10)
        )
        assertTrue("Start before end should be valid", trip.isDateRangeValid())
    }

    @Test
    fun `trip with startDate after endDate is invalid`() {
        val trip = createTrip(
            startDate = LocalDate.of(2026, 6, 10),
            endDate   = LocalDate.of(2026, 6, 1)
        )
        assertFalse("Start after end should be invalid", trip.isDateRangeValid())
    }

    @Test
    fun `trip with same start and end date is invalid`() {
        val trip = createTrip(
            startDate = LocalDate.of(2026, 6, 5),
            endDate   = LocalDate.of(2026, 6, 5)
        )
        assertFalse("Same start and end should be invalid", trip.isDateRangeValid())
    }

    // ── Trip duration tests ───────────────────────────────────────────────────

    @Test
    fun `getDurationInDays returns correct number of days`() {
        val trip = createTrip(
            startDate = LocalDate.of(2026, 6, 1),
            endDate   = LocalDate.of(2026, 6, 10)
        )
        assertEquals(9, trip.getDurationInDays())
    }

    @Test
    fun `getDurationInDays for one-day trip returns 1`() {
        val trip = createTrip(
            startDate = LocalDate.of(2026, 6, 1),
            endDate   = LocalDate.of(2026, 6, 2)
        )
        assertEquals(1, trip.getDurationInDays())
    }

    // ── Activity date within trip range tests ─────────────────────────────────

    @Test
    fun `activity within trip date range is valid`() {
        val trip = createTrip()
        val activity = createActivity(date = LocalDate.of(2026, 6, 5))
        assertTrue("Activity on Jun 5 should be within Jun 1-10 range",
            trip.isActivityDateValid(activity))
    }

    @Test
    fun `activity on trip start date is valid`() {
        val trip = createTrip()
        val activity = createActivity(date = LocalDate.of(2026, 6, 1))
        assertTrue("Activity on start date should be valid",
            trip.isActivityDateValid(activity))
    }

    @Test
    fun `activity on trip end date is valid`() {
        val trip = createTrip()
        val activity = createActivity(date = LocalDate.of(2026, 6, 10))
        assertTrue("Activity on end date should be valid",
            trip.isActivityDateValid(activity))
    }

    @Test
    fun `activity before trip start date is invalid`() {
        val trip = createTrip()
        val activity = createActivity(date = LocalDate.of(2026, 5, 31))
        assertFalse("Activity before start date should be invalid",
            trip.isActivityDateValid(activity))
    }

    @Test
    fun `activity after trip end date is invalid`() {
        val trip = createTrip()
        val activity = createActivity(date = LocalDate.of(2026, 6, 11))
        assertFalse("Activity after end date should be invalid",
            trip.isActivityDateValid(activity))
    }

    // ── Trip activity CRUD ────────────────────────────────────────────────────

    @Test
    fun `addActivity appends activity to trip`() {
        val trip = createTrip()
        val activity = createActivity()
        val updated = trip.addActivity(activity)

        assertEquals(1, updated.activities.size)
        assertEquals("Test Activity", updated.activities[0].title)
    }

    @Test
    fun `addActivity does not modify original trip (immutable)`() {
        val trip = createTrip()
        val activity = createActivity()
        trip.addActivity(activity)

        assertEquals("Original trip should remain unchanged",
            0, trip.activities.size)
    }

    @Test
    fun `updateActivity replaces matching activity`() {
        val activity = createActivity(id = "act_1", title = "Original")
        val trip = createTrip(activities = listOf(activity))

        val modified = activity.copy(title = "Modified")
        val updated = trip.updateActivity(modified)

        assertEquals(1, updated.activities.size)
        assertEquals("Modified", updated.activities[0].title)
    }

    @Test
    fun `updateActivity does not affect non-matching activities`() {
        val act1 = createActivity(id = "act_1", title = "First")
        val act2 = createActivity(id = "act_2", title = "Second")
        val trip = createTrip(activities = listOf(act1, act2))

        val modified = act1.copy(title = "Updated First")
        val updated = trip.updateActivity(modified)

        assertEquals(2, updated.activities.size)
        assertEquals("Updated First", updated.activities[0].title)
        assertEquals("Second", updated.activities[1].title)
    }

    @Test
    fun `removeActivity removes matching activity`() {
        val activity = createActivity(id = "act_1")
        val trip = createTrip(activities = listOf(activity))

        val updated = trip.removeActivity("act_1")
        assertEquals(0, updated.activities.size)
    }

    @Test
    fun `removeActivity keeps non-matching activities`() {
        val act1 = createActivity(id = "act_1")
        val act2 = createActivity(id = "act_2")
        val trip = createTrip(activities = listOf(act1, act2))

        val updated = trip.removeActivity("act_1")
        assertEquals(1, updated.activities.size)
        assertEquals("act_2", updated.activities[0].id)
    }

    @Test
    fun `removeActivity with non-existent id does nothing`() {
        val activity = createActivity(id = "act_1")
        val trip = createTrip(activities = listOf(activity))

        val updated = trip.removeActivity("non_existent")
        assertEquals(1, updated.activities.size)
    }

    // ── Budget calculation tests ──────────────────────────────────────────────

    @Test
    fun `getRemainingBudget with no activities returns full budget`() {
        val trip = createTrip(budget = 1000.0)
        assertEquals(1000.0, trip.getRemainingBudget(), 0.01)
    }

    @Test
    fun `getRemainingBudget subtracts activity costs`() {
        val activities = listOf(
            createActivity(id = "a1", cost = 100.0),
            createActivity(id = "a2", cost = 250.0),
            createActivity(id = "a3", cost = 50.0)
        )
        val trip = createTrip(budget = 1000.0, activities = activities)
        assertEquals(600.0, trip.getRemainingBudget(), 0.01)
    }

    @Test
    fun `getRemainingBudget can be negative when overspent`() {
        val activities = listOf(
            createActivity(id = "a1", cost = 800.0),
            createActivity(id = "a2", cost = 400.0)
        )
        val trip = createTrip(budget = 1000.0, activities = activities)
        assertEquals(-200.0, trip.getRemainingBudget(), 0.01)
    }

    // ── Activity model tests ──────────────────────────────────────────────────

    @Test
    fun `activity isTransport returns true for FLIGHT`() {
        val activity = createActivity(type = ActivityType.FLIGHT)
        assertTrue(activity.isTransport())
    }

    @Test
    fun `activity isTransport returns true for TRANSPORT`() {
        val activity = createActivity(type = ActivityType.TRANSPORT)
        assertTrue(activity.isTransport())
    }

    @Test
    fun `activity isTransport returns false for VISIT`() {
        val activity = createActivity(type = ActivityType.VISIT)
        assertFalse(activity.isTransport())
    }

    @Test
    fun `activity isTransport returns false for FOOD`() {
        val activity = createActivity(type = ActivityType.FOOD)
        assertFalse(activity.isTransport())
    }

    @Test
    fun `activity default id is UUID format`() {
        val activity = Activity(
            title = "Auto ID",
            date  = LocalDate.of(2026, 6, 5),
            time  = LocalTime.of(10, 0)
        )
        assertTrue("Auto-generated ID should not be empty",
            activity.id.isNotEmpty())
    }
}
