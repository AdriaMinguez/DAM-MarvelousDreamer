package com.example.marvelousdreamer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.TripDao
import com.example.marvelousdreamer.data.local.entity.TripEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tripDao: TripDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tripDao = db.tripDao()
    }

    @After
    fun tearDown() { db.close() }

    private fun sampleTrip(id: String = "t1", title: String = "Test Trip", userId: String = "user1") =
        TripEntity(
            id = id, title = title, description = "Desc", destination = "City",
            startDate = LocalDate.of(2026, 6, 1), endDate = LocalDate.of(2026, 6, 10),
            budget = 1000.0, userId = userId
        )

    @Test
    fun insertAndGetTrip() = runBlocking {
        tripDao.insert(sampleTrip())
        val trip = tripDao.getTripById("t1")
        assertNotNull(trip)
        assertEquals("Test Trip", trip?.title)
    }

    @Test
    fun getTripsByUser_returnsOnlyUserTrips() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1", userId = "user1"))
        tripDao.insert(sampleTrip(id = "t2", userId = "user2"))
        tripDao.insert(sampleTrip(id = "t3", userId = "user1"))

        val user1Trips = tripDao.getTripsByUser("user1").first()
        assertEquals(2, user1Trips.size)
    }

    @Test
    fun updateTrip() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1", title = "Original"))
        tripDao.update(sampleTrip(id = "t1", title = "Updated"))
        val trip = tripDao.getTripById("t1")
        assertEquals("Updated", trip?.title)
    }

    @Test
    fun deleteTrip() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1"))
        tripDao.deleteById("t1")
        val trip = tripDao.getTripById("t1")
        assertNull(trip)
    }

    @Test
    fun isTitleDuplicate_detectsDuplicate() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1", title = "Kyoto"))
        val isDuplicate = tripDao.isTitleDuplicate("Kyoto", "user1")
        assertTrue(isDuplicate)
    }

    @Test
    fun isTitleDuplicate_excludesSelf() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1", title = "Kyoto"))
        val isDuplicate = tripDao.isTitleDuplicate("Kyoto", "user1", excludeId = "t1")
        assertFalse(isDuplicate)
    }

    @Test
    fun isTitleDuplicate_differentUser() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1", title = "Kyoto", userId = "user1"))
        val isDuplicate = tripDao.isTitleDuplicate("Kyoto", "user2")
        assertFalse(isDuplicate)
    }

    @Test
    fun getTripsByUser_orderedByStartDate() = runBlocking {
        tripDao.insert(sampleTrip(id = "t1").copy(startDate = LocalDate.of(2026, 12, 1)))
        tripDao.insert(sampleTrip(id = "t2").copy(startDate = LocalDate.of(2026, 3, 1)))
        tripDao.insert(sampleTrip(id = "t3").copy(startDate = LocalDate.of(2026, 7, 1)))

        val trips = tripDao.getTripsByUser("user1").first()
        assertEquals("t2", trips[0].id)
        assertEquals("t3", trips[1].id)
        assertEquals("t1", trips[2].id)
    }
}
