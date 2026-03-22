package com.example.marvelousdreamer

import com.example.marvelousdreamer.data.fakeDB.FakeTripDataSource
import com.example.marvelousdreamer.domain.Trip
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for FakeTripDataSource InMemory CRUD operations (T3.2).
 * Verifies addTrip, getTripById, updateTrip, deleteTrip.
 */
class FakeTripDataSourceTest {

    @Before
    fun setUp() {
        // Clear all data before each test for isolation
        FakeTripDataSource.clearAll()
    }

    private fun createTrip(
        id    : String = "test_1",
        title : String = "Test Trip"
    ) = Trip(
        id          = id,
        title       = title,
        description = "Desc",
        startDate   = LocalDate.of(2026, 7, 1),
        endDate     = LocalDate.of(2026, 7, 10),
        budget      = 500.0
    )

    // ── getTrips ──────────────────────────────────────────────────────────────

    @Test
    fun `getTrips returns empty list after clearAll`() {
        val trips = FakeTripDataSource.getTrips()
        assertTrue("Should be empty after clear", trips.isEmpty())
    }

    @Test
    fun `getTrips returns all added trips`() {
        FakeTripDataSource.addTrip(createTrip(id = "t1", title = "Trip 1"))
        FakeTripDataSource.addTrip(createTrip(id = "t2", title = "Trip 2"))
        FakeTripDataSource.addTrip(createTrip(id = "t3", title = "Trip 3"))

        val trips = FakeTripDataSource.getTrips()
        assertEquals(3, trips.size)
    }

    // ── addTrip ───────────────────────────────────────────────────────────────

    @Test
    fun `addTrip increases trip count by one`() {
        assertEquals(0, FakeTripDataSource.getTrips().size)
        FakeTripDataSource.addTrip(createTrip())
        assertEquals(1, FakeTripDataSource.getTrips().size)
    }

    @Test
    fun `addTrip stores trip with correct data`() {
        val trip = createTrip(id = "my_trip", title = "My Trip")
        FakeTripDataSource.addTrip(trip)

        val stored = FakeTripDataSource.getTripById("my_trip")
        assertNotNull("Trip should be retrievable by id", stored)
        assertEquals("My Trip", stored?.title)
        assertEquals(500.0, stored?.budget ?: 0.0, 0.01)
    }

    // ── getTripById ───────────────────────────────────────────────────────────

    @Test
    fun `getTripById returns null for non-existent id`() {
        val result = FakeTripDataSource.getTripById("non_existent")
        assertNull("Should return null for unknown id", result)
    }

    @Test
    fun `getTripById returns correct trip`() {
        FakeTripDataSource.addTrip(createTrip(id = "alpha", title = "Alpha"))
        FakeTripDataSource.addTrip(createTrip(id = "beta", title = "Beta"))

        val trip = FakeTripDataSource.getTripById("beta")
        assertNotNull(trip)
        assertEquals("Beta", trip?.title)
    }

    // ── updateTrip ────────────────────────────────────────────────────────────

    @Test
    fun `updateTrip modifies existing trip`() {
        FakeTripDataSource.addTrip(createTrip(id = "upd", title = "Original"))
        val updated = createTrip(id = "upd", title = "Updated")
        FakeTripDataSource.updateTrip(updated)

        val result = FakeTripDataSource.getTripById("upd")
        assertEquals("Updated", result?.title)
    }

    @Test
    fun `updateTrip does not change trip count`() {
        FakeTripDataSource.addTrip(createTrip(id = "upd", title = "Original"))
        val updated = createTrip(id = "upd", title = "Updated")
        FakeTripDataSource.updateTrip(updated)

        assertEquals("Trip count should remain 1", 1, FakeTripDataSource.getTrips().size)
    }

    @Test
    fun `updateTrip with non-existent id does not add trip`() {
        FakeTripDataSource.addTrip(createTrip(id = "existing"))
        FakeTripDataSource.updateTrip(createTrip(id = "ghost", title = "Ghost"))

        assertEquals("Should still be 1 trip", 1, FakeTripDataSource.getTrips().size)
        assertNull(FakeTripDataSource.getTripById("ghost"))
    }

    // ── deleteTrip ────────────────────────────────────────────────────────────

    @Test
    fun `deleteTrip removes the trip`() {
        FakeTripDataSource.addTrip(createTrip(id = "del"))
        FakeTripDataSource.deleteTrip("del")

        assertEquals(0, FakeTripDataSource.getTrips().size)
        assertNull(FakeTripDataSource.getTripById("del"))
    }

    @Test
    fun `deleteTrip with non-existent id does nothing`() {
        FakeTripDataSource.addTrip(createTrip(id = "keep"))
        FakeTripDataSource.deleteTrip("non_existent")

        assertEquals("Should still have 1 trip", 1, FakeTripDataSource.getTrips().size)
    }

    @Test
    fun `deleteTrip only removes the matching trip`() {
        FakeTripDataSource.addTrip(createTrip(id = "a", title = "A"))
        FakeTripDataSource.addTrip(createTrip(id = "b", title = "B"))
        FakeTripDataSource.addTrip(createTrip(id = "c", title = "C"))

        FakeTripDataSource.deleteTrip("b")

        assertEquals(2, FakeTripDataSource.getTrips().size)
        assertNull(FakeTripDataSource.getTripById("b"))
        assertNotNull(FakeTripDataSource.getTripById("a"))
        assertNotNull(FakeTripDataSource.getTripById("c"))
    }
}
