package com.example.marvelousdreamer.domain

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

/**
 * A single activity or event within a trip.
 * Sprint 02: added date (LocalDate) as required by T1.2.
 */
data class Activity(
    val id         : String       = UUID.randomUUID().toString(),
    val title      : String,
    val description: String       = "",
    val date       : LocalDate,
    val time       : LocalTime,
    val location   : String       = "",
    val cost       : Double       = 0.0,
    val type       : ActivityType = ActivityType.OTHER
) {
    /** Returns true if this activity involves transport. */
    fun isTransport(): Boolean =
        type == ActivityType.FLIGHT || type == ActivityType.TRANSPORT

    /** Future feature: format the time using the user's preferred locale. */
    fun getFormattedTime(): String {
        // @TODO Format using UserPreferences.language
        return time.toString()
    }

    /** Future feature: fetch live price from an external API. */
    fun refreshPrice(): Double {
        // @TODO Call external pricing API
        return cost
    }

    /** Future feature: add this activity to the Android calendar. */
    fun addToCalendar() {
        // @TODO Integrate with Android Calendar provider
    }
}