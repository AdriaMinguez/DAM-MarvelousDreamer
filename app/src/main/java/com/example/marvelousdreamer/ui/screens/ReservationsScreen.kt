package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marvelousdreamer.data.local.entity.ReservationEntity
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    hotelViewModel: HotelViewModel,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val reservations by hotelViewModel.reservations.collectAsState()
    val base = "http://15.224.84.148:8090"

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("My Reservations", color = c.snow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { padding ->
        if (reservations.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No reservations yet", style = MaterialTheme.typography.bodyMedium, color = c.fog)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(reservations, key = { it.id }) { res ->
                    ReservationCard(res = res, base = base, onDelete = {
                        hotelViewModel.cancelReservation(res.id)
                    })
                }
            }
        }
    }
}

@Composable
private fun ReservationCard(res: ReservationEntity, base: String, onDelete: () -> Unit) {
    val c = AppTheme.colors
    val imgUrl = if (res.hotelImageUrl.startsWith("http")) res.hotelImageUrl else "$base${res.hotelImageUrl}"
    val roomImgs = res.roomImages.split(",").filter { it.isNotBlank() }

    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(c.cardSurface)
    ) {
        AsyncImage(
            model = imgUrl, contentDescription = res.hotelName,
            modifier = Modifier.fillMaxWidth().height(140.dp),
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(res.hotelName, style = MaterialTheme.typography.titleMedium, color = c.snow, fontWeight = FontWeight.Bold)
                    Text(res.hotelAddress, style = MaterialTheme.typography.bodySmall, color = c.fog)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Rounded.Delete, "Delete", tint = c.rose)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("${res.roomType} · €${res.price}/night", style = MaterialTheme.typography.bodyMedium, color = c.violetLight)
            Text("${res.startDate} → ${res.endDate}", style = MaterialTheme.typography.bodySmall, color = c.fog)
            Text("Guest: ${res.guestName} (${res.guestEmail})", style = MaterialTheme.typography.bodySmall, color = c.fog)
            Text("Reservation: ${res.id}", style = MaterialTheme.typography.labelSmall, color = c.fog)

            if (roomImgs.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(roomImgs) { img ->
                        val url = if (img.startsWith("http")) img else "$base$img"
                        AsyncImage(
                            model = url, contentDescription = "Room",
                            modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}
