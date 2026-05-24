package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.marvelousdreamer.domain.model.Hotel
import com.example.marvelousdreamer.domain.model.Room
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.HotelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelDetailScreen(
    hotelViewModel: HotelViewModel,
    startDate: String,
    endDate: String,
    guestName: String,
    guestEmail: String,
    userId: String,
    onBack: () -> Unit,
    onBooked: () -> Unit
) {
    val c = AppTheme.colors
    val hotel by hotelViewModel.selectedHotel.collectAsState()
    val bookingResult by hotelViewModel.bookingResult.collectAsState()
    val base = "http://15.224.84.148:8090"

    LaunchedEffect(bookingResult) {
        if (bookingResult?.startsWith("Booked") == true) {
            onBooked()
            hotelViewModel.clearBookingResult()
        }
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text(hotel?.name ?: "Hotel", color = c.snow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { padding ->
        if (hotel == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hotel selected", color = c.fog)
            }
        } else {
            val h = hotel!!
            val imgUrl = if (h.imageUrl.startsWith("http")) h.imageUrl else "$base${h.imageUrl}"

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    AsyncImage(
                        model = imgUrl, contentDescription = h.name,
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Column(Modifier.padding(24.dp)) {
                        Text(h.name, style = MaterialTheme.typography.headlineSmall, color = c.snow, fontWeight = FontWeight.Bold)
                        Text(h.address, style = MaterialTheme.typography.bodyMedium, color = c.fog)
                        Row { repeat(h.rating) { Text("⭐", fontSize = 14.sp) } }
                        Spacer(Modifier.height(4.dp))
                        Text("$startDate → $endDate", style = MaterialTheme.typography.bodySmall, color = c.violetLight)
                    }
                }

                if (bookingResult != null) {
                    item {
                        val isError = bookingResult!!.startsWith("Error")
                        Text(
                            bookingResult!!,
                            color = if (isError) c.rose else c.emerald,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }

                item {
                    Text("Available Rooms", style = MaterialTheme.typography.titleMedium,
                        color = c.snow, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 24.dp))
                    Spacer(Modifier.height(12.dp))
                }

                items(h.rooms) { room ->
                    RoomCard(
                        room = room, base = base,
                        onBook = {
                            hotelViewModel.bookRoom(h, room.id, startDate, endDate, guestName, guestEmail, userId)
                        }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun RoomCard(room: Room, base: String, onBook: () -> Unit) {
    val c = AppTheme.colors

    Column(
        Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)).background(c.cardSurface).padding(16.dp)
    ) {
        Text(room.roomType, style = MaterialTheme.typography.titleMedium, color = c.snow, fontWeight = FontWeight.Bold)
        Text("€${room.price}/night", style = MaterialTheme.typography.titleLarge, color = c.emeraldLight, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        if (room.images.isNotEmpty()) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(room.images) { img ->
                    val url = if (img.startsWith("http")) img else "$base$img"
                    AsyncImage(
                        model = url, contentDescription = "Room image",
                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = onBook,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = c.violet)
        ) {
            Text("Book this room", fontWeight = FontWeight.Bold)
        }
    }
}
