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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
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
import com.example.marvelousdreamer.domain.Activity
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.domain.Trip
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import java.time.format.DateTimeFormatter

/**
 * Trip Detail Screen — Sprint 02 rewrite.
 * Now uses ViewModel data instead of hardcoded mock data.
 * Supports CRUD navigation for activities (T2.1, T2.2, T2.3).
 */
@Composable
fun TripDetailScreen(
    tripId          : String,
    viewModel       : TripViewModel,
    onBack          : () -> Unit,
    onEditTrip      : (String) -> Unit = {},
    onAddActivity   : (String) -> Unit = {},
    onEditActivity  : (String, String) -> Unit = { _, _ -> },
    onGalleryClick  : (String) -> Unit = {},
    onTripChanged   : (String) -> Unit = {}
) {
    val c = AppTheme.colors
    val trips by viewModel.trips.collectAsState()
    val tripIds = trips.map { it.id }

    var currentIndex by remember(tripIds, tripId) {
        mutableIntStateOf(tripIds.indexOf(tripId).coerceAtLeast(0))
    }
    var slideDirection by remember { mutableIntStateOf(1) }

    val currentTripId = tripIds.getOrNull(currentIndex) ?: tripId
    val currentTrip = trips.find { it.id == currentTripId }

    LaunchedEffect(currentTripId) {
        onTripChanged(currentTripId)
        viewModel.selectTrip(currentTripId)
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TripDetailTopBar(
                title        = currentTrip?.title ?: "Trip",
                onBack       = onBack,
                onEditTrip   = { onEditTrip(currentTripId) },
                currentIndex = currentIndex,
                totalTrips   = tripIds.size,
                onPrev       = { if (currentIndex > 0) { slideDirection = -1; currentIndex-- } },
                onNext       = { if (currentIndex < tripIds.size - 1) { slideDirection = 1; currentIndex++ } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = { onAddActivity(currentTripId) },
                shape          = RoundedCornerShape(16.dp),
                containerColor = c.emerald,
                contentColor   = c.snow
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add activity")
            }
        }
    ) { innerPadding ->
        if (currentTrip != null) {
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
                val animTrip = trips.find { it.id == animTripId }
                if (animTrip != null) {
                    TripDetailContent(
                        trip            = animTrip,
                        onEditActivity  = { actId -> onEditActivity(animTripId, actId) },
                        onDeleteActivity = { actId -> viewModel.deleteActivity(animTripId, actId) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No trip found", color = c.fog)
            }
        }
    }
}

// ─── Top bar with prev/next arrows and edit button ───────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripDetailTopBar(
    title       : String,
    onBack      : () -> Unit,
    onEditTrip  : () -> Unit,
    currentIndex: Int,
    totalTrips  : Int,
    onPrev      : () -> Unit,
    onNext      : () -> Unit
) {
    val c = AppTheme.colors
    TopAppBar(
        title = {
            Column(
                modifier            = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text       = title,
                    style      = MaterialTheme.typography.titleMedium,
                    color      = c.snow,
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
                                .background(if (index == currentIndex) c.emeraldLight else c.fog)
                        )
                    }
                }
            }
        },
        navigationIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.fog)
                }
                IconButton(onClick = onPrev, enabled = currentIndex > 0) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Previous trip",
                        tint = if (currentIndex > 0) c.emeraldLight else c.bgOutline
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onNext, enabled = currentIndex < totalTrips - 1) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowForward,
                    contentDescription = "Next trip",
                    tint = if (currentIndex < totalTrips - 1) c.emeraldLight else c.bgOutline
                )
            }
            IconButton(onClick = onEditTrip) {
                Icon(Icons.Rounded.Edit, contentDescription = "Edit trip", tint = c.violetLight)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
    )
}

// ─── Scrollable trip content (hero, stats, tabbed sections) ──────────────────

@Composable
private fun TripDetailContent(
    trip            : Trip,
    onEditActivity  : (String) -> Unit,
    onDeleteActivity: (String) -> Unit
) {
    val c = AppTheme.colors
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.detail_tab_itinerary),
        stringResource(R.string.detail_tab_budget),
        stringResource(R.string.detail_tab_notes)
    )
    val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    LazyColumn(
        modifier       = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
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
                containerColor   = c.cardSurface,
                contentColor     = c.violetLight,
                indicator        = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color    = c.violetLight
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
                                color = if (selectedTab == index) c.violetLight else c.fog)
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        when (selectedTab) {
            0 -> {
                if (trip.activities.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                                .clip(RoundedCornerShape(16.dp)).background(c.cardSurface).padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No activities yet. Tap + to add one!",
                                style = MaterialTheme.typography.bodyMedium, color = c.fog)
                        }
                    }
                } else {
                    // Group activities by date
                    val grouped = trip.activities.sortedWith(compareBy({ it.date }, { it.time }))
                        .groupBy { it.date }

                    grouped.forEach { (date, activities) ->
                        item {
                            DayHeader(label = date.format(dateFmt))
                            Spacer(Modifier.height(8.dp))
                        }
                        items(activities, key = { it.id }) { activity ->
                            ActivityRow(
                                activity = activity,
                                onEdit   = { onEditActivity(activity.id) },
                                onDelete = { onDeleteActivity(activity.id) }
                            )
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

// ─── Hero banner ─────────────────────────────────────────────────────────────

@Composable
private fun TripHero(trip: Trip) {
    val c = AppTheme.colors
    Box(
        modifier = Modifier.fillMaxWidth().height(180.dp)
            .background(Brush.linearGradient(listOf(c.gradStart, c.gradMid, c.gradEnd))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✈️", fontSize = 52.sp)
            Spacer(Modifier.height(8.dp))
            Text(trip.destination.ifEmpty { trip.title },
                style = MaterialTheme.typography.bodyMedium, color = c.snow.copy(alpha = 0.8f))
            Spacer(Modifier.height(4.dp))
            val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            Text("${trip.startDate.format(dateFmt)} – ${trip.endDate.format(dateFmt)}",
                style = MaterialTheme.typography.bodySmall, color = c.snow.copy(alpha = 0.6f))
        }
    }
}

// ─── Stats row ───────────────────────────────────────────────────────────────

@Composable
private fun TripStatsRow(trip: Trip) {
    val c = AppTheme.colors
    val nights = trip.getDurationInDays()

    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(16.dp)).background(c.cardSurface).padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        DetailStatChip("$nights", stringResource(R.string.detail_stat_nights))
        Box(Modifier.width(1.dp).height(32.dp).background(c.bgOutline))
        DetailStatChip("€${trip.budget.toInt()}", stringResource(R.string.detail_stat_budget))
        Box(Modifier.width(1.dp).height(32.dp).background(c.bgOutline))
        DetailStatChip("${trip.activities.size}", stringResource(R.string.detail_stat_activities))
    }
}

@Composable
private fun DetailStatChip(value: String, label: String) {
    val c = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = c.snow, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = c.fog)
    }
}

// ─── Day section header ──────────────────────────────────────────────────────

@Composable
private fun DayHeader(label: String) {
    val c = AppTheme.colors
    Text(label, style = MaterialTheme.typography.labelLarge, color = c.violetLight,
        modifier = Modifier.padding(horizontal = 24.dp))
}

// ─── Single activity row with edit/delete ────────────────────────────────────

@Composable
private fun ActivityRow(
    activity: Activity,
    onEdit  : () -> Unit,
    onDelete: () -> Unit
) {
    val c = AppTheme.colors
    val emoji = when (activity.type) {
        ActivityType.FLIGHT    -> "✈️"
        ActivityType.HOTEL     -> "🏨"
        ActivityType.FOOD      -> "🍱"
        ActivityType.TRANSPORT -> "🚄"
        ActivityType.VISIT     -> "⛩️"
        ActivityType.OTHER     -> "📌"
    }

    Row(
        modifier          = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(14.dp)).background(c.cardSurface)
            .clickable(onClick = onEdit)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(activity.title, style = MaterialTheme.typography.titleMedium, color = c.snow)
            if (activity.description.isNotEmpty()) {
                Text(activity.description, style = MaterialTheme.typography.bodySmall, color = c.fog)
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(activity.time.toString(), style = MaterialTheme.typography.labelSmall, color = c.fog)
            Spacer(Modifier.height(4.dp))
            if (activity.cost == 0.0) {
                Box(modifier = Modifier.clip(RoundedCornerShape(6.dp))
                    .background(c.emerald.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("FREE", style = MaterialTheme.typography.labelSmall, color = c.emerald)
                }
            } else {
                Text("€${activity.cost.toInt()}", style = MaterialTheme.typography.labelLarge,
                    color = c.violetLight, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(8.dp))
        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = c.rose, modifier = Modifier.size(18.dp))
        }
    }
}

// ─── Budget tab ──────────────────────────────────────────────────────────────

@Composable
private fun BudgetTab(trip: Trip) {
    val c = AppTheme.colors
    val spent     = trip.activities.sumOf { it.cost }
    val remaining = trip.budget - spent
    val progress  = if (trip.budget > 0) (spent / trip.budget).toFloat().coerceIn(0f, 1f) else 0f

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
        BudgetRow(stringResource(R.string.detail_budget_total),     "€${trip.budget.toInt()}", c.snow)
        Spacer(Modifier.height(8.dp))
        BudgetRow(stringResource(R.string.detail_budget_spent),     "€${spent.toInt()}",       c.amber)
        Spacer(Modifier.height(8.dp))
        BudgetRow(stringResource(R.string.detail_budget_remaining), "€${remaining.toInt()}",   c.emerald)
        Spacer(Modifier.height(16.dp))
        LinearProgressIndicator(
            progress   = { progress },
            modifier   = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color      = c.violetLight,
            trackColor = c.bgOutline
        )
        Spacer(Modifier.height(4.dp))
        Text("${(progress * 100).toInt()}% spent",
            style = MaterialTheme.typography.bodySmall, color = c.fog)
    }
}

@Composable
private fun BudgetRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    val c = AppTheme.colors
    Row(
        modifier              = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(c.cardSurface).padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium,  color = c.mist)
        Text(value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold)
    }
}

// ─── Notes tab ───────────────────────────────────────────────────────────────

@Composable
private fun NotesTab(notes: String) {
    val c = AppTheme.colors
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        .clip(RoundedCornerShape(16.dp)).background(c.cardSurface).padding(20.dp)) {
        Text(notes.ifEmpty { "—" }, style = MaterialTheme.typography.bodyMedium,
            color = if (notes.isEmpty()) c.fog else c.mist)
    }
}
