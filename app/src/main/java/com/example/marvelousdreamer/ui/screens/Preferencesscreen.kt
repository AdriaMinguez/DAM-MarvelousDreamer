package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.ui.themes.*

/**
 * P05 — Preferences (T4.4)
 *
 * - Idioma i Regió: dropdowns de llengua, moneda, format data
 * - Aparença: switch mode fosc, dropdown mida text
 * - Notificacions: 3 switches
 *
 * Només interfície — sense lògica associada.
 */
@Composable
fun PreferencesScreen(onBack: () -> Unit) {

    // ── String resources ──────────────────────────────────────────────────────
    val title    = stringResource(R.string.prefs_title)
    val subtitle = stringResource(R.string.prefs_subtitle)

    val languages   = stringArrayResource(R.array.languages).toList()
    val currencies  = stringArrayResource(R.array.currencies).toList()
    val dateFormats = stringArrayResource(R.array.date_formats).toList()
    val textSizes   = stringArrayResource(R.array.text_sizes).toList()

    // ── UI state (mock — no persistència en Sprint 01) ────────────────────────
    var selectedLanguage   by remember { mutableStateOf(languages.first()) }
    var selectedCurrency   by remember { mutableStateOf(currencies.first()) }
    var selectedDateFormat by remember { mutableStateOf(dateFormats.first()) }
    var selectedTextSize   by remember { mutableStateOf(textSizes[1]) }
    var darkMode           by remember { mutableStateOf(true) }
    var travelReminders    by remember { mutableStateOf(true) }
    var weeklySummary      by remember { mutableStateOf(true) }
    var aiSuggestions      by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BgBase,
        topBar = {
            PrefsTopBar(title = title, onBack = onBack)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier       = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── Subtitle ──────────────────────────────────────────────────
            item {
                Text(
                    text  = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Fog
                )
                Spacer(Modifier.height(8.dp))
            }

            // ── Secció: Idioma i Regió ────────────────────────────────────
            item {
                SectionLabel(stringResource(R.string.prefs_section_language))
            }
            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_language_label),
                    subtitle = stringResource(R.string.prefs_language_subtitle),
                    options  = languages,
                    selected = selectedLanguage,
                    onSelect = { selectedLanguage = it }
                )
            }
            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_currency_label),
                    subtitle = stringResource(R.string.prefs_currency_subtitle),
                    options  = currencies,
                    selected = selectedCurrency,
                    onSelect = { selectedCurrency = it }
                )
            }
            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_dateformat_label),
                    subtitle = "",
                    options  = dateFormats,
                    selected = selectedDateFormat,
                    onSelect = { selectedDateFormat = it }
                )
            }

            // ── Secció: Aparença ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                SectionLabel(stringResource(R.string.prefs_section_appearance))
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_darkmode_label),
                    subtitle = stringResource(R.string.prefs_darkmode_subtitle),
                    checked  = darkMode,
                    onCheck  = { darkMode = it }
                )
            }
            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_textsize_label),
                    subtitle = stringResource(R.string.prefs_textsize_subtitle),
                    options  = textSizes,
                    selected = selectedTextSize,
                    onSelect = { selectedTextSize = it }
                )
            }

            // ── Secció: Notificacions ─────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                SectionLabel(stringResource(R.string.prefs_section_notifications))
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_notif_reminders_label),
                    subtitle = stringResource(R.string.prefs_notif_reminders_subtitle),
                    checked  = travelReminders,
                    onCheck  = { travelReminders = it }
                )
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_notif_weekly_label),
                    subtitle = stringResource(R.string.prefs_notif_weekly_subtitle),
                    checked  = weeklySummary,
                    onCheck  = { weeklySummary = it }
                )
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_notif_ai_label),
                    subtitle = stringResource(R.string.prefs_notif_ai_subtitle),
                    checked  = aiSuggestions,
                    onCheck  = { aiSuggestions = it }
                )
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrefsTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge, color = Snow, fontWeight = FontWeight.Bold)
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = Snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = BgBase)
    )
}

// ─── Section label ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text      = text,
        style     = MaterialTheme.typography.labelLarge,
        color     = VioletLight,
        fontWeight = FontWeight.Bold,
        modifier  = Modifier.padding(vertical = 4.dp)
    )
}

// ─── Dropdown row ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrefsDropdown(
    label   : String,
    subtitle: String,
    options : List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardSurface)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleMedium, color = Snow)
                if (subtitle.isNotEmpty()) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Fog)
                }
            }
            ExposedDropdownMenuBox(
                expanded         = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedButton(
                    onClick  = {},
                    modifier = Modifier.menuAnchor(),
                    shape    = RoundedCornerShape(8.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = Mist),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, BgOutline)
                ) {
                    Text(selected, style = MaterialTheme.typography.bodySmall, color = Mist)
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
                ExposedDropdownMenu(
                    expanded         = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor   = BgElevated
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text    = { Text(option, color = if (option == selected) VioletLight else Snow) },
                            onClick = { onSelect(option); expanded = false }
                        )
                    }
                }
            }
        }
    }
}

// ─── Switch row ───────────────────────────────────────────────────────────────

@Composable
private fun PrefsSwitch(
    label   : String,
    subtitle: String,
    checked : Boolean,
    onCheck : (Boolean) -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardSurface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleMedium, color = Snow)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Fog)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onCheck,
            colors          = SwitchDefaults.colors(
                checkedThumbColor       = Snow,
                checkedTrackColor       = Violet,
                uncheckedThumbColor     = Fog,
                uncheckedTrackColor     = BgOutline
            )
        )
    }
}