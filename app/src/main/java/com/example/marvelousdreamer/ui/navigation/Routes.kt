package com.example.marvelousdreamer.ui.navigation

object Routes {
    // Sprint 01
    const val SPLASH       = "splash"
    const val HOME         = "home"
    const val TRIP_DETAIL  = "trip_detail/{tripId}"
    const val TRIP_GALLERY = "trip_gallery/{tripId}"
    const val PREFERENCES  = "preferences"
    const val ABOUT        = "about"
    const val TERMS        = "terms"
    const val TRIPS_LIST   = "trips_list"
    const val PROFILE      = "profile"
    const val GALLERY_ALL  = "gallery_all"

    // Sprint 02 — CRUD screens
    const val ADD_TRIP          = "add_trip"
    const val EDIT_TRIP         = "edit_trip/{tripId}"
    const val ADD_ACTIVITY      = "add_activity/{tripId}"
    const val EDIT_ACTIVITY     = "edit_activity/{tripId}/{activityId}"

    // Sprint 01 helpers
    fun tripDetail(tripId: String)  = "trip_detail/$tripId"
    fun tripGallery(tripId: String) = "trip_gallery/$tripId"

    // Sprint 02 helpers
    fun editTrip(tripId: String)                              = "edit_trip/$tripId"
    fun addActivity(tripId: String)                           = "add_activity/$tripId"
    fun editActivity(tripId: String, activityId: String)      = "edit_activity/$tripId/$activityId"
}