package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.HotelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelSearchScreen(
    hotelViewModel: HotelViewModel,
    onBack: () -> Unit,
    onHotelClick: (Hotel) -> Unit
) {
    val c = AppTheme.colors
    val searchState by hotelViewModel.searchState.collectAsState()
    val cities = listOf("London", "Paris", "Barcelona")
    var selectedCity by remember { mutableStateOf(cities[0]) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }
    val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val apiFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    if (showStartPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        startDate = java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    }
                    showStartPicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = { TextButton(onClick = { showStartPicker = false }) { Text("Cancel", color = c.fog) } }
        ) { DatePicker(state = state) }
    }
    if (showEndPicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        endDate = java.time.Instant.ofEpochMilli(it)
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    }
                    showEndPicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = { TextButton(onClick = { showEndPicker = false }) { Text("Cancel", color = c.fog) } }
        ) { DatePicker(state = state) }
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("Search Hotels", color = c.snow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // City selector
            item {
                Text("City", style = MaterialTheme.typography.labelLarge, color = c.mist)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    cities.forEach { city ->
                        FilterChip(
                            selected = selectedCity == city,
                            onClick = { selectedCity = city },
                            label = { Text(city) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = c.violet,
                                selectedLabelColor = c.snow
                            )
                        )
                    }
                }
            }

            // Date pickers
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f)) {
                        Text("Check-in", style = MaterialTheme.typography.labelLarge, color = c.mist)
                        Box(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .background(c.cardSurface).clickable { showStartPicker = true }
                                .padding(16.dp)
                        ) {
                            Text(startDate?.format(dateFmt) ?: "Select date",
                                color = if (startDate != null) c.snow else c.fog)
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Check-out", style = MaterialTheme.typography.labelLarge, color = c.mist)
                        Box(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                .background(c.cardSurface).clickable { showEndPicker = true }
                                .padding(16.dp)
                        ) {
                            Text(endDate?.format(dateFmt) ?: "Select date",
                                color = if (endDate != null) c.snow else c.fog)
                        }
                    }
                }
            }

            // Search button
            item {
                Button(
                    onClick = {
                        if (startDate != null && endDate != null) {
                            hotelViewModel.searchHotels(
                                selectedCity,
                                startDate!!.format(apiFmt),
                                endDate!!.format(apiFmt)
                            )
                        }
                    },
                    enabled = startDate != null && endDate != null && !searchState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = c.emerald)
                ) {
                    if (searchState.isLoading) CircularProgressIndicator(Modifier.size(20.dp), color = c.snow)
                    else Text("Search Hotels", fontWeight = FontWeight.Bold)
                }
            }

            if (searchState.error != null) {
                item {
                    Text(searchState.error!!, color = c.rose, style = MaterialTheme.typography.bodySmall)
                }
            }

            // Results
            if (searchState.hotels.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("${searchState.hotels.size} hotels found",
                        style = MaterialTheme.typography.titleMedium, color = c.snow, fontWeight = FontWeight.Bold)
                }
                items(searchState.hotels) { hotel ->
                    HotelCard(hotel = hotel, baseUrl = "http://15.224.84.148:8090", onClick = {
                        hotelViewModel.selectHotel(hotel)
                        onHotelClick(hotel)
                    })
                }
            }
        }
    }
}

@Composable
private fun HotelCard(hotel: Hotel, baseUrl: String, onClick: () -> Unit) {
    val c = AppTheme.colors
    val imgUrl = if (hotel.imageUrl.startsWith("http")) hotel.imageUrl else "$baseUrl${hotel.imageUrl}"

    Box(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(c.cardSurface).clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = imgUrl, contentDescription = hotel.name,
                modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(hotel.name, style = MaterialTheme.typography.titleMedium, color = c.snow, fontWeight = FontWeight.Bold)
                Text(hotel.address, style = MaterialTheme.typography.bodySmall, color = c.fog)
                Spacer(Modifier.height(4.dp))
                Row {
                    repeat(hotel.rating) { Text("⭐", fontSize = 12.sp) }
                    Spacer(Modifier.width(8.dp))
                    Text("${hotel.rooms.size} rooms", style = MaterialTheme.typography.labelSmall, color = c.violetLight)
                }
            }
        }
    }
}
