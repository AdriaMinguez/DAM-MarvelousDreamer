package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val state by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onRegisterSuccess()
    }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("Create Account", color = c.snow) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = c.fog)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = c.bgBase)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("✈️", fontSize = 48.sp)
            Spacer(Modifier.height(8.dp))
            Text("Join Marvelous Dreamer", style = MaterialTheme.typography.titleLarge,
                color = c.snow, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.violet, unfocusedBorderColor = c.bgOutline,
                    focusedLabelColor = c.violet, cursorColor = c.violet,
                    focusedTextColor = c.snow, unfocusedTextColor = c.snow
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.violet, unfocusedBorderColor = c.bgOutline,
                    focusedLabelColor = c.violet, cursorColor = c.violet,
                    focusedTextColor = c.snow, unfocusedTextColor = c.snow
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = confirmPassword, onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.violet, unfocusedBorderColor = c.bgOutline,
                    focusedLabelColor = c.violet, cursorColor = c.violet,
                    focusedTextColor = c.snow, unfocusedTextColor = c.snow
                )
            )

            val errorMsg = localError ?: state.error
            if (errorMsg != null) {
                Spacer(Modifier.height(8.dp))
                Text(errorMsg, color = c.rose, style = MaterialTheme.typography.bodySmall)
            }
            if (state.successMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(state.successMessage!!, color = c.emerald, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    localError = null
                    when {
                        email.isBlank() -> localError = "Email is required"
                        password.length < 6 -> localError = "Password must be at least 6 characters"
                        password != confirmPassword -> localError = "Passwords do not match"
                        else -> authViewModel.register(email.trim(), password)
                    }
                },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = c.emerald)
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = c.snow)
                else Text("Create Account", fontWeight = FontWeight.Bold)
            }
        }
    }
}
