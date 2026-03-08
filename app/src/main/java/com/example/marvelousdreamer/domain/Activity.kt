package com.example.marvelousdreamer.domain

import java.time.LocalTime

data class Activity(
    val id         : String,
    val title      : String,
    val description: String,
    val time       : LocalTime,
    val location   : String,
    val cost       : Double,
    val type       : ActivityType
) {
    /**
     * Returns true if this activity involves transport (flight or ground transport).
     */
    fun isTransport(): Boolean {
        return type == ActivityType.FLIGHT || type == ActivityType.TRANSPORT
    }

    /**
     * Future feature: format the time using the user's preferred locale.
     */
    fun getFormattedTime(): String {
        // @TODO Format using UserPreferences.language
        return time.toString()
    }

    /**
     * Future feature: fetch live price from an external API.
     */
    fun refreshPrice(): Double {
        // @TODO Call external pricing API
        return cost
    }

    /**
     * Future feature: add this activity to the Android calendar.
     */
    fun addToCalendar() {
        // @TODO Integrate with Android Calendar provider
    }
}
