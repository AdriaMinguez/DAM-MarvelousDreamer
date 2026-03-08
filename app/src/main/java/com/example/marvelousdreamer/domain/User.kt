package com.example.marvelousdreamer.domain

data class User(
    val id            : String,
    val name          : String,
    val email         : String,
    val avatarUrl     : String,
    val trips         : List<Trip>          = emptyList(),
    val preferences   : UserPreferences     = UserPreferences(),
    val authentication: UserAuthentication  = UserAuthentication(userId = "")
) {
    /**
     * Returns all trips sorted by start date ascending.
     */
    fun getUpcomingTrips(): List<Trip> {
        return trips.sortedBy { it.startDate }
    }

    /**
     * Returns a new User with the given trip appended.
     */
    fun addTrip(trip: Trip): User {
        return copy(trips = trips + trip)
    }

    /**
     * Returns a new User with the trip matching [tripId] removed.
     */
    fun removeTrip(tripId: String): User {
        return copy(trips = trips.filter { it.id != tripId })
    }

    /**
     * Returns a new User with an updated avatar URL.
     */
    fun updateAvatar(url: String): User {
        return copy(avatarUrl = url)
    }

    /**
     * Future feature: aggregate travel statistics (km, countries, total spent).
     */
    fun getTravelStats(): Map<String, Int> {
        // @TODO Implement travel stats aggregation
        return emptyMap()
    }

    /**
     * Future feature: export full user data as JSON or PDF.
     */
    fun exportData(): String {
        // @TODO Implement data export
        return ""
    }

    /**
     * Future feature: delete account and cascade to all owned trips.
     */
    fun deleteAccount() {
        // @TODO Implement account deletion with cascade
    }
}
