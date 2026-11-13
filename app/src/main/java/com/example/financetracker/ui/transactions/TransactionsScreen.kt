package com.example.financetracker.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.SampleData
import com.example.financetracker.data.TxType
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.ui.components.TransactionRow
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Income
import com.example.financetracker.ui.theme.Outline
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

private enum class TxFilter(val label: String, val type: TxType?) {
    ALL("Semua", null),
    INCOME("Pemasukan", TxType.INCOME),
    EXPENSE("Pengeluaran", TxType.EXPENSE),
    TRANSFER("Transfer", TxType.TRANSFER),
}

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    onOpenFilter: () -> Unit,
    onTransactionClick: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(TxFilter.ALL) }

    // Terapkan pencarian + filter jenis
    val groups = SampleData.groups.map { g ->
        g.copy(
            items = g.items.filter { t ->
                (filter.type == null || t.type == filter.type) &&
                    (query.isBlank() || t.title.contains(query, ignoreCase = true))
            },
        )
    }.filter { it.items.isNotEmpty() }

    val allItems = SampleData.groups.flatMap { it.items }
    val totalIncome = allItems.filter { it.type == TxType.INCOME }.sumOf { it.amount }
    val totalExpense = allItems.filter { it.type == TxType.EXPENSE }.sumOf { it.amount }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Transaksi", style = MaterialTheme.typography.headlineMedium, color = Primary)
            Text(
                "Oktober 2026",
                Modifier.background(Surface, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 8.dp),
                color = TextPrimary,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        // Pencarian + tombol filter
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Cari transaksi...", color = TextMuted) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = TextMuted) },
                singleLine = true,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = Outline,
                ),
            )
            Box(
                Modifier.size(56.dp).background(Primary, RoundedCornerShape(16.dp)).clickable(onClick = onOpenFilter),
                contentAlignment = Alignment.Center,
            ) { Icon(Icons.Filled.FilterList, contentDescription = "Filter lanjutan", tint = Color.White) }
        }

        // Chip jenis transaksi
        Row(
            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            TxFilter.entries.forEach { f ->
                val selected = f == filter
                Text(
                    f.label,
                    Modifier
                        .background(if (selected) Primary else Surface, RoundedCornerShape(14.dp))
                        .clickable { filter = f }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    color = if (selected) Color.White else TextSecondary,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        // Ringkasan
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Pemasukan", formatRupiah(totalIncome), Income, Modifier.weight(1f))
            SummaryCard("Pengeluaran", formatRupiah(totalExpense), Expense, Modifier.weight(1f))
        }

        // Daftar per tanggal
        if (groups.isEmpty()) {
            Text("Tidak ada transaksi yang cocok.", color = TextMuted, modifier = Modifier.padding(vertical = 24.dp))
        }
        groups.forEach { g ->
            Column {
                Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(g.label, color = TextSecondary, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                    Text(g.total, color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                }
                Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp))) {
                    g.items.forEach { t -> TransactionRow(t, onClick = { onTransactionClick(t.id) }) }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier.background(Surface, RoundedCornerShape(16.dp)).padding(14.dp)) {
        Text(label, color = TextMuted, style = MaterialTheme.typography.labelSmall)
        Text(value, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}
