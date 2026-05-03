package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    val c = AppTheme.colors
    val state by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) onLoginSuccess()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(c.bgBase),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("✈️", fontSize = 64.sp)
            Spacer(Modifier.height(8.dp))
            Text("Marvelous Dreamer", style = MaterialTheme.typography.headlineMedium,
                color = c.snow, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Sign in to your account", style = MaterialTheme.typography.bodyMedium, color = c.fog)
            Spacer(Modifier.height(32.dp))

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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = c.violet, unfocusedBorderColor = c.bgOutline,
                    focusedLabelColor = c.violet, cursorColor = c.violet,
                    focusedTextColor = c.snow, unfocusedTextColor = c.snow
                )
            )
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onNavigateToForgotPassword, modifier = Modifier.align(Alignment.End)) {
                Text("Forgot password?", color = c.violetLight)
            }

            if (state.error != null) {
                Spacer(Modifier.height(8.dp))
                Text(state.error!!, color = c.rose, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.login(email.trim(), password) },
                enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = c.violet)
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = c.snow)
                else Text("Sign In", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToRegister) {
                Text("Don't have an account? ", color = c.fog)
                Text("Sign Up", color = c.emeraldLight, fontWeight = FontWeight.Bold)
            }
        }
    }
}
