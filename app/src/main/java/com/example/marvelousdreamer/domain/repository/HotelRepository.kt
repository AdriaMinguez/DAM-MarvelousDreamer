package com.example.marvelousdreamer.domain.repository

import com.example.marvelousdreamer.domain.model.Hotel
import com.example.marvelousdreamer.domain.model.Reservation
import com.example.marvelousdreamer.domain.model.ReserveRequest

interface HotelRepository {
    suspend fun getAvailability(groupId: String, start: String, end: String, city: String? = null): List<Hotel>
    suspend fun reserve(groupId: String, request: ReserveRequest): Reservation
    suspend fun getGroupReservations(groupId: String, guestEmail: String? = null): List<Reservation>
    suspend fun cancelById(resId: String): Reservation
}
