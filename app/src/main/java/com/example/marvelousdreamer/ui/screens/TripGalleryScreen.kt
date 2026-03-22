package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
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

/**
 * P04 — Trip Gallery (T3.1)
 *
 * - TopBar amb títol i botó enrere
 * - Botó "Afegir foto" (UI only)
 * - Filter chips: All | ★ Top | Video
 * - Graella 3 columnes amb emoji + caption
 * - Badge TOP en destacats
 * - Botó eliminar per item (UI only)
 */
@Composable
fun TripGalleryScreen(
    tripId: String,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val filterAll   = stringResource(R.string.gallery_filter_all)
    val filterTop   = stringResource(R.string.gallery_filter_top)
    val filterVideo = stringResource(R.string.gallery_filter_video)
    val filters     = listOf(filterAll, filterTop, filterVideo)

    var selectedFilter by remember { mutableStateOf(filterAll) }

    val allItems = GalleryData.forTrip(tripId)
    val items    = when (selectedFilter) {
        filterTop   -> allItems.filter { it.isTop }
        filterVideo -> allItems.filter { it.isVideo }
        else        -> allItems
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            GalleryTopBar(
                tripId = tripId,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ── Add photo button ──────────────────────────────────────────
            Spacer(Modifier.height(12.dp))
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // Filter chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick  = { selectedFilter = filter },
                            label    = { Text(filter, style = MaterialTheme.typography.labelLarge) },
                            colors   = FilterChipDefaults.filterChipColors(
                                selectedContainerColor    = c.violet,
                                selectedLabelColor        = c.snow,
                                containerColor            = c.cardSurface,
                                labelColor                = c.fog
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled          = true,
                                selected         = selectedFilter == filter,
                                borderColor      = c.bgOutline,
                                selectedBorderColor = c.violet
                            )
                        )
                    }
                }
                // Add button
                SmallFloatingActionButton(
                    onClick        = { /* @TODO pick image */ },
                    containerColor = c.violet,
                    contentColor   = c.snow,
                    shape          = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = stringResource(R.string.gallery_add_photo))
                }
            }

            Spacer(Modifier.height(12.dp))

            // ── Grid ──────────────────────────────────────────────────────
            LazyVerticalGrid(
                columns        = GridCells.Fixed(3),
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement   = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    GalleryCell(item = item)
                }
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GalleryTopBar(tripId: String, onBack: () -> Unit) {
    val c = AppTheme.colors
    val title = when (tripId) {
        "trip_kyoto"   -> "Kyoto Escape"
        "trip_morocco" -> "Moroccan Dream"
        else           -> "Iceland Aurora"
    }
    TopAppBar(
        title = {
            Text(
                text       = "$title · Gallery",
                style      = MaterialTheme.typography.titleLarge,
                color      = c.snow,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
    )
}

// ─── Gallery cell ─────────────────────────────────────────────────────────────

@Composable
private fun GalleryCell(item: GalleryItemModel) {
    val c = AppTheme.colors
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        c.cardSurface,
                        c.bgElevated
                    )
                )
            )
    ) {
        // Emoji as image placeholder
        Text(
            text     = item.emoji,
            fontSize = 36.sp,
            modifier = Modifier.align(Alignment.Center)
        )

        // Video badge
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(c.bgBase.copy(alpha = 0.75f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text("▶", fontSize = 9.sp, color = c.snow)
            }
        }

        // TOP badge
        if (item.isTop) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(c.amber.copy(alpha = 0.9f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text("★ TOP", fontSize = 9.sp, color = c.bgBase, fontWeight = FontWeight.Bold)
            }
        }

        // Delete button
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(c.bgBase.copy(alpha = 0.7f))
        ) {
            Icon(
                imageVector        = Icons.Rounded.Close,
                contentDescription = "Delete",
                tint               = c.snow,
                modifier           = Modifier
                    .size(14.dp)
                    .align(Alignment.Center)
            )
        }

        // Caption overlay at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            c.bgBase.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Text(
                text  = item.caption,
                style = MaterialTheme.typography.labelSmall,
                color = c.snow,
                maxLines = 1
            )
        }
    }
}

// ─── Data model ───────────────────────────────────────────────────────────────

data class GalleryItemModel(
    val emoji  : String,
    val caption: String,
    val isTop  : Boolean = false,
    val isVideo: Boolean = false
)

object GalleryData {
    fun forTrip(tripId: String): List<GalleryItemModel> = when (tripId) {
        "trip_kyoto" -> listOf(
            GalleryItemModel("⛩️", "Fushimi Inari",      isTop   = true),
            GalleryItemModel("🎋", "Bamboo Grove"),
            GalleryItemModel("🌙", "Gion at night",      isTop   = true),
            GalleryItemModel("🍵", "Tea Ceremony"),
            GalleryItemModel("🏯", "Nijo Castle"),
            GalleryItemModel("✨", "Kinkaku-ji",         isVideo = true),
            GalleryItemModel("🍱", "Kaiseki dinner"),
            GalleryItemModel("🚄", "Shinkansen view"),
            GalleryItemModel("🌸", "Philosopher's Path")
        )
        "trip_morocco" -> listOf(
            GalleryItemModel("🏟️", "Djemaa el-Fna",     isTop   = true),
            GalleryItemModel("🌿", "Majorelle Garden"),
            GalleryItemModel("🌆", "Medina at dusk",    isVideo = true),
            GalleryItemModel("🍵", "Mint tea")
        )
        else -> emptyList()
    }
}