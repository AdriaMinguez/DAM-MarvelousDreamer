package com.example.marvelousdreamer.domain

import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrips(userId: String): Flow<List<Trip>>
    suspend fun getTripById(id: String): Trip?
    suspend fun isTitleDuplicate(title: String, userId: String, excludeId: String = ""): Boolean
    suspend fun addTrip(trip: Trip, userId: String)
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(id: String)
    suspend fun addActivity(tripId: String, activity: Activity)
    suspend fun updateActivity(tripId: String, activity: Activity)
    suspend fun deleteActivity(activityId: String)
    suspend fun getActivitiesForTrip(tripId: String): List<Activity>
}