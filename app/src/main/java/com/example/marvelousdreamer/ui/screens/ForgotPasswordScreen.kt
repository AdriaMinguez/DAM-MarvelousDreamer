package com.example.marvelousdreamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marvelousdreamer.ui.themes.AppTheme
import com.example.marvelousdreamer.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit
) {
    val c = AppTheme.colors
    val state by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }

    Scaffold(
        containerColor = c.bgBase,
        topBar = {
            TopAppBar(
                title = { Text("Recover Password", color = c.snow) },
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
            modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🔑", fontSize = 48.sp)
            Spacer(Modifier.height(8.dp))
            Text("Enter your email to receive a recovery link",
                style = MaterialTheme.typography.bodyMedium, color = c.fog)
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

            if (state.error != null) {
                Spacer(Modifier.height(8.dp))
                Text(state.error!!, color = c.rose, style = MaterialTheme.typography.bodySmall)
            }
            if (state.successMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(state.successMessage!!, color = c.emerald, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { authViewModel.sendPasswordReset(email.trim()) },
                enabled = !state.isLoading && email.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = c.violet)
            ) {
                if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = c.snow)
                else Text("Send Recovery Email", fontWeight = FontWeight.Bold)
            }
        }
    }
}
