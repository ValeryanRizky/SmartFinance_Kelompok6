package com.example.financetracker.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.financetracker.data.SampleWallets
import com.example.financetracker.data.WalletType
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Outline
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

private val cardColors = listOf(
    "Hijau tua" to Color(0xFF163A2C),
    "Hijau" to Color(0xFF3C6B55),
    "Biru" to Color(0xFF2F5F96),
    "Ungu" to Color(0xFF5442A0),
    "Merah bata" to Color(0xFFA3501E),
)

@Composable
fun WalletFormScreen(
    walletId: String?,          // null = tambah baru
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    val existing = remember(walletId) { SampleWallets.list.find { it.id == walletId } }
    val isEdit = existing != null

    var type by remember { mutableStateOf(existing?.type ?: WalletType.BANK) }
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var balance by remember { mutableStateOf(existing?.balance?.toString() ?: "") }
    var includeInTotal by remember { mutableStateOf(true) }
    var color by remember { mutableStateOf(existing?.bg?.takeIf { c -> cardColors.any { it.second == c } } ?: cardColors.first().second) }

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding().imePadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary) }
                Text(if (isEdit) "Edit Wallet" else "Tambah Wallet", style = MaterialTheme.typography.headlineMedium, color = Primary)
            }

            // Pratinjau kartu
            Column(Modifier.fillMaxWidth().background(color, RoundedCornerShape(24.dp)).padding(20.dp)) {
                Text(type.english, color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(24.dp))
                Text(name.ifBlank { "Nama wallet" }, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Text(formatRupiah(balance.toLongOrNull() ?: 0L), color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }

            // Jenis wallet
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Jenis wallet", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                WalletType.entries.chunked(3).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowItems.forEach { t ->
                            val selected = t == type
                            Box(
                                Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .background(if (selected) Primary else Surface, RoundedCornerShape(14.dp))
                                    .border(1.dp, if (selected) Primary else Outline, RoundedCornerShape(14.dp))
                                    .clickable { type = t },
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    t.label,
                                    color = if (selected) Color.White else TextPrimary,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                        repeat(3 - rowItems.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }

            AppTextField("Nama wallet", name, { name = it }, placeholder = "Contoh: Rekening Utama")
            AppTextField(
                "Saldo awal", balance, { balance = it.filter { c -> c.isDigit() || c == '-' }.take(13) },
                placeholder = "0", keyboardType = KeyboardType.Number,
            )

            // Hitung ke total saldo
            Row(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(16.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Hitung ke Total Saldo", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text("Matikan untuk kartu kredit jika perlu", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                }
                Switch(
                    checked = includeInTotal,
                    onCheckedChange = { includeInTotal = it },
                    colors = SwitchDefaults.colors(checkedTrackColor = Primary),
                )
            }

            // Warna kartu
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Warna kartu", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    cardColors.forEach { (label, c) ->
                        val selected = c == color
                        Box(
                            Modifier
                                .size(44.dp)
                                .border(if (selected) 2.dp else 1.dp, if (selected) TextPrimary else Outline, CircleShape)
                                .padding(4.dp)
                                .background(c, CircleShape)
                                .clickable { color = c },
                        )
                    }
                }
            }

            if (isEdit) {
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) {
                    Text("Hapus wallet ini", color = Expense, fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Simpan Wallet", onClick = onSave, enabled = name.isNotBlank())
        }
    }
}
