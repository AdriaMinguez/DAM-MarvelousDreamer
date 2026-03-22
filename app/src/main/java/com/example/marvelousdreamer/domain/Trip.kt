package com.example.marvelousdreamer.domain

import java.time.LocalDate
import java.util.UUID

/**
 * Core domain entity representing a travel trip.
 * Sprint 02: added description + flat activities list for CRUD.
 */
data class Trip(
    val id         : String         = UUID.randomUUID().toString(),
    val title      : String,
    val description: String         = "",
    val destination: String         = "",
    val startDate  : LocalDate,
    val endDate    : LocalDate,
    val budget     : Double         = 0.0,
    val activities : List<Activity> = emptyList(),
    val notes      : String         = ""
) {
    /** Returns the number of days between start and end date. */
    fun getDurationInDays(): Int =
        (endDate.toEpochDay() - startDate.toEpochDay()).toInt()

    /** Returns budget minus the sum of all activity costs. */
    fun getRemainingBudget(): Double =
        budget - activities.sumOf { it.cost }

    fun isDateRangeValid(): Boolean =
        startDate.toEpochDay() < endDate.toEpochDay()

    fun isActivityDateValid(activity: Activity): Boolean =
        activity.date.toEpochDay() >= startDate.toEpochDay() &&
                activity.date.toEpochDay() <= endDate.toEpochDay()

    /** Returns a new Trip with the given activity appended. */
    fun addActivity(activity: Activity): Trip =
        copy(activities = activities + activity)

    /** Returns a new Trip with the matching activity replaced. */
    fun updateActivity(activity: Activity): Trip =
        copy(activities = activities.map { if (it.id == activity.id) activity else it })

    /** Returns a new Trip with the activity matching [activityId] removed. */
    fun removeActivity(activityId: String): Trip =
        copy(activities = activities.filter { it.id != activityId })

    /** Future feature: suggest optimal daily budget distribution. */
    fun optimizeBudgetDistribution() {
        // @TODO Implement smart budget distribution algorithm
    }

    /** Future feature: generate a shareable link or exportable PDF. */
    fun shareTrip(): String {
        // @TODO Implement trip sharing
        return ""
    }

    /** Future feature: estimate CO₂ based on transport activities. */
    fun getCarbonFootprint(): Double {
        // @TODO Calculate carbon footprint
        return 0.0
    }
}