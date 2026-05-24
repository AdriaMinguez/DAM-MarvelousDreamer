package com.example.marvelousdreamer.data.repository

import android.net.Uri
import android.util.Log
import com.example.marvelousdreamer.data.local.dao.ImageDao
import com.example.marvelousdreamer.data.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GalleryRepository @Inject constructor(
    private val imageDao: ImageDao
) {
    companion object { private const val TAG = "GalleryRepository" }

    fun getImages(tripId: String): Flow<List<Uri>> =
        imageDao.getImagesByTrip(tripId).map { list -> list.map { Uri.parse(it.uri) } }

    suspend fun addImage(tripId: String, uri: Uri) {
        imageDao.insert(ImageEntity(tripId = tripId, uri = uri.toString()))
        Log.i(TAG, "addImage: $uri to trip $tripId")
    }

    suspend fun deleteImage(uri: Uri) {
        imageDao.deleteByUri(uri.toString())
        try { File(uri.path!!).delete() } catch (_: Exception) {}
        Log.i(TAG, "deleteImage: $uri")
    }
}
