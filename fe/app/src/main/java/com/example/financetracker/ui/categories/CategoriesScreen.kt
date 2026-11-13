package com.example.financetracker.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.ui.theme.*

data class CategoryItem(
    val id: String,
    val name: String,
    val initial: String,
    val txCount: Int,
    val isExpense: Boolean,
    val colorBg: Color,
    val colorFg: Color,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    var categories by remember {
        mutableStateOf(
            listOf(
                CategoryItem("c1", "Makanan & Minuman", "M", 42, true, Color(0xFFFBEADF), Color(0xFFB3412E)),
                CategoryItem("c2", "Transportasi", "T", 28, true, Color(0xFFE2EAF4), Color(0xFF2F5F96)),
                CategoryItem("c3", "Belanja Bulanan", "B", 15, true, Color(0xFFF2EEDB), Color(0xFF7A6312)),
                CategoryItem("c4", "Tagihan & Utilitas", "U", 9, true, Color(0xFFE3ECE5), Color(0xFF24563F)),
                CategoryItem("c5", "Hiburan & Rekreasi", "H", 12, true, Color(0xFFF6DDD7), Color(0xFF8A2A1A)),
                CategoryItem("c6", "Pendidikan & Buku", "P", 5, true, Color(0xFFD7EEDD), Color(0xFF17503A)),
                CategoryItem("c7", "Kesehatan", "K", 3, true, Color(0xFFEDE7F6), Color(0xFF512DA8)),
                CategoryItem("c8", "Gaji Bulanan", "G", 12, false, Color(0xFFD7EEDD), Color(0xFF17503A)),
                CategoryItem("c9", "Bonus & THR", "B", 2, false, Color(0xFFE3ECE5), Color(0xFF24563F)),
                CategoryItem("c10", "Investasi / Dividen", "I", 6, false, Color(0xFFE2EAF4), Color(0xFF2F5F96)),
                CategoryItem("c11", "Hadiah / Hibah", "H", 3, false, Color(0xFFF2EEDB), Color(0xFF7A6312)),
            )
        )
    }

    val isExpenseSelected = selectedTab == 0
    val filteredCategories = categories.filter { it.isExpense == isExpenseSelected }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kategori Transaksi", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGround),
            )
        },
        containerColor = PrimaryGround,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ForestDeep,
                contentColor = Color.White,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Kategori")
            }
        },
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = ForestDeep,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Pengeluaran", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Pemasukan", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                )
            }

            // Categories List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(filteredCategories, key = { it.id }) { cat ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, CardBorder, RoundedCornerShape(14.dp)),
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(cat.colorBg),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(cat.initial, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = cat.colorFg)
                            }
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(cat.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                                Text("${cat.txCount} transaksi", fontSize = 12.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Kategori Baru", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("Nama Kategori") },
                    placeholder = { Text("misal: Hiburan, Belanja...") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCategoryName.isNotBlank()) {
                            val newCat = CategoryItem(
                                id = "c_" + System.currentTimeMillis(),
                                name = newCategoryName.trim(),
                                initial = newCategoryName.trim().take(1).uppercase(),
                                txCount = 0,
                                isExpense = isExpenseSelected,
                                colorBg = if (isExpenseSelected) Color(0xFFFBEADF) else Color(0xFFD7EEDD),
                                colorFg = if (isExpenseSelected) ExpenseRed else IncomeGreen,
                            )
                            categories = categories + newCat
                            newCategoryName = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestDeep),
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
        )
    }
}

