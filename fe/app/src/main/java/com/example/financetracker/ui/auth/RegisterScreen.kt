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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var agreed by remember { mutableStateOf(false) }

    val passwordsMatch = password == confirm
    val canSubmit = name.isNotBlank() && email.isNotBlank() && password.length >= 8 && passwordsMatch && agreed

    Column(
        Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(Modifier.size(44.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary)
            }
        }

        Column {
            Text("Buat akun baru", style = MaterialTheme.typography.headlineLarge, color = Primary)
            Spacer(Modifier.height(8.dp))
            Text("Mulai atur keuanganmu dalam 1 menit.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        AppTextField("Nama lengkap", name, { name = it }, placeholder = "Nama kamu")
        AppTextField("Email", email, { email = it }, placeholder = "nama@email.com", keyboardType = KeyboardType.Email)
        Column {
            AppTextField("Password", password, { password = it }, isPassword = true)
            Text(
                "Minimal 8 karakter",
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.labelSmall,
                color = if (password.length >= 8) Primary else TextMuted,
            )
        }
        Column {
            AppTextField("Konfirmasi password", confirm, { confirm = it }, isPassword = true)
            if (confirm.isNotEmpty() && !passwordsMatch) {
                Text("Password tidak sama", modifier = Modifier.padding(top = 6.dp), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreed,
                onCheckedChange = { agreed = it },
                colors = CheckboxDefaults.colors(checkedColor = Primary),
            )
            Text("Saya menyetujui Syarat & Ketentuan serta Kebijakan Privasi.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }

        PrimaryButton("Daftar", onClick = onRegister, enabled = canSubmit)

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text("Sudah punya akun?", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onLogin) { Text("Masuk", color = Primary, fontWeight = FontWeight.Bold) }
        }
    }
}
