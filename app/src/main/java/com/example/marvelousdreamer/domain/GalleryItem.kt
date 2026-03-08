package com.example.marvelousdreamer.domain

import java.time.LocalDate

data class GalleryItem(
    val id       : String,
    val url      : String,
    val type     : MediaType,
    val isTop    : Boolean   = false,
    val sizeMB   : Double    = 0.0,
    val dateAdded: LocalDate = LocalDate.now()
) {
    /**
     * Returns a new GalleryItem with isTop toggled.
     */
    fun toggleTop(): GalleryItem {
        return copy(isTop = !isTop)
    }

    /**
     * Future feature: return a resized thumbnail URL for list views.
     */
    fun getThumbnailUrl(): String {
        // @TODO Append resize params for cloud storage CDN
        return url
    }

    /**
     * Future feature: upload image to Firebase Storage and return public URL.
     */
    fun upload(localPath: String): String {
        // @TODO Upload to Firebase Storage
        return ""
    }

    /**
     * Future feature: delete image from cloud storage and database.
     */
    fun delete() {
        // @TODO Remove from cloud storage and database
    }

    /**
     * Future feature: use Vision AI to detect landmarks and generate tags.
     */
    fun analyzeWithAI(): Map<String, String> {
        // @TODO Call Vision API for landmark detection and tagging
        return emptyMap()
    }
}
