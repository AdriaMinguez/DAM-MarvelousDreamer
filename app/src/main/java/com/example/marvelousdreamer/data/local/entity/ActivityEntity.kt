package com.example.marvelousdreamer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.marvelousdreamer.domain.ActivityType
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "activities",
    foreignKeys = [ForeignKey(
        entity = TripEntity::class,
        parentColumns = ["id"],
        childColumns = ["tripId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ActivityEntity(
    @PrimaryKey val id: String,
    val tripId: String,
    val title: String,
    val description: String = "",
    val date: LocalDate,
    val time: LocalTime,
    val location: String = "",
    val cost: Double = 0.0,
    val type: ActivityType = ActivityType.OTHER
)
