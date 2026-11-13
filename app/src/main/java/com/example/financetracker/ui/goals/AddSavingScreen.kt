package com.example.financetracker.ui.goals

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.financetracker.data.SampleGoals
import com.example.financetracker.data.SampleWallets
import com.example.financetracker.data.WalletType
import com.example.financetracker.data.balanceLabel
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.data.percent
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.components.ProgressBar
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Outline
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun AddSavingScreen(
    goalId: String?,
    onClose: () -> Unit,
    onSave: () -> Unit,
) {
    val goal = SampleGoals.find(goalId)
    if (goal == null) {
        Box(Modifier.fillMaxSize().background(Background), contentAlignment = Alignment.Center) { Text("Goal tidak ditemukan") }
        return
    }
    // Kartu kredit tidak dipakai sebagai sumber tabungan
    val wallets = remember { SampleWallets.list.filter { it.type != WalletType.CREDIT } }

    var amount by remember { mutableStateOf(500_000L) }
    var walletId by remember { mutableStateOf(wallets.first().id) }

    val newSaved = goal.saved + amount
    val newPct = if (goal.target == 0L) 0 else ((newSaved * 100) / goal.target).toInt().coerceAtMost(100)

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding().imePadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f).padding(start = 4.dp)) {
                    Text("Tambah Tabungan", style = MaterialTheme.typography.headlineMedium, color = Primary)
                    Text("ke goal ${goal.name}", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                }
                IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = "Tutup", tint = Primary) }
            }

            // Nominal
            Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp)).padding(20.dp)) {
                Text(formatRupiah(amount), color = Primary, style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(12.dp))
                AppTextField(
                    label = "Ketik nominal",
                    value = if (amount == 0L) "" else amount.toString(),
                    onValueChange = { amount = it.filter(Char::isDigit).take(12).toLongOrNull() ?: 0L },
                    placeholder = "0",
                    keyboardType = KeyboardType.Number,
                )
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(100_000L to "100rb", 250_000L to "250rb", 500_000L to "500rb", 1_500_000L to "1,5 jt").forEach { (v, label) ->
                        val selected = v == amount
                        Text(
                            label,
                            Modifier
                                .background(if (selected) Primary else Surface, RoundedCornerShape(19.dp))
                                .border(1.dp, if (selected) Primary else Outline, RoundedCornerShape(19.dp))
                                .clickable { amount = v }
                                .padding(horizontal = 14.dp, vertical = 9.dp),
                            color = if (selected) Color.White else TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            // Wallet sumber
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ambil dari wallet", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                wallets.forEach { w ->
                    val selected = w.id == walletId
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(Surface, RoundedCornerShape(16.dp))
                            .border(2.dp, if (selected) Primary else Color(0xFFEEECE5), RoundedCornerShape(16.dp))
                            .clickable { walletId = w.id }
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(w.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                            Text("Saldo ${w.balanceLabel()}", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                        }
                        Box(
                            Modifier
                                .size(20.dp)
                                .border(if (selected) 6.dp else 2.dp, if (selected) Primary else Color(0xFFB9B5A8), CircleShape),
                        )
                    }
                }
            }

            // Progres setelah ini
            Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Progres setelah ini", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text("$newPct%", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                }
                ProgressBar(
                    fraction = goal.percent / 100f,
                    color = Primary,
                    previewFraction = newPct / 100f,
                )
            }
        }

        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Simpan Tabungan", onClick = onSave, enabled = amount > 0)
        }
    }
}
