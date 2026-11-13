package com.example.financetracker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.OutlinedAppButton
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.PrimaryVariant
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onBack: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(Background).verticalScroll(rememberScrollState())) {
        // Header hijau
        Column(
            Modifier
                .fillMaxWidth()
                .background(Primary, RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 40.dp),
        ) {
            Box(Modifier.size(44.dp).background(PrimaryVariant, CircleShape), contentAlignment = Alignment.Center) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Selamat datang kembali", color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text("Masuk untuk melanjutkan mencatat keuanganmu.", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
        }

        // Form
        Column(Modifier.padding(horizontal = 24.dp, vertical = 28.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            AppTextField("Email", email, { email = it }, placeholder = "nama@email.com", keyboardType = KeyboardType.Email)
            AppTextField("Password", password, { password = it }, isPassword = true)

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { /* TODO: lupa password */ }) {
                    Text("Lupa password?", color = PrimaryVariant, fontWeight = FontWeight.SemiBold)
                }
            }

            PrimaryButton("Masuk", onClick = onLogin)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFD9D6CC)))
                Text("atau", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFD9D6CC)))
            }

            OutlinedAppButton("Masuk dengan akun Google", onClick = { /* TODO: Google sign-in */ })

            Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Belum punya akun?", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onRegister) { Text("Daftar", color = Primary, fontWeight = FontWeight.Bold) }
            }
        }
    }
}
