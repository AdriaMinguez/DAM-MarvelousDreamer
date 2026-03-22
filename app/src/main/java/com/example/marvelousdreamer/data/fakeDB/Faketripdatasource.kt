package com.example.marvelousdreamer.data.fakeDB

import android.util.Log
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import java.time.LocalDate
import java.time.LocalTime

/**
 * InMemory data source for trips.
 * Data lives only while the app is running — will be replaced by Room in Sprint 03.
 */
object FakeTripDataSource {

    private const val TAG = "FakeTripDataSource"

    // Preloaded fake dataset for development and testing
    private val trips = mutableListOf(
        Trip(
            id          = "trip_kyoto",
            title       = "Kyoto Escape ⛩️",
            description = "Una setmana descobrint els temples i jardins de Kyoto.",
            destination = "Kyoto, Japan",
            startDate   = LocalDate.of(2026, 4, 10),
            endDate     = LocalDate.of(2026, 4, 17),
            budget      = 1500.0,
            activities  = mutableListOf(
                Activity(
                    id          = "act_k1",
                    title       = "Vol Barcelona → Osaka",
                    description = "Vueling VY7182, Terminal 1",
                    date        = LocalDate.of(2026, 4, 10),
                    time        = LocalTime.of(8, 0),
                    location    = "Aeroport El Prat",
                    cost        = 420.0,
                    type        = ActivityType.FLIGHT
                ),
                Activity(
                    id          = "act_k2",
                    title       = "Senso-ji Temple",
                    description = "Visita al temple budista més famós de Kyoto",
                    date        = LocalDate.of(2026, 4, 11),
                    time        = LocalTime.of(9, 0),
                    location    = "Asakusa, Kyoto",
                    cost        = 0.0,
                    type        = ActivityType.VISIT
                ),
                Activity(
                    id          = "act_k3",
                    title       = "Ramen Ippudo",
                    description = "Reserva confirmada",
                    date        = LocalDate.of(2026, 4, 11),
                    time        = LocalTime.of(13, 0),
                    location    = "Shibuya, Kyoto",
                    cost        = 18.0,
                    type        = ActivityType.FOOD
                )
            )
        ),
        Trip(
            id          = "trip_morocco",
            title       = "Moroccan Dream 🕌",
            description = "Explorant els zocos i deserts del Marroc.",
            destination = "Marrakech, Morocco",
            startDate   = LocalDate.of(2026, 5, 1),
            endDate     = LocalDate.of(2026, 5, 8),
            budget      = 900.0,
            activities  = emptyList()
        ),
        Trip(
            id          = "trip_iceland",
            title       = "Iceland Aurora 🌌",
            description = "Aurores boreals i paisatges d'un altre món.",
            destination = "Reykjavik, Iceland",
            startDate   = LocalDate.of(2026, 12, 26),
            endDate     = LocalDate.of(2027, 1, 2),
            budget      = 2200.0,
            activities  = emptyList()
        )
    )

    fun getTrips(): List<Trip> {
        Log.d(TAG, "getTrips: returning ${trips.size} trips")
        return trips.toList()
    }

    fun getTripById(id: String): Trip? =
        trips.find { it.id == id }

    fun addTrip(trip: Trip) {
        trips.add(trip)
        Log.i(TAG, "addTrip: '${trip.title}' added (id=${trip.id})")
    }

    fun updateTrip(trip: Trip) {
        val index = trips.indexOfFirst { it.id == trip.id }
        if (index != -1) {
            trips[index] = trip
            Log.i(TAG, "updateTrip: '${trip.title}' updated")
        } else {
            Log.e(TAG, "updateTrip: trip ${trip.id} not found")
        }
    }

    fun deleteTrip(id: String) {
        val removed = trips.removeIf { it.id == id }
        if (removed) Log.i(TAG, "deleteTrip: $id removed")
        else         Log.e(TAG, "deleteTrip: $id not found")
    }

    /** Clears all trips — used in unit tests to start with a clean state. */
    fun clearAll() {
        trips.clear()
        Log.d(TAG, "clearAll: all trips removed")
    }
}