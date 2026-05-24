package com.example.marvelousdreamer.domain.model

data class Room(
    val id: String,
    val roomType: String,
    val price: Float,
    val images: List<String> = emptyList()
)
