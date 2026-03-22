package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.data.preferences.UserPreferencesManager
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import java.time.format.DateTimeFormatter

/**
 * Home screen — Sprint 02: now reads trips from TripViewModel (T2.3).
 * Updates dynamically when trips are added/edited/deleted.
 */
@Composable
fun HomeScreen(
    viewModel      : TripViewModel,
    onTripClick    : (String) -> Unit,
    onAddTripClick : () -> Unit = {},
    onSeeAllClick  : () -> Unit = {},
    onProfileClick : () -> Unit = {},
    onTermsClick   : () -> Unit = {},
    onAboutClick   : () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val c = AppTheme.colors
    val trips by viewModel.trips.collectAsState()

    val context = LocalContext.current
    val prefsManager = remember { UserPreferencesManager(context) }
    val defaultName = stringResource(R.string.user_name)
    val userName = prefsManager.username.ifEmpty { defaultName }
    val greeting        = stringResource(R.string.home_greeting)
    val sectionUpcoming = stringResource(R.string.home_section_upcoming)
    val seeAll          = stringResource(R.string.home_section_see_all)
    val statTrips       = stringResource(R.string.home_stat_trips)
    val statBudget      = stringResource(R.string.home_stat_budget)
    val statNights      = stringResource(R.string.home_stat_nights)

    val totalBudget = trips.sumOf { it.budget.toInt() }
    val totalNights = trips.sumOf { it.getDurationInDays() }

    Scaffold(
        containerColor = c.bgBase,
        floatingActionButton = {
            FloatingActionButton(
                onClick        = onAddTripClick,
                shape          = RoundedCornerShape(16.dp),
                containerColor = c.emerald,
                contentColor   = c.bgBase,
                elevation      = FloatingActionButtonDefaults.elevation(0.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add trip")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                HomeHeader(
                    greeting        = greeting,
                    userName        = userName,
                    onProfileClick  = onProfileClick,
                    onTermsClick    = onTermsClick,
                    onAboutClick    = onAboutClick,
                    onSettingsClick = onSettingsClick
                )
            }
            item {
                Spacer(Modifier.height(4.dp))
                HomeStatsRow(trips.size, totalBudget, totalNights, statTrips, statBudget, statNights)
                Spacer(Modifier.height(28.dp))
            }
            item {
                Row(
                    modifier              = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(sectionUpcoming, style = MaterialTheme.typography.titleLarge,
                        color = c.snow, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onSeeAllClick, contentPadding = PaddingValues(0.dp)) {
                        Text(seeAll, style = MaterialTheme.typography.bodyMedium, color = c.emeraldLight, fontWeight = FontWeight.SemiBold)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }
            items(trips.size) { index ->
                val trip = trips[index]
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    HomeTripCard(trip = trip, onClick = { onTripClick(trip.id) })
                    if (index < trips.lastIndex) Spacer(Modifier.height(12.dp))
                }
            }
            if (trips.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(16.dp)).background(c.cardSurface).padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No trips yet. Tap + to create one!",
                            style = MaterialTheme.typography.bodyMedium, color = c.fog)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    greeting       : String,
    userName       : String,
    onProfileClick : () -> Unit = {},
    onTermsClick   : () -> Unit,
    onAboutClick   : () -> Unit,
    onSettingsClick: () -> Unit
) {
    val c = AppTheme.colors
    var menuExpanded by remember { mutableStateOf(false) }
    val userEmail = stringResource(R.string.user_email)

    Box(
        modifier = Modifier.fillMaxWidth().background(
            Brush.verticalGradient(listOf(c.violet.copy(alpha = 0.25f), c.bgBase.copy(alpha = 0f)))
        )
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 52.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Top
        ) {
            Column {
                Text(greeting, style = MaterialTheme.typography.bodyMedium, color = c.mist)
                Spacer(Modifier.height(2.dp))
                Text(userName, style = MaterialTheme.typography.headlineLarge,
                    color = c.snow, fontWeight = FontWeight.ExtraBold)
            }

            Box {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(c.violet, c.emerald)))
                        .border(1.5.dp, c.bgOutline, CircleShape)
                        .clickable { menuExpanded = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(userName.first().uppercase(), color = c.snow,
                        fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                DropdownMenu(
                    expanded         = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset           = DpOffset(x = 16.dp, y = (-71).dp),
                    containerColor   = c.bgElevated
                ) {
                    Row(
                        modifier          = Modifier
                            .width(220.dp)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(userName, style = MaterialTheme.typography.titleMedium,
                                color = c.snow, fontWeight = FontWeight.Bold)
                            Text(userEmail, style = MaterialTheme.typography.bodySmall,
                                color = c.fog, maxLines = 1)
                        }
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(c.violet, c.emerald)))
                                .border(1.5.dp, c.bgOutline, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(userName.first().uppercase(), color = c.snow,
                                fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                    ProfileMenuItem(emoji = "👤", label = "Profile",            onClick = { menuExpanded = false; onProfileClick() })
                    ProfileMenuItem(emoji = "⚙️", label = "Settings",           onClick = { menuExpanded = false; onSettingsClick() })
                    ProfileMenuItem(emoji = "ℹ️",  label = "About",              onClick = { menuExpanded = false; onAboutClick() })
                    HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                    ProfileMenuItem(emoji = "📄", label = "Terms & Conditions", onClick = { menuExpanded = false; onTermsClick() })
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(emoji: String, label: String, onClick: () -> Unit) {
    val c = AppTheme.colors
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 16.sp)
                Spacer(Modifier.width(10.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium, color = c.snow)
            }
        },
        onClick  = onClick,
        colors   = MenuDefaults.itemColors(textColor = c.snow),
        modifier = Modifier.width(220.dp)
    )
}

@Composable
private fun HomeStatsRow(
    tripCount: Int, totalBudget: Int, totalNights: Int,
    labelTrips: String, labelBudget: String, labelNights: String
) {
    val c = AppTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(c.cardSurface)
            .border(1.dp, c.bgOutline, RoundedCornerShape(20.dp))
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        HomeStatChip("$tripCount",    labelTrips)
        Box(Modifier.width(1.dp).height(36.dp).background(c.bgOutline))
        HomeStatChip("€$totalBudget", labelBudget)
        Box(Modifier.width(1.dp).height(36.dp).background(c.bgOutline))
        HomeStatChip("$totalNights",  labelNights)
    }
}

@Composable
private fun HomeStatChip(value: String, label: String) {
    val c = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = c.snow, fontWeight = FontWeight.ExtraBold)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = c.fog)
    }
}

@Composable
private fun HomeTripCard(trip: Trip, onClick: () -> Unit) {
    val c = AppTheme.colors
    val dateFmt = DateTimeFormatter.ofPattern("dd/MM")
    val dateRange = "${trip.startDate.format(dateFmt)} – ${trip.endDate.format(dateFmt)}"
    val nights = trip.getDurationInDays()
    val spent = trip.activities.sumOf { it.cost }
    val progress = if (trip.budget > 0) (spent / trip.budget).toFloat().coerceIn(0f, 1f) else 0f

    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(c.cardSurface)
            .border(1.dp, c.bgOutline, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.width(3.dp).fillMaxHeight()
                .background(Brush.verticalGradient(listOf(c.violet, c.emerald)))
        )
        Row(
            modifier          = Modifier.padding(start = 19.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(68.dp).clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(c.gradStart, c.gradMid, c.gradEnd))),
                contentAlignment = Alignment.Center
            ) {
                Text("✈️", fontSize = 28.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(trip.title, style = MaterialTheme.typography.titleMedium,
                    color = c.snow, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(3.dp))
                Text("📅 $dateRange · $nights nights",
                    style = MaterialTheme.typography.bodySmall, color = c.fog)
                Spacer(Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("€${trip.budget.toInt()}", style = MaterialTheme.typography.labelLarge,
                        color = c.violetLight, fontWeight = FontWeight.Bold)
                    Text("${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall, color = c.fog)
                }
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(3.dp)
                        .clip(RoundedCornerShape(2.dp)).background(c.bgOutline)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(progress).fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(c.violet, c.emerald)))
                    )
                }
            }
        }
    }
}
