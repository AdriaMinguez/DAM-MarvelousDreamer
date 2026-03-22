package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Screen for creating or editing a trip (T1.1 / T1.3).
 * Uses Material3 DatePickerDialog — dates cannot be typed manually.
 *
 * @param tripId  null = create mode, non-null = edit mode
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTripScreen(
    tripId    : String?,
    viewModel : TripViewModel,
    onBack    : () -> Unit,
    onSaved   : () -> Unit
) {
    val form by viewModel.tripForm.collectAsState()
    val c = AppTheme.colors
    val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // DatePicker dialog visibility
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker   by remember { mutableStateOf(false) }

    // ── DatePicker dialogs ────────────────────────────────────────────────────

    if (showStartPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = form.startDate
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.updateTripForm { copy(startDate = date, startDateError = null) }
                    }
                    showStartPicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("Cancel·lar", color = c.fog)
                }
            }
        ) { DatePicker(state = state) }
    }

    if (showEndPicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = form.endDate
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.updateTripForm { copy(endDate = date, endDateError = null) }
                    }
                    showEndPicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("Cancel·lar", color = c.fog)
                }
            }
        ) { DatePicker(state = state) }
    }

    // ── Scaffold ──────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = if (form.isEditing) "Editar viatge" else "Nou viatge",
                        style      = MaterialTheme.typography.titleLarge,
                        color      = c.snow,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Back", tint = c.snow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Title
            TripFormField(
                label        = "Títol *",
                value        = form.title,
                onValueChange = { viewModel.updateTripForm { copy(title = it, titleError = null) } },
                placeholder  = "Ex: Kyoto Escape ⛩️",
                error        = form.titleError
            )

            // Description
            TripFormField(
                label        = "Descripció",
                value        = form.description,
                onValueChange = { viewModel.updateTripForm { copy(description = it) } },
                placeholder  = "Descriu el teu viatge...",
                singleLine   = false
            )

            // Destination
            TripFormField(
                label        = "Destinació",
                value        = form.destination,
                onValueChange = { viewModel.updateTripForm { copy(destination = it) } },
                placeholder  = "Ex: Kyoto, Japan"
            )

            // Start date — DatePicker, no free text allowed
            DateField(
                label      = "Data d'inici *",
                value      = form.startDate?.format(fmt) ?: "",
                error      = form.startDateError,
                onClick    = { showStartPicker = true }
            )

            // End date — DatePicker, no free text allowed
            DateField(
                label      = "Data de fi *",
                value      = form.endDate?.format(fmt) ?: "",
                error      = form.endDateError,
                onClick    = { showEndPicker = true }
            )

            // Budget
            TripFormField(
                label        = "Pressupost (€)",
                value        = form.budget,
                onValueChange = { viewModel.updateTripForm { copy(budget = it) } },
                placeholder  = "0.00",
                keyboardType = KeyboardType.Decimal
            )

            Spacer(Modifier.height(8.dp))

            // Save button
            Button(
                onClick = { if (viewModel.saveTrip()) onSaved() },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = c.violet)
            ) {
                Text(
                    text       = if (form.isEditing) "Desar canvis" else "Crear viatge",
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = c.snow,
                    modifier   = Modifier.padding(vertical = 6.dp)
                )
            }

            // Delete button — only in edit mode
            if (form.isEditing) {
                OutlinedButton(
                    onClick  = {
                        form.editingId?.let { viewModel.deleteTrip(it) }
                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, c.rose),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = c.rose)
                ) {
                    Text(
                        text     = "Eliminar viatge",
                        style    = MaterialTheme.typography.titleMedium,
                        color    = c.rose,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}

// ── Reusable form components ──────────────────────────────────────────────────

@Composable
private fun TripFormField(
    label        : String,
    value        : String,
    onValueChange: (String) -> Unit,
    placeholder  : String       = "",
    error        : String?      = null,
    singleLine   : Boolean      = true,
    keyboardType : KeyboardType = KeyboardType.Text
) {
    val c = AppTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = c.mist)
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, color = c.fog) },
            singleLine    = singleLine,
            isError       = error != null,
            supportingText = error?.let { { Text(it, color = c.rose) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier      = Modifier.fillMaxWidth(),
            shape         = RoundedCornerShape(12.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = c.violet,
                unfocusedBorderColor = c.bgOutline,
                focusedTextColor     = c.snow,
                unfocusedTextColor   = c.snow,
                cursorColor          = c.violetLight,
                errorBorderColor     = c.rose
            ),
            minLines = if (singleLine) 1 else 3
        )
    }
}

/**
 * Read-only date field that opens a DatePicker dialog on click.
 * Free text input is intentionally disabled (T1.3).
 */
@Composable
private fun DateField(
    label  : String,
    value  : String,
    error  : String?,
    onClick: () -> Unit
) {
    val c = AppTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = c.mist)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (error != null) c.rose.copy(alpha = 0.08f) else c.cardSurface)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text  = value.ifEmpty { "Selecciona data" },
                    color = if (value.isEmpty()) c.fog else c.snow,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(Icons.Rounded.DateRange, "Selecciona data", tint = c.violetLight)
            }
        }
        if (error != null) {
            Text(error, color = c.rose, style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp))
        }
    }
}