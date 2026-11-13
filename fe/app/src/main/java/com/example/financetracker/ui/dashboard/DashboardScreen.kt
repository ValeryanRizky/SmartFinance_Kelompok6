package com.example.financetracker.ui.dashboard

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.SampleData
import com.example.financetracker.ui.components.TransactionRow
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Income
import com.example.financetracker.ui.theme.Info
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.PrimaryVariant
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.Warning

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onProfile: () -> Unit,
    onSearch: () -> Unit,
    onAddTransaction: () -> Unit,
    onTransfer: () -> Unit,
    onGoals: () -> Unit,
    onInsight: () -> Unit,
    onSeeAll: () -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                Modifier.size(44.dp).background(Primary, CircleShape).clickable(onClick = onProfile),
                contentAlignment = Alignment.Center,
            ) { Text("F", color = Color.White, fontWeight = FontWeight.Bold) }
            Column(Modifier.weight(1f)) {
                Text("Selamat pagi, Farrel", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Text("Yuk, catat keuanganmu hari ini", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onSearch) { Icon(Icons.Filled.Search, contentDescription = "Cari transaksi", tint = TextPrimary) }
            IconButton(onClick = { /* TODO: notifikasi */ }) { Icon(Icons.Filled.Notifications, contentDescription = "Notifikasi", tint = TextPrimary) }
        }

        // Kartu saldo
        Column(
            Modifier.fillMaxWidth().background(Primary, RoundedCornerShape(28.dp)).padding(20.dp),
        ) {
            Text("Total Saldo", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("Rp 12.480.500", color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(4.dp))
            Text("▲ 12,6% dari bulan lalu", color = Color(0xFFB8D8C2), style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryChip("Pemasukan", "Rp 8,5 jt", Modifier.weight(1f))
                SummaryChip("Pengeluaran", "Rp 3,2 jt", Modifier.weight(1f))
                SummaryChip("Tabungan", "Rp 2,1 jt", Modifier.weight(1f))
            }
        }

        // Aksi cepat
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            QuickAction("Pemasukan", Income, Modifier.weight(1f), onAddTransaction)
            QuickAction("Pengeluaran", Expense, Modifier.weight(1f), onAddTransaction)
            QuickAction("Transfer", Info, Modifier.weight(1f), onTransfer)
            QuickAction("Goals", Warning, Modifier.weight(1f), onGoals)
        }

        // Insight
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2EEDB), RoundedCornerShape(18.dp))
                .clickable(onClick = onInsight)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFF7A6312))
            Column {
                Text("Pengeluaran Makan naik 30%", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                Text("Dibanding bulan lalu. Lihat insight lainnya", color = Color(0xFF7A6312), style = MaterialTheme.typography.labelSmall)
            }
        }

        // Transaksi terbaru
        Column {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Transaksi Terbaru", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                TextButton(onClick = onSeeAll) { Text("Lihat semua", color = PrimaryVariant, fontWeight = FontWeight.SemiBold) }
            }
            Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp))) {
                SampleData.groups.flatMap { it.items }.take(3).forEach { tx ->
                    TransactionRow(tx, onClick = { onTransactionClick(tx.id) }, showDate = true)
                }
            }
        }
    }
}

@Composable
private fun SummaryChip(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(PrimaryVariant, RoundedCornerShape(14.dp)).padding(12.dp)) {
        Text(label, color = Color(0xFFD6E4DA), style = MaterialTheme.typography.labelSmall)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun QuickAction(label: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier
            .background(Surface, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(12.dp).background(color, CircleShape))
        Text(label, color = TextPrimary, style = MaterialTheme.typography.labelSmall)
    }
}
