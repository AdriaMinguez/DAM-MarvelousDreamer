package com.example.marvelousdreamer.data.local.dao

import androidx.room.*
import com.example.marvelousdreamer.data.local.entity.AccessLogEntity

@Dao
interface AccessLogDao {
    @Insert
    suspend fun insert(log: AccessLogEntity)

    @Query("SELECT * FROM access_log WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getLogsByUser(userId: String): List<AccessLogEntity>
}
