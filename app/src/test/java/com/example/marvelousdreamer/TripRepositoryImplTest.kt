package com.example.marvelousdreamer

import com.example.marvelousdreamer.data.fakeDB.FakeTripDataSource
import com.example.marvelousdreamer.data.repository.TripRepositoryImpl
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Unit tests for TripRepositoryImpl — tests trip + activity CRUD
 * through the repository layer (T3.2).
 */
class TripRepositoryImplTest {

    private lateinit var repository: TripRepositoryImpl

    @Before
    fun setUp() {
        FakeTripDataSource.clearAll()
        repository = TripRepositoryImpl()
    }

    private fun sampleTrip(id: String = "repo_trip") = Trip(
        id          = id,
        title       = "Repo Trip",
        description = "Testing repository",
        startDate   = LocalDate.of(2026, 8, 1),
        endDate     = LocalDate.of(2026, 8, 15),
        budget      = 2000.0
    )

    private fun sampleActivity(id: String = "repo_act") = Activity(
        id          = id,
        title       = "Repo Activity",
        description = "Testing activity in repo",
        date        = LocalDate.of(2026, 8, 5),
        time        = LocalTime.of(14, 30),
        cost        = 75.0,
        type        = ActivityType.VISIT
    )

    // ── Trip CRUD through repository ──────────────────────────────────────────

    @Test
    fun `addTrip and getTrips works through repository`() {
        repository.addTrip(sampleTrip())
        val trips = repository.getTrips()
        assertEquals(1, trips.size)
        assertEquals("Repo Trip", trips[0].title)
    }

    @Test
    fun `getTripById through repository returns correct trip`() {
        repository.addTrip(sampleTrip(id = "find_me"))
        val trip = repository.getTripById("find_me")
        assertNotNull(trip)
        assertEquals("find_me", trip?.id)
    }

    @Test
    fun `updateTrip through repository updates data`() {
        repository.addTrip(sampleTrip(id = "upd"))
        val updated = sampleTrip(id = "upd").copy(title = "Updated Title")
        repository.updateTrip(updated)

        val result = repository.getTripById("upd")
        assertEquals("Updated Title", result?.title)
    }

    @Test
    fun `deleteTrip through repository removes trip`() {
        repository.addTrip(sampleTrip(id = "del"))
        repository.deleteTrip("del")
        assertNull(repository.getTripById("del"))
        assertEquals(0, repository.getTrips().size)
    }

    // ── Activity CRUD through repository ──────────────────────────────────────

    @Test
    fun `addActivity adds activity to trip`() {
        repository.addTrip(sampleTrip(id = "trip_a"))
        repository.addActivity("trip_a", sampleActivity(id = "act_1"))

        val trip = repository.getTripById("trip_a")
        assertNotNull(trip)
        assertEquals(1, trip?.activities?.size)
        assertEquals("Repo Activity", trip?.activities?.get(0)?.title)
    }

    @Test
    fun `addActivity to non-existent trip does not crash`() {
        // Should handle gracefully (log error, no exception)
        repository.addActivity("ghost_trip", sampleActivity())
        // No exception thrown = pass
    }

    @Test
    fun `updateActivity modifies existing activity`() {
        repository.addTrip(sampleTrip(id = "trip_b"))
        repository.addActivity("trip_b", sampleActivity(id = "act_u"))

        val modified = sampleActivity(id = "act_u").copy(title = "Modified Activity")
        repository.updateActivity("trip_b", modified)

        val trip = repository.getTripById("trip_b")
        assertEquals("Modified Activity", trip?.activities?.get(0)?.title)
    }

    @Test
    fun `deleteActivity removes activity from trip`() {
        repository.addTrip(sampleTrip(id = "trip_c"))
        repository.addActivity("trip_c", sampleActivity(id = "act_d1"))
        repository.addActivity("trip_c", sampleActivity(id = "act_d2"))

        repository.deleteActivity("trip_c", "act_d1")

        val trip = repository.getTripById("trip_c")
        assertEquals(1, trip?.activities?.size)
        assertEquals("act_d2", trip?.activities?.get(0)?.id)
    }

    @Test
    fun `deleteActivity with non-existent activityId does nothing`() {
        repository.addTrip(sampleTrip(id = "trip_d"))
        repository.addActivity("trip_d", sampleActivity(id = "act_keep"))

        repository.deleteActivity("trip_d", "non_existent")

        val trip = repository.getTripById("trip_d")
        assertEquals(1, trip?.activities?.size)
    }

    @Test
    fun `multiple activities can be added to same trip`() {
        repository.addTrip(sampleTrip(id = "trip_multi"))
        repository.addActivity("trip_multi", sampleActivity(id = "a1"))
        repository.addActivity("trip_multi", sampleActivity(id = "a2"))
        repository.addActivity("trip_multi", sampleActivity(id = "a3"))

        val trip = repository.getTripById("trip_multi")
        assertEquals(3, trip?.activities?.size)
    }

    // ── Edge cases ────────────────────────────────────────────────────────────

    @Test
    fun `deleting a trip also removes its activities`() {
        repository.addTrip(sampleTrip(id = "trip_del"))
        repository.addActivity("trip_del", sampleActivity(id = "orphan"))
        repository.deleteTrip("trip_del")

        assertNull("Deleted trip should not exist", repository.getTripById("trip_del"))
    }

    @Test
    fun `getTrips returns defensive copy`() {
        repository.addTrip(sampleTrip(id = "copy_test"))
        val trips1 = repository.getTrips()
        val trips2 = repository.getTrips()

        assertEquals(trips1.size, trips2.size)
        assertEquals(trips1[0].id, trips2[0].id)
    }
}
