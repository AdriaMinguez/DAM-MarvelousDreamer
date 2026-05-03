package com.example.marvelousdreamer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String = "",
    val destination: String = "",
    val startDate: LocalDate,
    val endDate: LocalDate,
    val budget: Double = 0.0,
    val notes: String = "",
    val userId: String = ""
)
