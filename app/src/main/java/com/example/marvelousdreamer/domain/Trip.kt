package com.example.marvelousdreamer.domain

import java.time.LocalDate

data class Trip(
    val id          : String,
    val title       : String,
    val destination : String,
    val startDate   : LocalDate,
    val endDate     : LocalDate,
    val budget      : Double,
    val itinerary   : List<ItineraryDay> = emptyList(),
    val gallery     : List<GalleryItem>  = emptyList(),
    val notes       : String             = ""
) {
    /**
     * Returns the number of days between start and end date.
     */
    fun getDurationInDays(): Int {
        return (endDate.toEpochDay() - startDate.toEpochDay()).toInt()
    }

    /**
     * Returns the budget minus the sum of all activity costs.
     */
    fun getRemainingBudget(): Double {
        val totalActivityCost = itinerary.sumOf { it.getDailyCost() }
        return budget - totalActivityCost
    }

    /**
     * Returns a new Trip with the given activity added to the specified day index.
     */
    fun addActivity(day: Int, activity: Activity): Trip {
        val updatedItinerary = itinerary.mapIndexed { index, itineraryDay ->
            if (index == day) itineraryDay.addActivity(activity) else itineraryDay
        }
        return copy(itinerary = updatedItinerary)
    }

    /**
     * Returns a new Trip with the given gallery item appended.
     */
    fun addGalleryItem(item: GalleryItem): Trip {
        return copy(gallery = gallery + item)
    }

    /**
     * Returns a new Trip with the gallery item matching [itemId] removed.
     */
    fun removeGalleryItem(itemId: String): Trip {
        return copy(gallery = gallery.filter { it.id != itemId })
    }

    /**
     * Future feature: suggest optimal daily budget distribution.
     */
    fun optimizeBudgetDistribution() {
        // @TODO Implement smart budget distribution algorithm
    }

    /**
     * Future feature: generate a shareable link or exportable PDF.
     */
    fun shareTrip(): String {
        // @TODO Implement trip sharing
        return ""
    }

    /**
     * Future feature: estimate CO₂ emissions based on transport types in itinerary.
     */
    fun getCarbonFootprint(): Double {
        // @TODO Calculate carbon footprint based on transport activities
        return 0.0
    }
}
