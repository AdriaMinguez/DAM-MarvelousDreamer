package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

/**
 * P06 — About Page (T4.2)
 *
 * - Logo + versió
 * - Tagline
 * - Secció equip: avatar + nom + rol
 * - Secció informació tècnica: taula clau-valor
 */
@Composable
fun AboutScreen(onBack: () -> Unit) {
    val c = AppTheme.colors

    val appName    = stringResource(R.string.app_name)
    val appVersion = stringResource(R.string.app_version)
    val appTagline = stringResource(R.string.app_tagline)
    val appBuiltAt = stringResource(R.string.app_built_at)

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            AboutTopBar(onBack = onBack)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding      = PaddingValues(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Logo + versió ─────────────────────────────────────────────
            item {
                Spacer(Modifier.height(32.dp))
                Image(
                    painter            = painterResource(R.drawable.marvelousdreamer_circle_),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(36.dp))
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text       = appName,
                    style      = MaterialTheme.typography.headlineLarge,
                    color      = c.snow,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign  = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(c.violet.copy(alpha = 0.2f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text  = "v$appVersion",
                        style = MaterialTheme.typography.labelLarge,
                        color = c.violetLight
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text      = appTagline,
                    style     = MaterialTheme.typography.bodyMedium,
                    color     = c.fog,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.padding(horizontal = 40.dp)
                )
                Spacer(Modifier.height(32.dp))
            }

            // ── Equip ─────────────────────────────────────────────────────
            item {
                AboutSection(title = stringResource(R.string.about_section_team))
                Spacer(Modifier.height(12.dp))
            }

            item {
                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.cardSurface),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    TeamMemberRow(
                        initials = stringResource(R.string.team1_initials),
                        name     = stringResource(R.string.team1_name),
                        role     = stringResource(R.string.team1_role)
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Info tècnica ──────────────────────────────────────────────
            item {
                AboutSection(title = stringResource(R.string.about_section_tech))
                Spacer(Modifier.height(12.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(c.cardSurface)
                ) {
                    val rows = listOf(
                        stringResource(R.string.about_tech_version) to stringResource(R.string.app_version),
                        stringResource(R.string.about_tech_sprint)  to stringResource(R.string.app_sprint),
                        stringResource(R.string.about_tech_android) to stringResource(R.string.app_min_api),
                        stringResource(R.string.about_tech_kotlin)  to stringResource(R.string.app_kotlin_version),
                        stringResource(R.string.about_tech_license) to stringResource(R.string.app_license)
                    )
                    rows.forEachIndexed { index, (key, value) ->
                        TechRow(key = key, value = value)
                        if (index < rows.lastIndex) {
                            HorizontalDivider(color = c.bgOutline, thickness = 0.5.dp)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }

            // ── Built at ──────────────────────────────────────────────────
            item {
                Text(
                    text      = appBuiltAt,
                    style     = MaterialTheme.typography.bodySmall,
                    color     = c.fog,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutTopBar(onBack: () -> Unit) {
    val c = AppTheme.colors
    TopAppBar(
        title = {
            Text(
                text       = "About",
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

// ─── Section title ────────────────────────────────────────────────────────────

@Composable
private fun AboutSection(title: String) {
    val c = AppTheme.colors
    Text(
        text       = title,
        style      = MaterialTheme.typography.labelLarge,
        color      = c.violetLight,
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.padding(horizontal = 24.dp)
    )
}

// ─── Team member row ──────────────────────────────────────────────────────────

@Composable
private fun TeamMemberRow(initials: String, name: String, role: String) {
    val c = AppTheme.colors
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(c.violet, c.emerald))),
            contentAlignment = Alignment.Center
        ) {
            Text(initials, style = MaterialTheme.typography.labelLarge, color = c.snow, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(name, style = MaterialTheme.typography.titleMedium, color = c.snow)
            Text(role, style = MaterialTheme.typography.bodySmall,   color = c.fog)
        }
    }
}

// ─── Tech info row ────────────────────────────────────────────────────────────

@Composable
private fun TechRow(key: String, value: String) {
    val c = AppTheme.colors
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(key,   style = MaterialTheme.typography.bodyMedium, color = c.fog)
        Text(value, style = MaterialTheme.typography.titleMedium, color = c.snow, fontWeight = FontWeight.SemiBold)
    }
}