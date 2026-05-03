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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.screens.*
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel

private val noBottomBarRoutes = setOf(
    Routes.SPLASH, Routes.TERMS, Routes.ADD_TRIP, Routes.EDIT_TRIP,
    Routes.ADD_ACTIVITY, Routes.EDIT_ACTIVITY,
    Routes.LOGIN, Routes.REGISTER, Routes.FORGOT_PASSWORD
)

@Composable
fun NavGraph(
    navController: NavHostController,
    onDarkModeChanged: (Boolean) -> Unit = {},
    onLanguageChanged: (String) -> Unit = {}
) {
    val c = AppTheme.colors
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != null &&
            noBottomBarRoutes.none { pattern ->
                currentRoute == pattern || currentRoute.startsWith(pattern.substringBefore("{"))
            }

    val authViewModel: AuthViewModel = hiltViewModel()
    val tripViewModel: TripViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState.isLoggedIn) {
        val uid = authViewModel.currentUserId
        if (uid != null) tripViewModel.setUserId(uid)
    }

    var activeTripId by remember { mutableStateOf("") }

    Scaffold(
        containerColor = c.bgBase,
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    currentRoute    = currentRoute,
                    onHomeClick     = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                    onTripsClick    = { if (activeTripId.isNotEmpty()) navController.navigate(Routes.tripDetail(activeTripId)) },
                    onGalleryClick  = { if (activeTripId.isNotEmpty()) navController.navigate(Routes.tripGallery(activeTripId)) },
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
            composable(Routes.SPLASH) {
                SplashScreen(
                    onFinished = {
                        val dest = if (authState.isLoggedIn) Routes.HOME else Routes.LOGIN
                        navController.navigate(dest) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    }
                )
            }

            composable(Routes.LOGIN) {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    },
                    onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                    onNavigateToForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) { popUpTo(Routes.LOGIN) { inclusive = true } }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen(
                    authViewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.EDIT_PROFILE) {
                EditProfileScreen(
                    authViewModel = authViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    viewModel       = tripViewModel,
                    authViewModel   = authViewModel,
                    onTripClick     = { tripId -> navController.navigate(Routes.tripDetail(tripId)) },
                    onAddTripClick  = { navController.navigate(Routes.ADD_TRIP) },
                    onSeeAllClick   = { navController.navigate(Routes.TRIPS_LIST) },
                    onProfileClick  = { navController.navigate(Routes.PROFILE) },
                    onTermsClick    = { navController.navigate(Routes.TERMS) },
                    onAboutClick    = { navController.navigate(Routes.ABOUT) },
                    onSettingsClick = { navController.navigate(Routes.PREFERENCES) },
                    onLogout        = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    }
                )
            }

            composable(
                route = Routes.TRIP_DETAIL,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                if (tripId.isNotEmpty()) {
                    TripDetailScreen(
                        tripId         = tripId,
                        viewModel      = tripViewModel,
                        onBack         = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                        onEditTrip     = { id -> navController.navigate(Routes.editTrip(id)) },
                        onAddActivity  = { id -> navController.navigate(Routes.addActivity(id)) },
                        onEditActivity = { tId, aId -> navController.navigate(Routes.editActivity(tId, aId)) },
                        onGalleryClick = { id -> activeTripId = id; navController.navigate(Routes.tripGallery(id)) },
                        onTripChanged  = { id -> activeTripId = id }
                    )
                }
            }

            composable(
                route = Routes.TRIP_GALLERY,
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

            composable(Routes.PREFERENCES) {
                PreferencesScreen(
                    onBack = { navController.popBackStack() },
                    onDarkModeChanged = onDarkModeChanged,
                    onLanguageChanged = onLanguageChanged
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    onBack          = { navController.popBackStack() },
                    onSettingsClick = { navController.navigate(Routes.PREFERENCES) },
                    onTripClick     = { tripId -> navController.navigate(Routes.tripDetail(tripId)) },
                    onEditProfile   = { navController.navigate(Routes.EDIT_PROFILE) },
                    onLogout        = {
                        authViewModel.logout()
                        navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                    },
                    viewModel       = tripViewModel,
                    authViewModel   = authViewModel
                )
            }

            composable(Routes.ABOUT) { AboutScreen(onBack = { navController.popBackStack() }) }

            composable(Routes.TERMS) {
                TermsScreen(
                    onAccept  = { navController.navigate(Routes.HOME) { popUpTo(Routes.TERMS) { inclusive = true } } },
                    onDecline = { navController.popBackStack() }
                )
            }

            composable(Routes.TRIPS_LIST) {
                TripDetailScreen(
                    tripId = activeTripId.ifEmpty { "none" },
                    viewModel = tripViewModel,
                    onBack         = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                    onEditTrip     = { id -> navController.navigate(Routes.editTrip(id)) },
                    onAddActivity  = { id -> navController.navigate(Routes.addActivity(id)) },
                    onEditActivity = { tId, aId -> navController.navigate(Routes.editActivity(tId, aId)) },
                    onGalleryClick = { id -> activeTripId = id; navController.navigate(Routes.tripGallery(id)) },
                    onTripChanged  = { id -> activeTripId = id }
                )
            }

            composable(Routes.GALLERY_ALL) {
                TripGalleryScreen(tripId = "", onBack = { navController.navigate(Routes.HOME) })
            }

            composable(Routes.ADD_TRIP) {
                LaunchedEffect(Unit) { tripViewModel.prepareAddTrip() }
                AddEditTripScreen(
                    tripId = null, viewModel = tripViewModel,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } }
                )
            }

            composable(
                route = Routes.EDIT_TRIP,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val trip = tripViewModel.trips.value.find { it.id == tripId }
                if (trip != null) {
                    LaunchedEffect(tripId) { tripViewModel.prepareEditTrip(trip) }
                    AddEditTripScreen(
                        tripId = tripId, viewModel = tripViewModel,
                        onBack = { navController.popBackStack() },
                        onSaved = { navController.popBackStack() }
                    )
                }
            }

            composable(
                route = Routes.ADD_ACTIVITY,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                LaunchedEffect(Unit) { tripViewModel.prepareAddActivity() }
                AddEditActivityScreen(
                    tripId = tripId, activityId = null, viewModel = tripViewModel,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.EDIT_ACTIVITY,
                arguments = listOf(
                    navArgument("tripId") { type = NavType.StringType },
                    navArgument("activityId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                val activityId = backStackEntry.arguments?.getString("activityId") ?: ""
                val activity = tripViewModel.trips.value
                    .find { it.id == tripId }?.activities?.find { it.id == activityId }
                if (activity != null) {
                    LaunchedEffect(activityId) { tripViewModel.prepareEditActivity(activity) }
                    AddEditActivityScreen(
                        tripId = tripId, activityId = activityId, viewModel = tripViewModel,
                        onBack = { navController.popBackStack() },
                        onSaved = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBottomBar(
    currentRoute: String?,
    onHomeClick: () -> Unit,
    onTripsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val c = AppTheme.colors
    val navHome = stringResource(R.string.nav_home)
    val navTrips = stringResource(R.string.nav_trips)
    val navGallery = stringResource(R.string.nav_gallery)
    val navSettings = stringResource(R.string.nav_settings)

    Surface(color = c.cardSurface, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("🏠", navHome, currentRoute == Routes.HOME, onHomeClick)
            BottomNavItem("🗺️", navTrips, currentRoute == Routes.TRIPS_LIST || currentRoute?.startsWith("trip_detail/") == true, onTripsClick)
            BottomNavItem("🖼️", navGallery, currentRoute == Routes.GALLERY_ALL || currentRoute?.startsWith("trip_gallery/") == true, onGalleryClick)
            BottomNavItem("⚙️", navSettings, currentRoute == Routes.PREFERENCES, onSettingsClick)
        }
    }
}

@Composable
private fun BottomNavItem(emoji: String, label: String, selected: Boolean, onClick: () -> Unit) {
    val c = AppTheme.colors
    Column(
        modifier = Modifier.clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = if (selected) c.emeraldLight else c.fog,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
        Spacer(Modifier.height(4.dp))
        Box(Modifier.size(4.dp).clip(CircleShape).background(if (selected) c.emeraldLight else Color.Transparent))
    }
}