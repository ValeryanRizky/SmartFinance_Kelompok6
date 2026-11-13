package com.example.financetracker.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.data.*
import com.example.financetracker.data.repository.FinanceRepository
import com.example.financetracker.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transactionId: String?,
    onBack: () -> Unit,
    onEdit: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val transactions by FinanceRepository.transactions.collectAsState()
    val tx = transactions.find { it.id == transactionId } ?: SampleData.groups.flatMap { it.items }.find { it.id == transactionId }
        ?: Transaction("1", "Makan Siang", "Dompet Digital · Makanan", 45_000, TxType.EXPENSE, "M", "17 Okt 2026")

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Transaksi", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGround),
            )
        },
        containerColor = PrimaryGround,
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Hero Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val (iconBg, iconFg) = when (tx.type) {
                        TxType.INCOME -> Color(0xFFD7EEDD) to Color(0xFF24563F)
                        TxType.EXPENSE -> Color(0xFFFBEADF) to Color(0xFFB3412E)
                        TxType.TRANSFER -> Color(0xFFE2EAF4) to Color(0xFF2F5F96)
                    }

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(iconBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(tx.initial, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = iconFg)
                    }

                    Spacer(Modifier.height(14.dp))
                    Text(tx.title, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                    Spacer(Modifier.height(6.dp))

                    val amountColor = when (tx.type) {
                        TxType.INCOME -> IncomeGreen
                        TxType.EXPENSE -> ExpenseRed
                        TxType.TRANSFER -> TransferBlue
                    }
                    Text(tx.amountLabel(), fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, color = amountColor)

                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color = iconBg,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        val typeLabel = when (tx.type) {
                            TxType.INCOME -> "Pemasukan"
                            TxType.EXPENSE -> "Pengeluaran"
                            TxType.TRANSFER -> "Transfer"
                        }
                        Text(
                            text = typeLabel,
                            color = iconFg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }
            }

            // Info rows card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    val parts = tx.meta.split("·").map { it.trim() }
                    val walletName = parts.getOrNull(0) ?: "Dompet Digital"
                    val categoryName = parts.getOrNull(1) ?: "Makanan"

                    DetailRow(label = "Kategori", value = categoryName)
                    HorizontalDivider(color = CardBorder)
                    DetailRow(label = "Wallet", value = walletName)
                    HorizontalDivider(color = CardBorder)
                    DetailRow(label = "Tanggal", value = if (tx.date.isNotBlank()) tx.date else "17 Okt 2026")
                    HorizontalDivider(color = CardBorder)
                    DetailRow(label = "Catatan", value = "Nasi ayam di kantin kampus")
                }
            }

            // Struk / Receipt Preview
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = ForestLight, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Foto struk", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Text("Struk_pembayaran_17okt.jpg", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Action Buttons
            OutlinedButton(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestDeep),
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Edit Transaksi", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBEADF), contentColor = ExpenseRed),
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Hapus Transaksi", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus transaksi ini?", fontWeight = FontWeight.Bold) },
            text = {
                Text("Saldo wallet akan dikembalikan sebesar ${formatRupiah(tx.amount)}. Tindakan ini tidak bisa dibatalkan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            FinanceRepository.deleteTransaction(tx.id)
                            showDeleteDialog = false
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed),
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, fontSize = 13.sp, color = TextMuted)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

