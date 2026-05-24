package com.example.marvelousdreamer.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.GalleryViewModel
import com.example.marvelousdreamer.utils.copyUriInternal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGalleryScreen(
    tripId: String,
    galleryViewModel: GalleryViewModel,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val context = LocalContext.current
    val images by galleryViewModel.images.collectAsState()

    LaunchedEffect(tripId) { galleryViewModel.setTripId(tripId) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && tripId.isNotEmpty()) {
            val localUri = copyUriInternal(context, uri)
            galleryViewModel.addImage(tripId, localUri)
        }
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("Gallery", color = c.snow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        },
        floatingActionButton = {
            if (tripId.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { imagePicker.launch("image/*") },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = c.emerald,
                    contentColor = c.snow
                ) {
                    Icon(Icons.Rounded.Add, "Add photo")
                }
            }
        }
    ) { padding ->
        if (images.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No photos yet. Tap + to add one!",
                    style = MaterialTheme.typography.bodyMedium, color = c.fog)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(padding).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(images) { uri ->
                    Box(Modifier.aspectRatio(1f).clip(RoundedCornerShape(8.dp))) {
                        AsyncImage(
                            model = uri, contentDescription = "Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { galleryViewModel.deleteImage(uri) },
                            modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
                                .background(c.bgBase.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        ) {
                            Icon(Icons.Rounded.Delete, "Delete", tint = c.rose, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
