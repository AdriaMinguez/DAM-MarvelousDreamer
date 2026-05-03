package com.example.marvelousdreamer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "access_log")
data class AccessLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val action: String,
    val timestamp: Long
)
