package com.example.marvelousdreamer.ui.navigation

object Routes {
    // Auth
    const val LOGIN            = "login"
    const val REGISTER         = "register"
    const val FORGOT_PASSWORD  = "forgot_password"
    const val EDIT_PROFILE     = "edit_profile"

    // Core
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

    // Trip CRUD
    const val ADD_TRIP      = "add_trip"
    const val EDIT_TRIP     = "edit_trip/{tripId}"
    const val ADD_ACTIVITY  = "add_activity/{tripId}"
    const val EDIT_ACTIVITY = "edit_activity/{tripId}/{activityId}"

    // Sprint 04 — Hotels
    const val HOTEL_SEARCH  = "hotel_search"
    const val HOTEL_DETAIL  = "hotel_detail"
    const val RESERVATIONS  = "reservations"

    fun tripDetail(tripId: String)  = "trip_detail/$tripId"
    fun tripGallery(tripId: String) = "trip_gallery/$tripId"
    fun editTrip(tripId: String)    = "edit_trip/$tripId"
    fun addActivity(tripId: String) = "add_activity/$tripId"
    fun editActivity(tripId: String, activityId: String) = "edit_activity/$tripId/$activityId"
}
