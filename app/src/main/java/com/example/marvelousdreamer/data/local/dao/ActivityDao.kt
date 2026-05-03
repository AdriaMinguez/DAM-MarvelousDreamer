package com.example.marvelousdreamer.data.local.dao

import androidx.room.*
import com.example.marvelousdreamer.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    fun getActivitiesByTrip(tripId: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE tripId = :tripId ORDER BY date ASC, time ASC")
    suspend fun getActivitiesByTripOnce(tripId: String): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE id = :id")
    suspend fun getActivityById(id: String): ActivityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity)

    @Update
    suspend fun update(activity: ActivityEntity)

    @Query("DELETE FROM activities WHERE id = :id")
    suspend fun deleteById(id: String)
}
