package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.marvelousdreamer.R
import com.example.marvelousdreamer.data.preferences.UserPreferencesManager
import com.example.marvelousdreamer.ui.themes.*

/**
 * P05 — Preferences (T4.4 Sprint 01 + T1.4 Sprint 02)
 *
 * Sprint 02: username, dateOfBirth, darkMode and language are now persisted
 * via UserPreferencesManager (SharedPreferences). Other fields remain UI-only mock.
 */
@Composable
fun PreferencesScreen(
    onBack: () -> Unit,
    onDarkModeChanged: (Boolean) -> Unit = {},
    onLanguageChanged: (String) -> Unit = {}
) {
    val c = AppTheme.colors

    val context      = LocalContext.current
    val prefsManager = remember { UserPreferencesManager(context) }

    // ── String resources ──────────────────────────────────────────────────────
    val title       = stringResource(R.string.prefs_title)
    val subtitle    = stringResource(R.string.prefs_subtitle)
    val languages   = stringArrayResource(R.array.languages).toList()
    val currencies  = stringArrayResource(R.array.currencies).toList()
    val dateFormats = stringArrayResource(R.array.date_formats).toList()
    val textSizes   = stringArrayResource(R.array.text_sizes).toList()

    // ── Persisted state (loaded from SharedPreferences) ───────────────────────
    var username    by remember { mutableStateOf(prefsManager.username) }
    var dateOfBirth by remember { mutableStateOf(prefsManager.dateOfBirth) }
    var darkMode    by remember { mutableStateOf(prefsManager.darkMode) }
    val savedLang   = prefsManager.language
    var selectedLanguage by remember {
        mutableStateOf(
            when (savedLang) {
                "ca" -> languages.getOrElse(0) { languages.first() }  // Català
                "es" -> languages.getOrElse(2) { languages.first() }  // Español
                else -> languages.getOrElse(1) { languages.first() }  // English
            }
        )
    }

    // ── UI-only state (Sprint 02: not yet persisted) ──────────────────────────
    var selectedCurrency   by remember { mutableStateOf(currencies.first()) }
    var selectedDateFormat by remember { mutableStateOf(dateFormats.first()) }
    var selectedTextSize   by remember { mutableStateOf(textSizes[1]) }
    var travelReminders    by remember { mutableStateOf(prefsManager.travelReminders) }
    var weeklySummary      by remember { mutableStateOf(prefsManager.weeklySummary) }
    var aiSuggestions      by remember { mutableStateOf(prefsManager.aiSuggestions) }

    Scaffold(
        containerColor = c.bgBase,
        topBar = { PrefsTopBar(title = title, onBack = onBack) }
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = c.fog)
                Spacer(Modifier.height(8.dp))
            }

            // ── Secció: Compte ────────────────────────────────────────────
            item { SectionLabel(stringResource(R.string.prefs_section_account)) }

            // Username — persisted
            item {
                PrefsTextField(
                    label        = stringResource(R.string.prefs_username_label),
                    value        = username,
                    onValueChange = {
                        username = it
                        prefsManager.username = it
                    },
                    placeholder  = "Adrià"
                )
            }

            // Date of birth — persisted as text
            item {
                PrefsTextField(
                    label        = stringResource(R.string.prefs_dob_label),
                    value        = dateOfBirth,
                    onValueChange = {
                        dateOfBirth = it
                        prefsManager.dateOfBirth = it
                    },
                    placeholder  = "dd/mm/aaaa",
                    keyboardType = KeyboardType.Number
                )
            }

            // ── Secció: Idioma i Regió ────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                SectionLabel(stringResource(R.string.prefs_section_language))
            }

            // Language — persisted
            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_language_label),
                    subtitle = stringResource(R.string.prefs_language_subtitle),
                    options  = languages,
                    selected = selectedLanguage,
                    onSelect = {
                        selectedLanguage = it
                        val langCode = when (it) {
                            languages.getOrElse(0) { "" } -> "ca"
                            languages.getOrElse(2) { "" } -> "es"
                            else                          -> "en"
                        }
                        prefsManager.language = langCode
                        onLanguageChanged(langCode)
                    }
                )
            }

            item {
                PrefsDropdown(
                    label    = stringResource(R.string.prefs_currency_label),
                    subtitle = stringResource(R.string.prefs_currency_subtitle),
                    options  = currencies,
                    selected = selectedCurrency,
                    onSelect = { selectedCurrency = it; prefsManager.currency = it }
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

            // Dark mode — persisted
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_darkmode_label),
                    subtitle = stringResource(R.string.prefs_darkmode_subtitle),
                    checked  = darkMode,
                    onCheck  = { darkMode = it; prefsManager.darkMode = it; onDarkModeChanged(it) }
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
                    onCheck  = { travelReminders = it; prefsManager.travelReminders = it }
                )
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_notif_weekly_label),
                    subtitle = stringResource(R.string.prefs_notif_weekly_subtitle),
                    checked  = weeklySummary,
                    onCheck  = { weeklySummary = it; prefsManager.weeklySummary = it }
                )
            }
            item {
                PrefsSwitch(
                    label    = stringResource(R.string.prefs_notif_ai_label),
                    subtitle = stringResource(R.string.prefs_notif_ai_subtitle),
                    checked  = aiSuggestions,
                    onCheck  = { aiSuggestions = it; prefsManager.aiSuggestions = it }
                )
            }
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrefsTopBar(title: String, onBack: () -> Unit) {
    val c = AppTheme.colors
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge, color = c.snow, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.snow)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
    )
}

// ─── Section label ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    val c = AppTheme.colors
    Text(
        text       = text,
        style      = MaterialTheme.typography.labelLarge,
        color      = c.violetLight,
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.padding(vertical = 4.dp)
    )
}

// ─── Text field row ───────────────────────────────────────────────────────────

@Composable
private fun PrefsTextField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String       = "",
    keyboardType : KeyboardType = KeyboardType.Text
) {
    val c = AppTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardSurface)
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium, color = c.snow,
                modifier = Modifier.weight(1f))
            OutlinedTextField(
                value           = value,
                onValueChange   = onValueChange,
                placeholder     = { Text(placeholder, color = c.fog) },
                singleLine      = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                modifier        = Modifier.width(160.dp),
                shape           = RoundedCornerShape(8.dp),
                colors          = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = c.violet,
                    unfocusedBorderColor = c.bgOutline,
                    focusedTextColor     = c.snow,
                    unfocusedTextColor   = c.snow,
                    cursorColor          = c.violetLight
                )
            )
        }
    }
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
    val c = AppTheme.colors
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardSurface)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleMedium, color = c.snow)
                if (subtitle.isNotEmpty()) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = c.fog)
                }
            }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedButton(
                    onClick  = {},
                    modifier = Modifier.menuAnchor(),
                    shape    = RoundedCornerShape(8.dp),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = c.mist),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, c.bgOutline)
                ) {
                    Text(selected, style = MaterialTheme.typography.bodySmall, color = c.mist)
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, containerColor = c.bgElevated) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text    = { Text(option, color = if (option == selected) c.violetLight else c.snow) },
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
    val c = AppTheme.colors
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(c.cardSurface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.titleMedium, color = c.snow)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = c.fog)
        }
        Switch(
            checked         = checked,
            onCheckedChange = onCheck,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = c.snow,
                checkedTrackColor   = c.violet,
                uncheckedThumbColor = c.fog,
                uncheckedTrackColor = c.bgOutline
            )
        )
    }
}