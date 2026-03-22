package com.example.marvelousdreamer.domain

/**
 * Repository interface for Trip and Activity CRUD operations.
 * UI → ViewModel → TripRepository → TripRepositoryImpl → FakeTripDataSource
 */
interface TripRepository {
    fun getTrips(): List<Trip>
    fun getTripById(id: String): Trip?
    fun addTrip(trip: Trip)
    fun updateTrip(trip: Trip)
    fun deleteTrip(id: String)
    fun addActivity(tripId: String, activity: Activity)
    fun updateActivity(tripId: String, activity: Activity)
    fun deleteActivity(tripId: String, activityId: String)
}