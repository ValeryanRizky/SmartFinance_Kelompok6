package com.example.financetracker.ui.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.SampleWallets
import com.example.financetracker.data.WalletType
import com.example.financetracker.data.balanceLabel
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary

private data class Segment(val label: String, val value: Long, val color: Color)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onTransfer: () -> Unit,
    onWalletClick: (String) -> Unit,
) {
    val wallets = SampleWallets.list
    val total = wallets.sumOf { it.balance }

    // Komposisi hanya dari wallet bersaldo positif
    val segments = listOf(
        Segment("Bank", wallets.filter { it.type == WalletType.BANK && it.balance > 0 }.sumOf { it.balance }, Color(0xFFB8D8C2)),
        Segment("Investasi", wallets.filter { it.type == WalletType.INVESTMENT && it.balance > 0 }.sumOf { it.balance }, Color(0xFF6E9A82)),
        Segment("E-Wallet", wallets.filter { it.type == WalletType.EWALLET && it.balance > 0 }.sumOf { it.balance }, Color(0xFF8DB4E2)),
        Segment("Tunai", wallets.filter { it.type == WalletType.CASH && it.balance > 0 }.sumOf { it.balance }, Color(0xFFE0C38A)),
    ).filter { it.value > 0 }
    val positiveTotal = segments.sumOf { it.value }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Wallet", style = MaterialTheme.typography.headlineMedium, color = Primary)
            Row(
                Modifier.background(Primary, RoundedCornerShape(14.dp)).clickable(onClick = onAdd).padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text("Tambah", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Kartu total + komposisi
        Column(Modifier.fillMaxWidth().background(Primary, RoundedCornerShape(28.dp)).padding(20.dp)) {
            Text("Total saldo semua wallet", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text(formatRupiah(total), color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth().height(10.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                segments.forEach { s ->
                    Box(Modifier.weight(s.value.toFloat()).height(10.dp).background(s.color, RoundedCornerShape(5.dp)))
                }
            }
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                segments.forEach { s ->
                    val pct = Math.round(s.value * 100f / positiveTotal)
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = s.color)) { append("■ ") }
                            append("${s.label} $pct%")
                        },
                        color = Color(0xFFD6E4DA),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF24563F), RoundedCornerShape(16.dp))
                    .clickable(onClick = onTransfer)
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = Color.White)
                Spacer(Modifier.size(8.dp))
                Text("Transfer Antar Wallet", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Daftar wallet
        Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp))) {
            wallets.forEach { w ->
                Row(
                    Modifier.fillMaxWidth().clickable { onWalletClick(w.id) }.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(Modifier.size(48.dp).background(w.bg, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                        Text(w.type.short, color = w.fg, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                    }
                    Column(Modifier.weight(1f)) {
                        Text(w.name, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                        Text(w.type.english, color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            w.balanceLabel(),
                            color = if (w.balance < 0) Expense else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(w.note, color = TextMuted, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}
