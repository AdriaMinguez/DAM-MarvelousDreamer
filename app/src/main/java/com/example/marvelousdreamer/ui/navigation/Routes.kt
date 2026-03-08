package com.example.marvelousdreamer.ui.navigation

object Routes {
    const val SPLASH       = "splash"
    const val HOME         = "home"
    const val TRIP_DETAIL  = "trip_detail/{tripId}"
    const val TRIP_GALLERY = "trip_gallery/{tripId}"
    const val PREFERENCES  = "preferences"
    const val ABOUT        = "about"
    const val TERMS        = "terms"
    const val TRIPS_LIST    = "trips_list"
    const val PROFILE       = "profile"
    const val GALLERY_ALL   = "gallery_all"

    fun tripDetail(tripId: String)  = "trip_detail/$tripId"
    fun tripGallery(tripId: String) = "trip_gallery/$tripId"
}
