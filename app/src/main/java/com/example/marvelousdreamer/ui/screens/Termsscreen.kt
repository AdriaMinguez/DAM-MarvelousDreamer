package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

/**
 * P07 — Terms & Conditions (T4.3)
 *
 * - Títol + data actualització
 * - 5 seccions de text legal
 * - Barra inferior amb botons Rebutjar / Acceptar
 */
@Composable
fun TermsScreen(
    onAccept : () -> Unit,
    onDecline: () -> Unit
) {
    val c = AppTheme.colors
    val title   = stringResource(R.string.terms_title)
    val updated = stringResource(R.string.terms_updated)

    val sections = listOf(
        stringResource(R.string.terms1_title) to stringResource(R.string.terms1_body),
        stringResource(R.string.terms2_title) to stringResource(R.string.terms2_body),
        stringResource(R.string.terms3_title) to stringResource(R.string.terms3_body),
        stringResource(R.string.terms4_title) to stringResource(R.string.terms4_body),
        stringResource(R.string.terms5_title) to stringResource(R.string.terms5_body)
    )

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TermsTopBar(title = title, onBack = onDecline)
        },
        bottomBar = {
            TermsBottomBar(onAccept = onAccept, onDecline = onDecline)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Data actualització ────────────────────────────────────────
            item {
                Text(
                    text  = updated,
                    style = MaterialTheme.typography.bodySmall,
                    color = c.fog
                )
                Spacer(Modifier.height(4.dp))
            }

            // ── Seccions de text ──────────────────────────────────────────
            items(sections.size) { index ->
                val (sectionTitle, sectionBody) = sections[index]
                TermsSection(title = sectionTitle, body = sectionBody)
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermsTopBar(title: String, onBack: () -> Unit) {
    val c = AppTheme.colors
    TopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge, color = c.snow, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
    )
}

// ─── Terms section card ───────────────────────────────────────────────────────

@Composable
private fun TermsSection(title: String, body: String) {
    val c = AppTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(c.cardSurface)
            .padding(16.dp)
    ) {
        Text(
            text       = title,
            style      = MaterialTheme.typography.titleMedium,
            color      = c.snow,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = body,
            style = MaterialTheme.typography.bodyMedium,
            color = c.mist
        )
    }
}

// ─── Bottom action bar ────────────────────────────────────────────────────────

@Composable
private fun TermsBottomBar(onAccept: () -> Unit, onDecline: () -> Unit) {
    val c = AppTheme.colors
    Surface(
        color = c.cardSurface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Rebutjar
            OutlinedButton(
                onClick  = onDecline,
                modifier = Modifier.weight(1f),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = c.fog),
                border   = androidx.compose.foundation.BorderStroke(1.dp, c.bgOutline)
            ) {
                Text(
                    text  = stringResource(R.string.terms_decline),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Acceptar
            Button(
                onClick  = onAccept,
                modifier = Modifier.weight(2f),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = c.violet,
                    contentColor   = c.snow
                )
            ) {
                Text(
                    text  = stringResource(R.string.terms_accept),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}