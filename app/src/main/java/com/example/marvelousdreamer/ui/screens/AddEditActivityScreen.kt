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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.marvelousdreamer.domain.ActivityType
import com.example.marvelousdreamer.ui.themes.*
import com.example.marvelousdreamer.ui.viewmodel.TripViewModel
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Screen for creating or editing an activity within a trip (T1.2 / T1.3).
 * Uses DatePickerDialog and TimePickerDialog — no free text for date/time.
 *
 * @param tripId     parent trip ID
 * @param activityId null = create mode, non-null = edit mode
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityScreen(
    tripId    : String,
    activityId: String?,
    viewModel : TripViewModel,
    onBack    : () -> Unit,
    onSaved   : () -> Unit
) {
    val form by viewModel.activityForm.collectAsState()
    val c = AppTheme.colors
    val dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFmt = DateTimeFormatter.ofPattern("HH:mm")

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var typeExpanded   by remember { mutableStateOf(false) }

    // ── DatePicker dialog ─────────────────────────────────────────────────────

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = form.date
                ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC).toLocalDate()
                        viewModel.updateActivityForm { copy(date = date, dateError = null) }
                    }
                    showDatePicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel·lar", color = c.fog)
                }
            }
        ) { DatePicker(state = state) }
    }

    // ── TimePicker dialog ─────────────────────────────────────────────────────

    if (showTimePicker) {
        val state = rememberTimePickerState(
            initialHour   = form.time?.hour ?: 9,
            initialMinute = form.time?.minute ?: 0,
            is24Hour      = true
        )
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                viewModel.updateActivityForm {
                    copy(time = LocalTime.of(state.hour, state.minute), timeError = null)
                }
                showTimePicker = false
            }
        ) { TimePicker(state = state) }
    }

    // ── Scaffold ──────────────────────────────────────────────────────────────

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = if (form.isEditing) "Editar activitat" else "Nova activitat",
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
            ActivityFormField(
                label         = "Títol *",
                value         = form.title,
                onValueChange = { viewModel.updateActivityForm { copy(title = it, titleError = null) } },
                placeholder   = "Ex: Senso-ji Temple",
                error         = form.titleError
            )

            // Description
            ActivityFormField(
                label         = "Descripció",
                value         = form.description,
                onValueChange = { viewModel.updateActivityForm { copy(description = it) } },
                placeholder   = "Detalls de l'activitat...",
                singleLine    = false
            )

            // Date — DatePicker, no free text allowed (T1.3)
            ActivityDateField(
                label   = "Data *",
                value   = form.date?.format(dateFmt) ?: "",
                error   = form.dateError,
                onClick = { showDatePicker = true }
            )

            // Time — TimePicker, no free text allowed (T1.3)
            ActivityDateField(
                label   = "Hora *",
                value   = form.time?.format(timeFmt) ?: "",
                error   = form.timeError,
                onClick = { showTimePicker = true },
                icon    = null
            )

            // Location
            ActivityFormField(
                label         = "Lloc",
                value         = form.location,
                onValueChange = { viewModel.updateActivityForm { copy(location = it) } },
                placeholder   = "Ex: Asakusa, Kyoto"
            )

            // Cost
            ActivityFormField(
                label         = "Cost (€)",
                value         = form.cost,
                onValueChange = { viewModel.updateActivityForm { copy(cost = it) } },
                placeholder   = "0.00",
                keyboardType  = KeyboardType.Decimal
            )

            // Type dropdown
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Tipus", style = MaterialTheme.typography.labelLarge, color = c.mist)
                ExposedDropdownMenuBox(
                    expanded         = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        value         = form.type.name,
                        onValueChange = {},
                        readOnly      = true,
                        trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier      = Modifier.fillMaxWidth().menuAnchor(),
                        shape         = RoundedCornerShape(12.dp),
                        colors        = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor   = c.violet,
                            unfocusedBorderColor = c.bgOutline,
                            focusedTextColor     = c.snow,
                            unfocusedTextColor   = c.snow
                        )
                    )
                    ExposedDropdownMenu(
                        expanded         = typeExpanded,
                        onDismissRequest = { typeExpanded = false },
                        containerColor   = c.bgElevated
                    ) {
                        ActivityType.entries.forEach { actType ->
                            DropdownMenuItem(
                                text    = { Text(actType.name, color = if (actType == form.type) c.violetLight else c.snow) },
                                onClick = {
                                    viewModel.updateActivityForm { copy(type = actType) }
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Save button
            Button(
                onClick  = { if (viewModel.saveActivity(tripId)) onSaved() },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = c.violet)
            ) {
                Text(
                    text       = if (form.isEditing) "Desar canvis" else "Afegir activitat",
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
                        form.editingId?.let { viewModel.deleteActivity(tripId, it) }
                        onSaved()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    border   = androidx.compose.foundation.BorderStroke(1.dp, c.rose),
                    colors   = ButtonDefaults.outlinedButtonColors(contentColor = c.rose)
                ) {
                    Text(
                        text     = "Eliminar activitat",
                        style    = MaterialTheme.typography.titleMedium,
                        color    = c.rose,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}

// ── Reusable components ───────────────────────────────────────────────────────

@Composable
private fun ActivityFormField(
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
            value           = value,
            onValueChange   = onValueChange,
            placeholder     = { Text(placeholder, color = c.fog) },
            singleLine      = singleLine,
            isError         = error != null,
            supportingText  = error?.let { { Text(it, color = c.rose) } },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier        = Modifier.fillMaxWidth(),
            shape           = RoundedCornerShape(12.dp),
            colors          = OutlinedTextFieldDefaults.colors(
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
 * Read-only date/time field — opens a picker dialog on click.
 * Free text input is intentionally disabled (T1.3).
 */
@Composable
private fun ActivityDateField(
    label  : String,
    value  : String,
    error  : String?,
    onClick: () -> Unit,
    icon   : Any? = Icons.Rounded.DateRange
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = value.ifEmpty { "Selecciona..." },
                    color = if (value.isEmpty()) c.fog else c.snow,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (icon != null) {
                    Icon(Icons.Rounded.DateRange, null, tint = c.violetLight)
                } else {
                    Text("🕐", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
        if (error != null) {
            Text(error, color = c.rose, style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 4.dp))
        }
    }
}

/**
 * Custom dialog wrapper for Material3 TimePicker.
 * Material3 does not include a TimePickerDialog out of the box.
 */
@Composable
private fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content  : @Composable () -> Unit
) {
    val c = AppTheme.colors
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape         = RoundedCornerShape(24.dp),
            color         = c.bgElevated,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier            = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Selecciona l'hora", style = MaterialTheme.typography.titleMedium, color = c.snow)
                content()
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel·lar", color = c.fog) }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = onConfirm) { Text("OK", color = c.violetLight) }
                }
            }
        }
    }
}