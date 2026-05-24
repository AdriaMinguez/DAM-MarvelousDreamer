package com.example.marvelousdreamer.ui.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelousdreamer.data.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val galleryRepository: GalleryRepository
) : ViewModel() {

    companion object { private const val TAG = "GalleryViewModel" }

    private val _tripId = MutableStateFlow("")

    val images: StateFlow<List<Uri>> = _tripId
        .flatMapLatest { id ->
            if (id.isEmpty()) flowOf(emptyList())
            else galleryRepository.getImages(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setTripId(tripId: String) { _tripId.value = tripId }

    fun addImage(tripId: String, uri: Uri) {
        viewModelScope.launch {
            galleryRepository.addImage(tripId, uri)
            Log.i(TAG, "addImage: $uri to trip $tripId")
        }
    }

    fun deleteImage(uri: Uri) {
        viewModelScope.launch {
            galleryRepository.deleteImage(uri)
            Log.i(TAG, "deleteImage: $uri")
        }
    }
}
