package com.example.marvelousdreamer.data.remote.dto

data class ReservationResponseDto(
    val message: String,
    val nights: Int,
    val reservation: ReservationDto
)
