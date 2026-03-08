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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

@Composable
fun HomeScreen(
    onTripClick    : (String) -> Unit,
    onSeeAllClick  : () -> Unit = {},
    onProfileClick : () -> Unit = {},
    onTermsClick   : () -> Unit = {},
    onAboutClick   : () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    val userName        = stringResource(R.string.user_name)
    val greeting        = stringResource(R.string.home_greeting)
    val sectionUpcoming = stringResource(R.string.home_section_upcoming)
    val seeAll          = stringResource(R.string.home_section_see_all)
    val statTrips       = stringResource(R.string.home_stat_trips)
    val statBudget      = stringResource(R.string.home_stat_budget)
    val statNights      = stringResource(R.string.home_stat_nights)

    val trips = listOf(
        TripUiModel(id = stringResource(R.string.trip1_id), title = stringResource(R.string.trip1_title),
            dateRange = "${stringResource(R.string.trip1_start)} – ${stringResource(R.string.trip1_end)}",
            nights = 8, budget = stringResource(R.string.trip1_budget).toInt(),
            spentBudget = 720, coverEmoji = stringResource(R.string.trip1_cover_emoji)),
        TripUiModel(id = stringResource(R.string.trip2_id), title = stringResource(R.string.trip2_title),
            dateRange = "${stringResource(R.string.trip2_start)} – ${stringResource(R.string.trip2_end)}",
            nights = 4, budget = stringResource(R.string.trip2_budget).toInt(),
            spentBudget = 410, coverEmoji = stringResource(R.string.trip2_cover_emoji)),
        TripUiModel(id = stringResource(R.string.trip3_id), title = stringResource(R.string.trip3_title),
            dateRange = "${stringResource(R.string.trip3_start)} – ${stringResource(R.string.trip3_end)}",
            nights = 3, budget = stringResource(R.string.trip3_budget).toInt(),
            spentBudget = 0, coverEmoji = stringResource(R.string.trip3_cover_emoji))
    )

    val totalBudget = trips.sumOf { it.budget }
    val totalNights = trips.sumOf { it.nights }

    Scaffold(
        containerColor = BgBase,
        floatingActionButton = {
            FloatingActionButton(
                onClick        = {},
                shape          = RoundedCornerShape(16.dp),
                containerColor = Emerald,
                contentColor   = BgBase,
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
                        color = Snow, fontWeight = FontWeight.Bold)
                    TextButton(onClick = onSeeAllClick, contentPadding = PaddingValues(0.dp)) {
                        Text(seeAll, style = MaterialTheme.typography.bodyMedium, color = EmeraldLight, fontWeight = FontWeight.SemiBold)
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
        }
    }
}

private data class TripUiModel(
    val id: String, val title: String, val dateRange: String,
    val nights: Int, val budget: Int, val spentBudget: Int, val coverEmoji: String
) {
    fun progress(): Float = if (budget > 0) (spentBudget.toFloat() / budget).coerceIn(0f, 1f) else 0f
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
    var menuExpanded by remember { mutableStateOf(false) }
    val userEmail = stringResource(R.string.user_email)

    Box(
        modifier = Modifier.fillMaxWidth().background(
            Brush.verticalGradient(listOf(Violet.copy(alpha = 0.25f), BgBase.copy(alpha = 0f)))
        )
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 52.dp, bottom = 28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Top
        ) {
            Column {
                Text(greeting, style = MaterialTheme.typography.bodyMedium, color = Mist)
                Spacer(Modifier.height(2.dp))
                Text(userName, style = MaterialTheme.typography.headlineLarge,
                    color = Snow, fontWeight = FontWeight.ExtraBold)
            }

            Box {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(Violet, Emerald)))
                        .border(1.5.dp, BgOutline, CircleShape)
                        .clickable { menuExpanded = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(userName.first().uppercase(), color = Snow,
                        fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }

                // Dropdown — alineat a la dreta, sobreposant l'avatar
                DropdownMenu(
                    expanded         = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset           = DpOffset(x = 16.dp, y = (-71).dp),
                    containerColor   = BgElevated
                ) {
                    // Capçalera: nom+email esquerra, avatar dreta
                    Row(
                        modifier          = Modifier
                            .width(220.dp)
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(userName, style = MaterialTheme.typography.titleMedium,
                                color = Snow, fontWeight = FontWeight.Bold)
                            Text(userEmail, style = MaterialTheme.typography.bodySmall,
                                color = Fog, maxLines = 1)
                        }
                        Spacer(Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(Violet, Emerald)))
                                .border(1.5.dp, BgOutline, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(userName.first().uppercase(), color = Snow,
                                fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }

                    HorizontalDivider(color = BgOutline, thickness = 0.5.dp)

                    ProfileMenuItem(emoji = "👤", label = "Profile",            onClick = { menuExpanded = false; onProfileClick() })
                    ProfileMenuItem(emoji = "⚙️", label = "Settings",           onClick = { menuExpanded = false; onSettingsClick() })
                    ProfileMenuItem(emoji = "ℹ️",  label = "About",              onClick = { menuExpanded = false; onAboutClick() })

                    HorizontalDivider(color = BgOutline, thickness = 0.5.dp)

                    ProfileMenuItem(emoji = "📄", label = "Terms & Conditions", onClick = { menuExpanded = false; onTermsClick() })
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItem(emoji: String, label: String, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 16.sp)
                Spacer(Modifier.width(10.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium, color = Snow)
            }
        },
        onClick  = onClick,
        colors   = MenuDefaults.itemColors(textColor = Snow),
        modifier = Modifier.width(220.dp)
    )
}

@Composable
private fun HomeStatsRow(
    tripCount: Int, totalBudget: Int, totalNights: Int,
    labelTrips: String, labelBudget: String, labelNights: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CardSurface)
            .border(1.dp, BgOutline, RoundedCornerShape(20.dp))
            .padding(vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        HomeStatChip("$tripCount",    labelTrips)
        Box(Modifier.width(1.dp).height(36.dp).background(BgOutline))
        HomeStatChip("€$totalBudget", labelBudget)
        Box(Modifier.width(1.dp).height(36.dp).background(BgOutline))
        HomeStatChip("$totalNights",  labelNights)
    }
}

@Composable
private fun HomeStatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = Snow, fontWeight = FontWeight.ExtraBold)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, color = Fog)
    }
}

@Composable
private fun HomeTripCard(trip: TripUiModel, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(CardSurface)
            .border(1.dp, BgOutline, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.width(3.dp).fillMaxHeight()
                .background(Brush.verticalGradient(listOf(Violet, Emerald)))
        )
        Row(
            modifier          = Modifier.padding(start = 19.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(68.dp).clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(listOf(GradStart, GradMid, GradEnd))),
                contentAlignment = Alignment.Center
            ) {
                Text(trip.coverEmoji, fontSize = 28.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(trip.title, style = MaterialTheme.typography.titleMedium,
                    color = Snow, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(3.dp))
                Text("📅 ${trip.dateRange} · ${trip.nights} nights",
                    style = MaterialTheme.typography.bodySmall, color = Fog)
                Spacer(Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("€${trip.budget}", style = MaterialTheme.typography.labelLarge,
                        color = VioletLight, fontWeight = FontWeight.Bold)
                    Text("${(trip.progress() * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall, color = Fog)
                }
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().height(3.dp)
                        .clip(RoundedCornerShape(2.dp)).background(BgOutline)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(trip.progress()).fillMaxHeight()
                            .background(Brush.horizontalGradient(listOf(Violet, Emerald)))
                    )
                }
            }
        }
    }
}