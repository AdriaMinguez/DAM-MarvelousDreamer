package com.example.marvelousdreamer.data.repository

import android.util.Log
import com.example.marvelousdreamer.data.local.dao.ActivityDao
import com.example.marvelousdreamer.data.local.dao.TripDao
import com.example.marvelousdreamer.data.local.entity.ActivityEntity
import com.example.marvelousdreamer.data.local.entity.TripEntity
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.domain.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Room-based implementation of TripRepository (Sprint 03).
 * Replaces FakeTripDataSource with persistent SQLite storage.
 */
class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao,
    private val activityDao: ActivityDao
) : TripRepository {

    companion object { private const val TAG = "TripRepositoryImpl" }

    override fun getTrips(userId: String): Flow<List<Trip>> =
        tripDao.getTripsByUser(userId).map { entities ->
            entities.map { entity ->
                val activities = activityDao.getActivitiesByTripOnce(entity.id)
                entity.toDomain(activities.map { it.toDomain() })
            }
        }

    override suspend fun getTripById(id: String): Trip? {
        val entity = tripDao.getTripById(id) ?: return null
        val activities = activityDao.getActivitiesByTripOnce(id)
        return entity.toDomain(activities.map { it.toDomain() })
    }

    override suspend fun isTitleDuplicate(title: String, userId: String, excludeId: String): Boolean =
        tripDao.isTitleDuplicate(title, userId, excludeId)

    override suspend fun addTrip(trip: Trip, userId: String) {
        tripDao.insert(trip.toEntity(userId))
        Log.i(TAG, "addTrip: '${trip.title}' inserted")
    }

    override suspend fun updateTrip(trip: Trip) {
        val existing = tripDao.getTripById(trip.id)
        if (existing != null) {
            tripDao.update(trip.toEntity(existing.userId))
            Log.i(TAG, "updateTrip: '${trip.title}' updated")
        } else {
            Log.e(TAG, "updateTrip: trip ${trip.id} not found")
        }
    }

    override suspend fun deleteTrip(id: String) {
        tripDao.deleteById(id)
        Log.i(TAG, "deleteTrip: $id deleted")
    }

    override suspend fun addActivity(tripId: String, activity: Activity) {
        activityDao.insert(activity.toEntity(tripId))
        Log.i(TAG, "addActivity: '${activity.title}' added to trip $tripId")
    }

    override suspend fun updateActivity(tripId: String, activity: Activity) {
        activityDao.update(activity.toEntity(tripId))
        Log.i(TAG, "updateActivity: '${activity.title}' updated")
    }

    override suspend fun deleteActivity(activityId: String) {
        activityDao.deleteById(activityId)
        Log.i(TAG, "deleteActivity: $activityId deleted")
    }

    override suspend fun getActivitiesForTrip(tripId: String): List<Activity> =
        activityDao.getActivitiesByTripOnce(tripId).map { it.toDomain() }
}

// ── Mapping extensions ────────────────────────────────────────────────────────

fun TripEntity.toDomain(activities: List<Activity> = emptyList()) = Trip(
    id = id, title = title, description = description, destination = destination,
    startDate = startDate, endDate = endDate, budget = budget, notes = notes,
    activities = activities
)

fun Trip.toEntity(userId: String) = TripEntity(
    id = id, title = title, description = description, destination = destination,
    startDate = startDate, endDate = endDate, budget = budget, notes = notes,
    userId = userId
)

fun ActivityEntity.toDomain() = Activity(
    id = id, title = title, description = description, date = date,
    time = time, location = location, cost = cost, type = type
)

fun Activity.toEntity(tripId: String) = ActivityEntity(
    id = id, tripId = tripId, title = title, description = description,
    date = date, time = time, location = location, cost = cost, type = type
)
