package com.example.marvelousdreamer.domain

import java.time.LocalDate

data class ItineraryDay(
    val dayNumber : Int,
    val date      : LocalDate,
    val activities: List<Activity> = emptyList()
) {
    /**
     * Returns a new ItineraryDay with the given activity appended.
     */
    fun addActivity(activity: Activity): ItineraryDay {
        return copy(activities = activities + activity)
    }

    /**
     * Returns a new ItineraryDay with the activity matching [activityId] removed.
     */
    fun removeActivity(activityId: String): ItineraryDay {
        return copy(activities = activities.filter { it.id != activityId })
    }

    /**
     * Returns the sum of all activity costs for this day.
     */
    fun getDailyCost(): Double {
        return activities.sumOf { it.cost }
    }
}
