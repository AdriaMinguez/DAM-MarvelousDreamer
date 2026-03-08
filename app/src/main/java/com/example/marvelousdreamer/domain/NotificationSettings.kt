package com.example.marvelousdreamer.domain

data class NotificationSettings(
    val travelReminders: Boolean = true,
    val weeklySummary  : Boolean = true,
    val aiSuggestions  : Boolean = false
)
