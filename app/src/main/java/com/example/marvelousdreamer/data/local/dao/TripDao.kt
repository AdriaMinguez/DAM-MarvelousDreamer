package com.example.marvelousdreamer.data.local.dao

import androidx.room.*
import com.example.marvelousdreamer.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips WHERE userId = :userId ORDER BY startDate ASC")
    fun getTripsByUser(userId: String): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    suspend fun getTripById(id: String): TripEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM trips WHERE title = :title AND userId = :userId AND id != :excludeId)")
    suspend fun isTitleDuplicate(title: String, userId: String, excludeId: String = ""): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity)

    @Update
    suspend fun update(trip: TripEntity)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteById(id: String)
}
