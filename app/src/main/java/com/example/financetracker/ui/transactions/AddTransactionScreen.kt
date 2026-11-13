package com.example.financetracker.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.financetracker.data.TxType
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Income
import com.example.financetracker.ui.theme.Info
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

private data class Category(val id: String, val initial: String, val label: String, val bg: Color, val fg: Color)

private val expenseCategories = listOf(
    Category("food", "M", "Makan", Color(0xFFFBEADF), Color(0xFFA3501E)),
    Category("trans", "T", "Transpor", Color(0xFFE2EAF4), Color(0xFF2F5F96)),
    Category("shop", "B", "Belanja", Color(0xFFF3E4EE), Color(0xFF8A3C6E)),
    Category("bill", "L", "Tagihan", Color(0xFFF2EEDB), Color(0xFF7A6312)),
    Category("fun", "H", "Hiburan", Color(0xFFE8E4F5), Color(0xFF5442A0)),
    Category("health", "K", "Kesehatan", Color(0xFFE3ECE5), Color(0xFF24563F)),
    Category("edu", "P", "Pendidikan", Color(0xFFE2EAF4), Color(0xFF2F5F96)),
    Category("other", "+", "Lainnya", Color(0xFFEDEBE4), Color(0xFF4F5C54)),
)
private val incomeCategories = listOf(
    Category("salary", "G", "Gaji", Color(0xFFE3ECE5), Color(0xFF24563F)),
    Category("bonus", "B", "Bonus", Color(0xFFE3ECE5), Color(0xFF24563F)),
    Category("side", "U", "Usaha", Color(0xFFE3ECE5), Color(0xFF24563F)),
    Category("gift", "H", "Hadiah", Color(0xFFE3ECE5), Color(0xFF24563F)),
)

private val wallets = listOf("E-Wallet", "Rekening Bank", "Tunai")

@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    var type by remember { mutableStateOf(TxType.EXPENSE) }
    var amount by remember { mutableStateOf(0L) }
    var categoryId by remember { mutableStateOf("food") }
    var wallet by remember { mutableStateOf(wallets.first()) }
    var date by remember { mutableStateOf("Sabtu, 17 Okt 2026") }
    var note by remember { mutableStateOf("") }

    val amountColor = when (type) {
        TxType.INCOME -> Income
        TxType.TRANSFER -> Info
        TxType.EXPENSE -> Expense
    }
    val categories = if (type == TxType.INCOME) incomeCategories else expenseCategories

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding().imePadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary) }
                Text("Tambah Transaksi", style = MaterialTheme.typography.headlineMedium, color = Primary)
            }

            // Tab jenis transaksi
            Row(Modifier.fillMaxWidth().background(Color(0xFFEEECE5), RoundedCornerShape(18.dp)).padding(4.dp)) {
                listOf(
                    TxType.EXPENSE to "Pengeluaran",
                    TxType.INCOME to "Pemasukan",
                    TxType.TRANSFER to "Transfer",
                ).forEach { (t, label) ->
                    val selected = t == type
                    Box(
                        Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(if (selected) Primary else Color.Transparent, RoundedCornerShape(14.dp))
                            .clickable {
                                type = t
                                categoryId = if (t == TxType.INCOME) "salary" else "food"
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            label,
                            color = if (selected) Color.White else TextSecondary,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            // Nominal
            Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp)).padding(20.dp)) {
                Text("Nominal", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text(formatRupiah(amount), color = amountColor, style = MaterialTheme.typography.headlineLarge)
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
                    listOf(10_000L to "+10rb", 50_000L to "+50rb", 100_000L to "+100rb").forEach { (add, label) ->
                        Text(
                            label,
                            Modifier
                                .background(Color(0xFFEEECE5), RoundedCornerShape(12.dp))
                                .clickable { amount += add }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }

            // Kategori (disembunyikan untuk transfer)
            if (type != TxType.TRANSFER) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Kategori", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                    categories.chunked(4).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowItems.forEach { c ->
                                val selected = c.id == categoryId
                                Column(
                                    Modifier
                                        .weight(1f)
                                        .background(Surface, RoundedCornerShape(16.dp))
                                        .border(2.dp, if (selected) Primary else Color.Transparent, RoundedCornerShape(16.dp))
                                        .clickable { categoryId = c.id }
                                        .padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Box(Modifier.size(40.dp).background(c.bg, CircleShape), contentAlignment = Alignment.Center) {
                                        Text(c.initial, color = c.fg, fontWeight = FontWeight.Bold)
                                    }
                                    Text(c.label, color = TextPrimary, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                            repeat(4 - rowItems.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }

            // Wallet
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Wallet", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    wallets.forEach { w ->
                        val selected = w == wallet
                        Text(
                            w,
                            Modifier
                                .background(if (selected) Primary else Surface, RoundedCornerShape(14.dp))
                                .clickable { wallet = w }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            color = if (selected) Color.White else TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

            AppTextField("Tanggal", date, { date = it })
            AppTextField("Catatan", note, { note = it }, placeholder = "Contoh: makan siang di kampus")

            Text(
                "📎 Lampirkan foto struk (opsional)",
                Modifier
                    .fillMaxWidth()
                    .background(Surface, RoundedCornerShape(16.dp))
                    .clickable { /* TODO: pilih foto */ }
                    .padding(16.dp),
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        // Tombol simpan tetap di bawah
        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Simpan Transaksi", onClick = onSave, enabled = amount > 0)
        }
    }
}
