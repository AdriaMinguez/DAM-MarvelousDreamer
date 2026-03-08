package com.example.marvelousdreamer.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.screens.*
import com.example.marvelousdreamer.ui.themes.*

// Pantallas on NO volem mostrar el bottom bar
private val noBottomBarRoutes = setOf(
    Routes.SPLASH,
    Routes.TERMS
)

@Composable
fun NavGraph(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute !in noBottomBarRoutes &&
            currentRoute != null

    // Trackeja quin trip és actiu per enviar-lo a la galeria des del bottom bar
    var activeTripId by remember { mutableStateOf("trip_kyoto") }

    Scaffold(
        containerColor = BgBase,
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute   = currentRoute,
                    onHomeClick    = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    },
                    onTripsClick    = { navController.navigate(Routes.tripDetail(activeTripId)) },
                    onGalleryClick  = { navController.navigate(Routes.tripGallery(activeTripId)) },
                    onSettingsClick = { navController.navigate(Routes.PREFERENCES) }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = navController,
            startDestination = Routes.SPLASH,
            modifier         = Modifier.padding(innerPadding)
        ) {

            // ── Splash ────────────────────────────────────────────────────
            composable(Routes.SPLASH) {
                SplashScreen(
                    onFinished = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            // ── Home ──────────────────────────────────────────────────────
            composable(Routes.HOME) {
                HomeScreen(
                    onTripClick     = { tripId -> navController.navigate(Routes.tripDetail(tripId)) },
                    onSeeAllClick   = { navController.navigate(Routes.TRIPS_LIST) },
                    onProfileClick  = { navController.navigate(Routes.PROFILE) },
                    onTermsClick    = { navController.navigate(Routes.TERMS) },
                    onAboutClick    = { navController.navigate(Routes.ABOUT) },
                    onSettingsClick = { navController.navigate(Routes.PREFERENCES) }
                )
            }

            // ── Trip Detail ───────────────────────────────────────────────
            composable(
                route     = Routes.TRIP_DETAIL,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                if (tripId.isNotEmpty()) {
                    TripDetailScreen(
                        tripId         = tripId,
                        onBack         = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                        onGalleryClick = { id -> activeTripId = id; navController.navigate(Routes.tripGallery(id)) },
                        onTripChanged  = { id -> activeTripId = id }
                    )
                }
            }

            // ── Trip Gallery ──────────────────────────────────────────────
            composable(
                route     = Routes.TRIP_GALLERY,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                if (tripId.isNotEmpty()) {
                    TripGalleryScreen(
                        tripId = tripId,
                        onBack = {
                            activeTripId = tripId
                            navController.navigate(Routes.tripDetail(tripId)) {
                                popUpTo(Routes.HOME) { inclusive = false }
                            }
                        }
                    )
                }
            }

            // ── Preferences ───────────────────────────────────────────────
            composable(Routes.PREFERENCES) {
                PreferencesScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // ── Profile ───────────────────────────────────────────────────
            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack          = { navController.popBackStack() },
                    onSettingsClick = { navController.navigate(Routes.PREFERENCES) },
                    onTripClick     = { tripId -> navController.navigate(Routes.tripDetail(tripId)) }
                )
            }

            // ── About ─────────────────────────────────────────────────────
            composable(Routes.ABOUT) {
                AboutScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // ── Terms ─────────────────────────────────────────────────────
            composable(Routes.TERMS) {
                TermsScreen(
                    onAccept  = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.TERMS) { inclusive = true }
                        }
                    },
                    onDecline = { navController.popBackStack() }
                )
            }

            // ── Trips List ────────────────────────────────────────────────
            composable(Routes.TRIPS_LIST) {
                TripDetailScreen(
                    tripId         = activeTripId,
                    onBack         = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                    onGalleryClick = { id -> activeTripId = id; navController.navigate(Routes.tripGallery(id)) },
                    onTripChanged  = { id -> activeTripId = id }
                )
            }

            // ── Gallery All ───────────────────────────────────────────────
            composable(Routes.GALLERY_ALL) {
                TripGalleryScreen(
                    tripId = "trip_kyoto",
                    onBack = { navController.navigate(Routes.HOME) }
                )
            }
        }
    }
}

// ─── Bottom bar global ────────────────────────────────────────────────────────

@Composable
private fun AppBottomBar(
    currentRoute   : String?,
    onHomeClick    : () -> Unit,
    onTripsClick   : () -> Unit,
    onGalleryClick : () -> Unit,
    onSettingsClick: () -> Unit
) {
    val navHome     = stringResource(R.string.nav_home)
    val navTrips    = stringResource(R.string.nav_trips)
    val navGallery  = stringResource(R.string.nav_gallery)
    val navSettings = stringResource(R.string.nav_settings)

    Surface(color = CardSurface, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            BottomNavItem(
                emoji    = "🏠",
                label    = navHome,
                selected = currentRoute == Routes.HOME,
                onClick  = onHomeClick
            )
            BottomNavItem(
                emoji    = "🗺️",
                label    = navTrips,
                selected = currentRoute == Routes.TRIPS_LIST || currentRoute?.startsWith("trip_detail/") == true,
                onClick  = onTripsClick
            )
            BottomNavItem(
                emoji    = "🖼️",
                label    = navGallery,
                selected = currentRoute == Routes.GALLERY_ALL || currentRoute?.startsWith("trip_gallery/") == true,
                onClick  = onGalleryClick
            )
            BottomNavItem(
                emoji    = "⚙️",
                label    = navSettings,
                selected = currentRoute == Routes.PREFERENCES,
                onClick  = onSettingsClick
            )
        }
    }
}

@Composable
private fun BottomNavItem(emoji: String, label: String, selected: Boolean, onClick: () -> Unit) {
    Column(
        modifier            = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            text       = label,
            style      = MaterialTheme.typography.labelSmall,
            color      = if (selected) EmeraldLight else Fog,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (selected) EmeraldLight else Color.Transparent)
        )
    }
}