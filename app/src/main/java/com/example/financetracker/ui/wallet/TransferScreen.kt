package com.example.financetracker.ui.wallet

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.financetracker.data.SampleWallets
import com.example.financetracker.data.Wallet
import com.example.financetracker.data.balanceLabel
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Info
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun TransferScreen(
    onBack: () -> Unit,
    onTransfer: () -> Unit,
) {
    val wallets = SampleWallets.list
    var from by remember { mutableStateOf(wallets[0]) }
    var to by remember { mutableStateOf(wallets[2]) }
    var amount by remember { mutableStateOf(0L) }
    var fee by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("Sabtu, 17 Okt 2026") }
    var note by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding().imePadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary) }
                Text("Transfer", style = MaterialTheme.typography.headlineMedium, color = Primary)
            }

            // Dari / Ke
            WalletSelector("Dari", from, wallets) { picked ->
                if (picked.id == to.id) to = from
                from = picked
            }
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    Modifier.size(44.dp).background(Primary, CircleShape).clickable { val t = from; from = to; to = t },
                    contentAlignment = Alignment.Center,
                ) { Icon(Icons.Filled.SwapVert, contentDescription = "Tukar wallet asal dan tujuan", tint = Color.White) }
            }
            WalletSelector("Ke", to, wallets) { picked ->
                if (picked.id == from.id) from = to
                to = picked
            }

            // Jumlah
            Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp)).padding(20.dp)) {
                Text("Jumlah transfer", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(formatRupiah(amount), color = Info, style = MaterialTheme.typography.headlineLarge)
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
                    listOf(100_000L to "100rb", 500_000L to "500rb", 1_000_000L to "1 jt").forEach { (v, label) ->
                        Text(
                            label,
                            Modifier
                                .background(Color(0xFFEEECE5), RoundedCornerShape(12.dp))
                                .clickable { amount = v }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            AppTextField("Biaya admin", fee, { fee = it.filter(Char::isDigit) }, placeholder = "0", keyboardType = KeyboardType.Number)
            AppTextField("Tanggal", date, { date = it })
            AppTextField("Catatan", note, { note = it }, placeholder = "Contoh: top up")

            Text(
                "Transfer tidak dihitung sebagai pemasukan atau pengeluaran, jadi total di dashboard tetap akurat.",
                Modifier.fillMaxWidth().background(Color(0xFFE2EAF4), RoundedCornerShape(16.dp)).padding(16.dp),
                color = Info,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Transfer Sekarang", onClick = onTransfer, enabled = amount > 0 && from.id != to.id)
        }
    }
}

@Composable
private fun WalletSelector(
    role: String,
    wallet: Wallet,
    options: List<Wallet>,
    onSelect: (Wallet) -> Unit,
) {
    var open by remember { mutableStateOf(false) }
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Surface, RoundedCornerShape(18.dp))
                .clickable { open = true }
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(Modifier.size(44.dp).background(wallet.bg, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Text(wallet.type.short, color = wallet.fg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            }
            Column(Modifier.weight(1f)) {
                Text(role, color = TextMuted, style = MaterialTheme.typography.labelSmall)
                Text(wallet.name, fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Text("Saldo ${wallet.balanceLabel()}", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
            }
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = TextMuted)
        }
        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            options.forEach { w ->
                DropdownMenuItem(
                    text = { Text("${w.name} · ${w.balanceLabel()}") },
                    onClick = { onSelect(w); open = false },
                )
            }
        }
    }
}
