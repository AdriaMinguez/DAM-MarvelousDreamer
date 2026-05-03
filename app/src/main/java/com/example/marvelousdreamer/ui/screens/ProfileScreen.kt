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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    onBack         : () -> Unit,
    onSettingsClick: () -> Unit = {},
    onTripClick    : (String) -> Unit = {},
    onEditProfile  : () -> Unit = {},
    onLogout       : () -> Unit = {},
    viewModel      : TripViewModel? = null,
    authViewModel  : AuthViewModel? = null
) {
    val c = AppTheme.colors

    // Read user data from Room
    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val localUser = authViewModel?.getLocalUser()
        if (localUser != null) {
            userName = localUser.username
            userEmail = localUser.login
        } else {
            val fb = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
            userEmail = fb?.email ?: ""
            userName = userEmail.substringBefore("@")
        }
    }

    val trips = viewModel?.trips?.collectAsState()?.value ?: emptyList()
    val totalNights = trips.sumOf { it.getDurationInDays() }
    val totalBudget = trips.sumOf { it.budget.toInt() }
    val totalSpent = trips.sumOf { trip -> trip.activities.sumOf { it.cost }.toInt() }

    Scaffold(
        containerColor = c.bgBase,
        topBar = { ProfileTopBar(onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding      = PaddingValues(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(Modifier.height(28.dp))
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(c.violet, c.emerald)))
                        .border(3.dp, c.bgOutline, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = if (userName.isNotEmpty()) userName.first().uppercase() else "?",
                        fontSize   = 38.sp,
                        color      = c.snow,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    text       = userName.ifEmpty { "User" },
                    style      = MaterialTheme.typography.headlineMedium,
                    color      = c.snow,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text      = userEmail,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = c.fog,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(c.emerald.copy(alpha = 0.15f))
                        .border(1.dp, c.emerald.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text("✦ Explorer", style = MaterialTheme.typography.labelLarge,
                        color = c.emeraldLight, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(28.dp))
            }

            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(c.cardSurface)
                        .border(1.dp, c.bgOutline, RoundedCornerShape(20.dp))
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    ProfileStatChip("${trips.size}", "TRIPS")
                    Box(Modifier.width(1.dp).height(36.dp).background(c.bgOutline))
                    ProfileStatChip("$totalNights", "NIGHTS")
                    Box(Modifier.width(1.dp).height(36.dp).background(c.bgOutline))
                    ProfileStatChip("€$totalBudget", "BUDGET")
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                val spendProgress = if (totalBudget > 0) totalSpent.toFloat() / totalBudget else 0f
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.cardSurface)
                        .border(1.dp, c.bgOutline, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total spending", style = MaterialTheme.typography.bodyMedium, color = c.fog)
                        Text("€$totalSpent / €$totalBudget",
                            style = MaterialTheme.typography.labelLarge,
                            color = c.violetLight, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(6.dp)
                            .clip(RoundedCornerShape(3.dp)).background(c.bgOutline)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(spendProgress).fillMaxHeight()
                                .background(Brush.horizontalGradient(listOf(c.violet, c.emerald)))
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text("${(spendProgress * 100).toInt()}% of total budget used",
                        style = MaterialTheme.typography.bodySmall, color = c.fog)
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                ProfileSection(title = "🗺️  MY TRIPS")
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.cardSurface)
                ) {
                    if (trips.isEmpty()) {
                        Text(
                            "No trips yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = c.fog,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        val dateFmt = DateTimeFormatter.ofPattern("dd/MM")
                        trips.forEachIndexed { index, trip ->
                            val dateRange = "${trip.startDate.format(dateFmt)} – ${trip.endDate.format(dateFmt)}"
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
                                        .background(Brush.linearGradient(listOf(c.gradStart, c.gradEnd))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("✈️", fontSize = 20.sp)
                                }
                                Spacer(Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(trip.title, style = MaterialTheme.typography.titleMedium, color = c.snow)
                                    Text(dateRange, style = MaterialTheme.typography.bodySmall, color = c.fog)
                                }
                                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
                                    tint = c.emeraldLight, modifier = Modifier.size(20.dp))
                            }
                            if (index < trips.lastIndex) {
                                HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            item {
                ProfileSection(title = "⚙️  ACCOUNT")
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.cardSurface)
                ) {
                    ProfileActionRow(icon = Icons.Rounded.Person, label = "Edit profile", tint = c.violetLight, onClick = onEditProfile)
                    HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Notifications, label = "Notifications", tint = c.violetLight, onClick = onSettingsClick)
                    HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Lock, label = "Privacy & security", tint = c.violetLight)
                    HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                    ProfileActionRow(icon = Icons.Rounded.Close, label = "Log out", tint = c.rose, onClick = onLogout)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    val c = AppTheme.colors
    TopAppBar(
        title = {
            Text("Profile", style = MaterialTheme.typography.titleLarge,
                color = c.snow, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
    )
}

@Composable
private fun ProfileSection(title: String) {
    val c = AppTheme.colors
    Text(text = title, style = MaterialTheme.typography.labelLarge,
        color = c.violetLight, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp))
}

@Composable
private fun ProfileStatChip(value: String, label: String) {
    val c = AppTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium,
            color = c.snow, fontWeight = FontWeight.ExtraBold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = c.fog)
    }
}

@Composable
private fun ProfileActionRow(
    icon   : ImageVector,
    label  : String,
    tint   : androidx.compose.ui.graphics.Color,
    onClick: () -> Unit = {}
) {
    val c = AppTheme.colors
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge, color = c.snow,
            modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null,
            tint = c.fog, modifier = Modifier.size(18.dp))
    }
}