package com.example.marvelousdreamer.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

private val ALL_TRIP_IDS = listOf("trip_kyoto", "trip_morocco", "trip_iceland")

@Composable
fun TripDetailScreen(
    tripId        : String,
    onBack        : () -> Unit,
    onGalleryClick: (String) -> Unit,
    onTripChanged : (String) -> Unit = {}
) {
    var currentIndex   by remember { mutableIntStateOf(ALL_TRIP_IDS.indexOf(tripId).coerceAtLeast(0)) }
    var slideDirection by remember { mutableIntStateOf(1) }
    val currentTripId = ALL_TRIP_IDS[currentIndex]

    // Notify NavGraph whenever the active trip changes (keeps bottom bar gallery in sync)
    LaunchedEffect(currentTripId) { onTripChanged(currentTripId) }

    Scaffold(
        containerColor = BgBase,
        topBar = {
            TripDetailTopBar(
                title          = TripDetailData.forId(currentTripId).title,
                currentTripId  = currentTripId,
                onBack         = onBack,
                onGalleryClick = onGalleryClick,
                currentIndex   = currentIndex,
                totalTrips     = ALL_TRIP_IDS.size,
                onPrev         = { slideDirection = -1; currentIndex-- },
                onNext         = { slideDirection =  1; currentIndex++ }
            )
        }
    ) { innerPadding ->
        // Slide animation when switching between trips
        AnimatedContent(
            targetState    = currentTripId,
            transitionSpec = {
                if (slideDirection > 0) {
                    (slideInHorizontally(tween(280)) { it } + fadeIn(tween(280)))
                        .togetherWith(slideOutHorizontally(tween(280)) { -it } + fadeOut(tween(280)))
                } else {
                    (slideInHorizontally(tween(280)) { -it } + fadeIn(tween(280)))
                        .togetherWith(slideOutHorizontally(tween(280)) { it } + fadeOut(tween(280)))
                }
            },
            label    = "trip_slide",
            modifier = Modifier.padding(innerPadding)
        ) { animTripId ->
            TripDetailContent(
                trip           = remember(animTripId) { TripDetailData.forId(animTripId) },
                onGalleryClick = { onGalleryClick(animTripId) }
            )
        }
    }
}

// ─── Top bar with prev/next arrows and gallery button ────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripDetailTopBar(
    title         : String,
    currentTripId : String,
    onBack        : () -> Unit,
    onGalleryClick: (String) -> Unit,
    currentIndex  : Int,
    totalTrips    : Int,
    onPrev        : () -> Unit,
    onNext        : () -> Unit
) {
    TopAppBar(
        title = {
            // Centered title + pagination dots
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text       = title,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = Snow,
                    fontWeight = FontWeight.Bold,
                    maxLines   = 1
                )
                Spacer(Modifier.height(3.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    repeat(totalTrips) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentIndex) 8.dp else 5.dp)
                                .clip(CircleShape)
                                .background(if (index == currentIndex) EmeraldLight else Fog)
                        )
                    }
                }
            }
        },
        navigationIcon = {
            // Left side: back (gray) + previous trip (green)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Fog)
                }
                IconButton(onClick = onPrev, enabled = currentIndex > 0) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Previous trip",
                        tint = if (currentIndex > 0) EmeraldLight else BgOutline
                    )
                }
            }
        },
        actions = {
            // Right side: next trip (green) + gallery
            IconButton(onClick = onNext, enabled = currentIndex < totalTrips - 1) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Next trip",
                    tint = if (currentIndex < totalTrips - 1) EmeraldLight else BgOutline
                )
            }
            IconButton(onClick = { onGalleryClick(currentTripId) }) {
                Icon(Icons.Rounded.List, contentDescription = "Gallery", tint = Snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BgBase)
    )
}

// ─── Scrollable trip content (hero, stats, tabbed sections) ──────────────────

@Composable
private fun TripDetailContent(
    trip          : TripDetailModel,
    onGalleryClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.detail_tab_itinerary),
        stringResource(R.string.detail_tab_budget),
        stringResource(R.string.detail_tab_notes)
    )

    LazyColumn(
        modifier       = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { TripHero(trip = trip) }

        item {
            Spacer(Modifier.height(20.dp))
            TripStatsRow(trip = trip)
            Spacer(Modifier.height(20.dp))
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor   = CardSurface,
                contentColor     = VioletLight,
                indicator        = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color    = VioletLight
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick  = { selectedTab = index },
                        text     = {
                            Text(text  = title,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selectedTab == index) VioletLight else Fog)
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        when (selectedTab) {
            0 -> {
                if (trip.days.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                                .clip(RoundedCornerShape(16.dp)).background(CardSurface).padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No itinerary planned yet.",
                                style = MaterialTheme.typography.bodyMedium, color = Fog)
                        }
                    }
                } else {
                    trip.days.forEach { day ->
                        item {
                            DayHeader(label = day.label)
                            Spacer(Modifier.height(8.dp))
                        }
                        items(day.activities) { activity ->
                            ActivityRow(activity = activity)
                            Spacer(Modifier.height(8.dp))
                        }
                        item { Spacer(Modifier.height(12.dp)) }
                    }
                }
            }
            1 -> item { BudgetTab(trip = trip) }
            2 -> item { NotesTab(notes = trip.notes) }
        }
    }
}

// ─── Hero banner with cover emoji and destination ────────────────────────────

@Composable
private fun TripHero(trip: TripDetailModel) {
    Box(
        modifier = Modifier.fillMaxWidth().height(180.dp)
            .background(Brush.linearGradient(listOf(GradStart, GradMid, GradEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(trip.coverEmoji, fontSize = 52.sp)
            Spacer(Modifier.height(8.dp))
            Text(trip.destination,
                style = MaterialTheme.typography.bodyMedium, color = Snow.copy(alpha = 0.8f))
        }
    }
}

// ─── Stats row: nights, budget, activity count ────────────────────────────────

@Composable
private fun TripStatsRow(trip: TripDetailModel) {
    val labelNights     = stringResource(R.string.detail_stat_nights)
    val labelBudget     = stringResource(R.string.detail_stat_budget)
    val labelActivities = stringResource(R.string.detail_stat_activities)

    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)).background(CardSurface).padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        DetailStatChip("${trip.nights}",            labelNights)
        Box(Modifier.width(1.dp).height(32.dp).background(BgOutline))
        DetailStatChip("€${trip.budget}",           labelBudget)
        Box(Modifier.width(1.dp).height(32.dp).background(BgOutline))
        DetailStatChip("${trip.totalActivities()}", labelActivities)
    }
}

@Composable
private fun DetailStatChip(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = Snow, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Fog)
    }
}

// ─── Day section header ───────────────────────────────────────────────────────

@Composable
private fun DayHeader(label: String) {
    Text(label, style = MaterialTheme.typography.labelLarge, color = VioletLight,
        modifier = Modifier.padding(horizontal = 24.dp))
}

// ─── Single activity row ──────────────────────────────────────────────────────

@Composable
private fun ActivityRow(activity: ActivityModel) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(14.dp)).background(CardSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(activity.emoji, fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(activity.title, style = MaterialTheme.typography.titleMedium, color = Snow)
            Text(activity.desc,  style = MaterialTheme.typography.bodySmall,   color = Fog)
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(activity.time, style = MaterialTheme.typography.labelSmall, color = Fog)
            Spacer(Modifier.height(4.dp))
            if (activity.cost == 0) {
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp))
                    .background(Emerald.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("FREE", style = MaterialTheme.typography.labelSmall, color = Emerald)
                }
            } else {
                Text("€${activity.cost}", style = MaterialTheme.typography.labelLarge,
                    color = VioletLight, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ─── Budget tab: total, spent, remaining + progress bar ───────────────────────

@Composable
private fun BudgetTab(trip: TripDetailModel) {
    val spent     = trip.days.flatMap { it.activities }.sumOf { it.cost }
    val remaining = trip.budget - spent
    val progress  = if (trip.budget > 0) (spent.toFloat() / trip.budget).coerceIn(0f, 1f) else 0f

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        BudgetRow(stringResource(R.string.detail_budget_total),     "€${trip.budget}", Snow)
        Spacer(Modifier.height(8.dp))
        BudgetRow(stringResource(R.string.detail_budget_spent),     "€$spent",         Amber)
        Spacer(Modifier.height(8.dp))
        BudgetRow(stringResource(R.string.detail_budget_remaining), "€$remaining",     Emerald)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            progress   = progress,
            modifier   = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color      = VioletLight,
            trackColor = BgOutline
        )
        Spacer(Modifier.height(4.dp))
        Text("${(progress * 100).toInt()}% spent",
            style = MaterialTheme.typography.bodySmall, color = Fog)
    }
}

@Composable
private fun BudgetRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier              = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(CardSurface).padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,  color = Mist)
        Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

// ─── Notes tab ────────────────────────────────────────────────────────────────

@Composable
private fun NotesTab(notes: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        .clip(RoundedCornerShape(16.dp)).background(CardSurface).padding(20.dp)) {
        Text(notes.ifEmpty { "—" }, style = MaterialTheme.typography.bodyMedium,
            color = if (notes.isEmpty()) Fog else Mist)
    }
}

// ─── Data models ──────────────────────────────────────────────────────────────

data class ActivityModel(val emoji: String, val time: String, val title: String, val desc: String, val cost: Int)
data class DayModel(val label: String, val activities: List<ActivityModel>)
data class TripDetailModel(
    val id        : String,
    val title     : String,
    val destination: String,
    val coverEmoji: String,
    val nights    : Int,
    val budget    : Int,
    val notes     : String,
    val days      : List<DayModel>
) {
    fun totalActivities() = days.sumOf { it.activities.size }
}

// ─── Mock data source — replace with repository when data layer is ready ──────

object TripDetailData {
    fun forId(id: String): TripDetailModel = when (id) {
        "trip_kyoto" -> TripDetailModel(
            id          = "trip_kyoto",
            title       = "Kyoto Escape",
            destination = "Kyoto, Japan",
            coverEmoji  = "⛩️",
            nights      = 9,
            budget      = 1560,
            notes       = "Book Arashiyama bamboo grove early morning to avoid crowds. Get an IC Card at the airport. Try kaiseki dinner at Nakamura.",
            days = listOf(
                DayModel("DAY 1 · JUN 3", listOf(
                    ActivityModel("✈️", "09:15", "Flight BCN → KIX",        "Iberia IB6845 · Terminal 1",      480),
                    ActivityModel("🏨", "21:00", "Check-in Gion Ryokan",    "Gion district · Traditional inn", 110)
                )),
                DayModel("DAY 2 · JUN 4", listOf(
                    ActivityModel("🎋", "07:00", "Arashiyama Bamboo Grove", "Early visit · 1h walk",             0),
                    ActivityModel("🍵", "11:00", "Tea Ceremony",            "Urasenke school · 2h",             45),
                    ActivityModel("⛩️", "15:00", "Fushimi Inari Shrine",    "Thousand torii gates",              0)
                )),
                DayModel("DAY 3 · JUN 5", listOf(
                    ActivityModel("🏯", "10:00", "Nijo Castle",    "UNESCO World Heritage site", 15),
                    ActivityModel("🍱", "19:00", "Kaiseki dinner", "Nakamura · Reserve ahead",   85)
                ))
            )
        )
        "trip_morocco" -> TripDetailModel(
            id          = "trip_morocco",
            title       = "Moroccan Dream",
            destination = "Marrakech, Morocco",
            coverEmoji  = "🕌",
            nights      = 6,
            budget      = 980,
            notes       = "Bargain at the souks — start at 30% of the asking price. Book a desert excursion in advance. Avoid tap water.",
            days = listOf(
                DayModel("DAY 1 · SEP 14", listOf(
                    ActivityModel("✈️", "06:30", "Flight BCN → RAK", "Ryanair FR2241",        95),
                    ActivityModel("🏨", "11:00", "Riad Yasmine",     "Medina · Rooftop pool", 75)
                )),
                DayModel("DAY 2 · SEP 15", listOf(
                    ActivityModel("🏺", "09:00", "Medina Souks",           "Spices, crafts & textiles",        0),
                    ActivityModel("🍊", "13:00", "Lunch at Djemaa el-Fna", "Street food · Fresh orange juice", 12),
                    ActivityModel("🌅", "17:00", "Majorelle Garden",        "Yves Saint Laurent garden",       15)
                ))
            )
        )
        else -> TripDetailModel(
            id          = "trip_iceland",
            title       = "Iceland Aurora",
            destination = "Reykjavik, Iceland",
            coverEmoji  = "🌌",
            nights      = 5,
            budget      = 1800,
            notes       = "",
            days        = emptyList()
        )
    }
}