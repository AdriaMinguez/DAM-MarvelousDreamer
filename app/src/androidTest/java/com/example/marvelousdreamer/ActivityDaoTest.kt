package com.example.marvelousdreamer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.marvelousdreamer.data.local.AppDatabase
import com.example.marvelousdreamer.data.local.dao.ActivityDao
import com.example.marvelousdreamer.data.local.dao.TripDao
import com.example.marvelousdreamer.data.local.entity.ActivityEntity
import com.example.marvelousdreamer.data.local.entity.TripEntity
import com.example.marvelousdreamer.domain.ActivityType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class ActivityDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var activityDao: ActivityDao
    private lateinit var tripDao: TripDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        activityDao = db.activityDao()
        tripDao = db.tripDao()
        // Insert parent trip (foreign key requirement)
        runBlocking {
            tripDao.insert(TripEntity(
                id = "trip1", title = "Trip", startDate = LocalDate.of(2026, 6, 1),
                endDate = LocalDate.of(2026, 6, 10), userId = "user1"
            ))
        }
    }

    @After
    fun tearDown() { db.close() }

    private fun sampleActivity(id: String = "a1", tripId: String = "trip1") =
        ActivityEntity(
            id = id, tripId = tripId, title = "Activity", description = "Desc",
            date = LocalDate.of(2026, 6, 5), time = LocalTime.of(10, 0),
            location = "Place", cost = 50.0, type = ActivityType.VISIT
        )

    @Test
    fun insertAndGetActivity() = runBlocking {
        activityDao.insert(sampleActivity())
        val activity = activityDao.getActivityById("a1")
        assertNotNull(activity)
        assertEquals("Activity", activity?.title)
    }

    @Test
    fun getActivitiesByTrip() = runBlocking {
        activityDao.insert(sampleActivity(id = "a1"))
        activityDao.insert(sampleActivity(id = "a2"))
        activityDao.insert(sampleActivity(id = "a3"))

        val activities = activityDao.getActivitiesByTrip("trip1").first()
        assertEquals(3, activities.size)
    }

    @Test
    fun updateActivity() = runBlocking {
        activityDao.insert(sampleActivity(id = "a1"))
        activityDao.update(sampleActivity(id = "a1").copy(title = "Updated"))
        val activity = activityDao.getActivityById("a1")
        assertEquals("Updated", activity?.title)
    }

    @Test
    fun deleteActivity() = runBlocking {
        activityDao.insert(sampleActivity(id = "a1"))
        activityDao.deleteById("a1")
        val activity = activityDao.getActivityById("a1")
        assertNull(activity)
    }

    @Test
    fun cascadeDeleteOnTripRemoval() = runBlocking {
        activityDao.insert(sampleActivity(id = "a1"))
        activityDao.insert(sampleActivity(id = "a2"))
        tripDao.deleteById("trip1")
        val activities = activityDao.getActivitiesByTripOnce("trip1")
        assertEquals(0, activities.size)
    }

    @Test
    fun activitiesOrderedByDateAndTime() = runBlocking {
        activityDao.insert(sampleActivity(id = "a1").copy(date = LocalDate.of(2026, 6, 7), time = LocalTime.of(14, 0)))
        activityDao.insert(sampleActivity(id = "a2").copy(date = LocalDate.of(2026, 6, 5), time = LocalTime.of(9, 0)))
        activityDao.insert(sampleActivity(id = "a3").copy(date = LocalDate.of(2026, 6, 5), time = LocalTime.of(15, 0)))

        val activities = activityDao.getActivitiesByTrip("trip1").first()
        assertEquals("a2", activities[0].id)
        assertEquals("a3", activities[1].id)
        assertEquals("a1", activities[2].id)
    }
}
