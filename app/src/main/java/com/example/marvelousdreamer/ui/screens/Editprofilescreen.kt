package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.marvelousdreamer.data.local.entity.UserEntity
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel

/**
 * Edit Profile screen (T4.1).
 * Fields: username, birthdate, address, country, phone, acceptEmails.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val authState by authViewModel.authState.collectAsState()

    var username by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var acceptEmails by remember { mutableStateOf(false) }
    var isLoaded by remember { mutableStateOf(false) }
    var saveSuccess by remember { mutableStateOf(false) }
    var localError by remember { mutableStateOf<String?>(null) }

    val userId = authViewModel.currentUserId ?: ""
    val userEmail = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email ?: ""

    // Load existing user data
    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            val user = authViewModel.getLocalUser()
            if (user != null) {
                username = user.username
                birthdate = if (user.birthdate != null) {
                    java.time.Instant.ofEpochMilli(user.birthdate)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate().toString()
                } else ""
                address = user.address
                country = user.country
                phone = user.phone
                acceptEmails = user.acceptEmails
            }
            isLoaded = true
        }
    }

    // DatePicker state
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        birthdate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }
                    showDatePicker = false
                }) { Text("OK", color = c.violetLight) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = c.fog)
                }
            }
        ) { DatePicker(state = state) }
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = c.snow, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { padding ->
        if (!isLoaded) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = c.violet)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Spacer(Modifier.height(8.dp))

                // Email (read-only)
                ProfileField(label = "Email", value = userEmail, readOnly = true)

                // Username
                ProfileField(
                    label = "Username *",
                    value = username,
                    onValueChange = { username = it }
                )

                // Birthdate (DatePicker, read-only field)
                ProfileDateField(
                    label = "Date of birth",
                    value = birthdate,
                    onClick = { showDatePicker = true }
                )

                // Address
                ProfileField(
                    label = "Address",
                    value = address,
                    onValueChange = { address = it }
                )

                // Country
                ProfileField(
                    label = "Country",
                    value = country,
                    onValueChange = { country = it }
                )

                // Phone
                ProfileField(
                    label = "Phone number",
                    value = phone,
                    onValueChange = { phone = it },
                    keyboardType = KeyboardType.Phone
                )

                // Accept emails
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(c.cardSurface, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Accept promotional emails", style = MaterialTheme.typography.bodyMedium, color = c.snow)
                    Switch(
                        checked = acceptEmails,
                        onCheckedChange = { acceptEmails = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = c.snow,
                            checkedTrackColor = c.emerald,
                            uncheckedTrackColor = c.bgOutline
                        )
                    )
                }

                // Errors
                val errorMsg = localError ?: authState.error
                if (errorMsg != null) {
                    Text(errorMsg, color = c.rose, style = MaterialTheme.typography.bodySmall)
                }
                if (saveSuccess) {
                    Text("Profile saved!", color = c.emerald, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.height(8.dp))

                // Save button
                Button(
                    onClick = {
                        localError = null
                        saveSuccess = false
                        if (username.isBlank()) {
                            localError = "Username is required"
                            return@Button
                        }
                        val birthdateMillis = try {
                            if (birthdate.isNotBlank()) {
                                val parts = birthdate.split("/")
                                if (parts.size == 3) {
                                    java.time.LocalDate.of(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
                                        .atStartOfDay(java.time.ZoneId.systemDefault())
                                        .toInstant().toEpochMilli()
                                } else null
                            } else null
                        } catch (e: Exception) { null }

                        authViewModel.saveLocalUser(
                            UserEntity(
                                id = userId,
                                login = userEmail,
                                username = username.trim(),
                                birthdate = birthdateMillis,
                                address = address.trim(),
                                country = country.trim(),
                                phone = phone.trim(),
                                acceptEmails = acceptEmails
                            )
                        )
                        saveSuccess = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = c.violet)
                ) {
                    Text("Save Profile", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val c = AppTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = c.mist)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = c.violet,
                unfocusedBorderColor = c.bgOutline,
                focusedTextColor = c.snow,
                unfocusedTextColor = if (readOnly) c.fog else c.snow,
                cursorColor = c.violet
            )
        )
    }
}

@Composable
private fun ProfileDateField(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    val c = AppTheme.colors
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = c.mist)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(c.cardSurface, RoundedCornerShape(12.dp))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            Text(
                text = value.ifEmpty { "Select date" },
                style = MaterialTheme.typography.bodyLarge,
                color = if (value.isEmpty()) c.fog else c.snow
            )
        }
    }
}