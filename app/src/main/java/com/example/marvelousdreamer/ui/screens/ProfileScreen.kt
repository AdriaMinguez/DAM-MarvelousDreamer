package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

// Mock trip entry — replace with domain model when data layer is ready
private data class TripEntry(val emoji: String, val title: String, val dates: String, val id: String)

private val MOCK_TRIPS = listOf(
    TripEntry("⛩️", "Kyoto Escape",   "Jun 3 – Jun 12",  "trip_kyoto"),
    TripEntry("🕌", "Moroccan Dream", "Sep 14 – Sep 20", "trip_morocco"),
    TripEntry("🌌", "Iceland Aurora", "Nov 20 – Nov 25", "trip_iceland")
)

private const val MOCK_TOTAL_NIGHTS = 9 + 6 + 5
private const val MOCK_TOTAL_BUDGET = 1560 + 980 + 1800
private const val MOCK_TOTAL_SPENT  = 720 + 410 + 0

@Composable
fun ProfileScreen(
    onBack         : () -> Unit,
    onSettingsClick: () -> Unit = {},
    onTripClick    : (String) -> Unit = {}
) {
    val userName  = stringResource(R.string.user_name)
    val userEmail = stringResource(R.string.user_email)

    Scaffold(
        containerColor = BgBase,
        topBar = { ProfileTopBar(onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding      = PaddingValues(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Avatar, name, email and explorer badge
            item {
                Spacer(Modifier.height(28.dp))
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Violet, Emerald)))
                        .border(3.dp, BgOutline, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = userName.first().uppercase(),
                        fontSize   = 38.sp,
                        color      = Snow,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    text       = userName,
                    style      = MaterialTheme.typography.headlineMedium,
                    color      = Snow,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text      = userEmail,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = Fog,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Emerald.copy(alpha = 0.15f))
                        .border(1.dp, Emerald.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text("✦ Explorer", style = MaterialTheme.typography.labelLarge,
                        color = EmeraldLight, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(28.dp))
            }

            // Summary stats: trips, nights, total budget
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSurface)
                        .border(1.dp, BgOutline, RoundedCornerShape(20.dp))
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    ProfileStatChip("${MOCK_TRIPS.size}", "TRIPS")
                    Box(Modifier.width(1.dp).height(36.dp).background(BgOutline))
                    ProfileStatChip("$MOCK_TOTAL_NIGHTS", "NIGHTS")
                    Box(Modifier.width(1.dp).height(36.dp).background(BgOutline))
                    ProfileStatChip("€$MOCK_TOTAL_BUDGET", "BUDGET")
                }
                Spacer(Modifier.height(24.dp))
            }

            // Overall spending progress across all trips
            item {
                val spendProgress = MOCK_TOTAL_SPENT.toFloat() / MOCK_TOTAL_BUDGET
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(1.dp, BgOutline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total spending", style = MaterialTheme.typography.bodyMedium, color = Fog)
                        Text("€$MOCK_TOTAL_SPENT / €$MOCK_TOTAL_BUDGET",
                            style = MaterialTheme.typography.labelLarge,
                            color = VioletLight, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(6.dp)
                            .clip(RoundedCornerShape(3.dp)).background(BgOutline)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(spendProgress).fillMaxHeight()
                                .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("${(spendProgress * 100).toInt()}% of total budget used",
                        style = MaterialTheme.typography.bodySmall, color = Fog)
                }
                Spacer(Modifier.height(24.dp))
            }

            // Trips list — each row navigates to the corresponding trip detail
            item {
                ProfileSection(title = "🗺️  MY TRIPS")
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                ) {
                    MOCK_TRIPS.forEachIndexed { index, trip ->
                        Row(
                            modifier          = Modifier
                                .fillMaxWidth()
                                .clickable { onTripClick(trip.id) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.linearGradient(listOf(GradStart, GradEnd))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(trip.emoji, fontSize = 20.sp)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(trip.title, style = MaterialTheme.typography.titleMedium, color = Snow)
                                Text(trip.dates, style = MaterialTheme.typography.bodySmall,   color = Fog)
                            }
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
                                tint = EmeraldLight, modifier = Modifier.size(20.dp))
                        }
                        if (index < MOCK_TRIPS.lastIndex) {
                            HorizontalDivider(color = BgOutline, thickness = 0.5.dp)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // Account settings actions
            item {
                ProfileSection(title = "⚙️  ACCOUNT")
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                ) {
                    ProfileActionRow(icon = Icons.Rounded.Person,        label = "Edit profile",       tint = VioletLight)
                    HorizontalDivider(color = BgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Notifications,  label = "Notifications",      tint = VioletLight, onClick = onSettingsClick)
                    HorizontalDivider(color = BgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Lock,           label = "Privacy & security", tint = VioletLight)
                    HorizontalDivider(color = BgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Close,          label = "Log out",            tint = Rose)
                }
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text("Profile", style = MaterialTheme.typography.titleLarge,
                color = Snow, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BgBase)
    )
}

// ─── Section label ────────────────────────────────────────────────────────────

@Composable
private fun ProfileSection(title: String) {
    Text(text = title, style = MaterialTheme.typography.labelLarge,
        color = VioletLight, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp))
}

// ─── Single stat chip (value + label) ────────────────────────────────────────

@Composable
private fun ProfileStatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = Snow, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Fog)
    }
}

// ─── Tappable account action row ─────────────────────────────────────────────

@Composable
private fun ProfileActionRow(
    icon   : ImageVector,
    label  : String,
    tint   : androidx.compose.ui.graphics.Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = Snow,
            modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
            tint = Fog, modifier = Modifier.size(18.dp))
    }
}