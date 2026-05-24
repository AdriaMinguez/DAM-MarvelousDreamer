package com.example.marvelousdreamer.data.local.dao

import androidx.room.*
import com.example.marvelousdreamer.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE tripId = :tripId")
    fun getImagesByTrip(tripId: String): Flow<List<ImageEntity>>

    @Insert
    suspend fun insert(image: ImageEntity)

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM images WHERE uri = :uri")
    suspend fun deleteByUri(uri: String)
}
