package com.example.financetracker.ui.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterTransactionScreen(
    onBack: () -> Unit,
    onApply: () -> Unit = onBack,
) {
    var selectedType by remember { mutableStateOf("Semua") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var selectedWallet by remember { mutableStateOf("Semua") }
    var minAmount by remember { mutableStateOf("") }
    var maxAmount by remember { mutableStateOf("") }

    val types = listOf("Semua", "Pemasukan", "Pengeluaran", "Transfer")
    val categories = listOf("Semua", "Makanan", "Transportasi", "Belanja", "Tagihan", "Hiburan", "Gaji", "Pendidikan")
    val wallets = listOf("Semua", "Rekening Utama", "Dompet Digital", "Uang Tunai", "Investasi")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filter Transaksi", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGround),
            )
        },
        containerColor = PrimaryGround,
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedButton(
                        onClick = {
                            selectedType = "Semua"
                            selectedCategory = "Semua"
                            selectedWallet = "Semua"
                            minAmount = ""
                            maxAmount = ""
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Reset", color = TextSecondary, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onApply,
                        modifier = Modifier.weight(1.5f).height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestDeep),
                    ) {
                        Text("Terapkan", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp),
        ) {
            // Jenis Transaksi
            FilterSection(title = "Jenis Transaksi") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    types.forEach { t ->
                        FilterChipItem(label = t, selected = selectedType == t, onClick = { selectedType = t })
                    }
                }
            }

            // Kategori
            FilterSection(title = "Kategori") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    categories.forEach { c ->
                        FilterChipItem(label = c, selected = selectedCategory == c, onClick = { selectedCategory = c })
                    }
                }
            }

            // Wallet
            FilterSection(title = "Wallet") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    wallets.forEach { w ->
                        FilterChipItem(label = w, selected = selectedWallet == w, onClick = { selectedWallet = w })
                    }
                }
            }

            // Rentang Nominal
            FilterSection(title = "Rentang Nominal") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = minAmount,
                        onValueChange = { minAmount = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Minimum (Rp)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = maxAmount,
                        onValueChange = { maxAmount = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Maksimum (Rp)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
        content()
    }
}

@Composable
private fun FilterChipItem(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) ForestDeep else Color.White
    val fg = if (selected) Color.White else TextPrimary
    val border = if (selected) ForestDeep else CardBorder

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, border, RoundedCornerShape(10.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(label, color = fg, fontSize = 13.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium)
    }
}

