package com.example.marvelousdreamer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey val id: String,
    val tripId: String,
    val hotelId: String,
    val hotelName: String,
    val hotelAddress: String,
    val hotelImageUrl: String,
    val roomId: String,
    val roomType: String,
    val price: Float,
    val roomImages: String,
    val startDate: String,
    val endDate: String,
    val guestName: String,
    val guestEmail: String,
    val userId: String
)
