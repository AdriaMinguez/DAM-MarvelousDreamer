package com.example.marvelousdreamer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val login: String,
    val username: String,
    val birthdate: Long? = null,
    val address: String = "",
    val country: String = "",
    val phone: String = "",
    val acceptEmails: Boolean = false
)
