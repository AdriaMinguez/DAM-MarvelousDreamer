package com.example.marvelousdreamer.data.repository

import android.util.Log
import com.example.marvelousdreamer.data.fakeDB.FakeTripDataSource
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.domain.TripRepository

/**
 * Concrete implementation of TripRepository.
 * Delegates all persistence to FakeTripDataSource (inMemory).
 * Sprint 03: replace FakeTripDataSource with Room DAO.
 */
class TripRepositoryImpl : TripRepository {

    companion object {
        private const val TAG = "TripRepositoryImpl"
    }

    private val dataSource = FakeTripDataSource

    override fun getTrips(): List<Trip> = dataSource.getTrips()

    override fun getTripById(id: String): Trip? = dataSource.getTripById(id)

    override fun addTrip(trip: Trip) {
        Log.d(TAG, "addTrip: delegating '${trip.title}'")
        dataSource.addTrip(trip)
    }

    override fun updateTrip(trip: Trip) {
        Log.d(TAG, "updateTrip: delegating '${trip.title}'")
        dataSource.updateTrip(trip)
    }

    override fun deleteTrip(id: String) {
        Log.d(TAG, "deleteTrip: delegating $id")
        dataSource.deleteTrip(id)
    }

    override fun addActivity(tripId: String, activity: Activity) {
        val trip = dataSource.getTripById(tripId)
        if (trip != null) {
            dataSource.updateTrip(trip.addActivity(activity))
            Log.i(TAG, "addActivity: '${activity.title}' added to trip $tripId")
        } else {
            Log.e(TAG, "addActivity: trip $tripId not found")
        }
    }

    override fun updateActivity(tripId: String, activity: Activity) {
        val trip = dataSource.getTripById(tripId)
        if (trip != null) {
            dataSource.updateTrip(trip.updateActivity(activity))
            Log.i(TAG, "updateActivity: '${activity.title}' updated in trip $tripId")
        } else {
            Log.e(TAG, "updateActivity: trip $tripId not found")
        }
    }

    override fun deleteActivity(tripId: String, activityId: String) {
        val trip = dataSource.getTripById(tripId)
        if (trip != null) {
            dataSource.updateTrip(trip.removeActivity(activityId))
            Log.i(TAG, "deleteActivity: $activityId removed from trip $tripId")
        } else {
            Log.e(TAG, "deleteActivity: trip $tripId not found")
        }
    }
}
