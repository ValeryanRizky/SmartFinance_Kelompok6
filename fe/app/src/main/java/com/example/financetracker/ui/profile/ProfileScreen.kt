package com.example.financetracker.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.data.repository.FinanceRepository
import com.example.financetracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onNavigateCategories: () -> Unit = {},
    onNavigateWallets: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    val user by FinanceRepository.user.collectAsState()
    var reminderEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // User Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(ForestDeep),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = user.name.take(1).uppercase(),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(user.name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary)
                    Text(user.email, fontSize = 13.sp, color = TextSecondary)

                    Spacer(Modifier.height(18.dp))
                    HorizontalDivider(color = CardBorder)
                    Spacer(Modifier.height(14.dp))

                    // Quick Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        StatItem(count = user.totalWallets.toString(), label = "Wallet")
                        Box(Modifier.width(1.dp).height(30.dp).background(CardBorder))
                        StatItem(count = user.totalTransactions.toString(), label = "Transaksi")
                        Box(Modifier.width(1.dp).height(30.dp).background(CardBorder))
                        StatItem(count = user.totalGoals.toString(), label = "Goals")
                    }
                }
            }

            // Section: Akun
            Text("AKUN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            ) {
                Column {
                    ProfileMenuRow(icon = Icons.Default.Person, title = "Edit Profil", onClick = {})
                    HorizontalDivider(color = CardBorder)
                    ProfileMenuRow(icon = Icons.Default.Category, title = "Kategori Transaksi", onClick = onNavigateCategories)
                    HorizontalDivider(color = CardBorder)
                    ProfileMenuRow(icon = Icons.Default.AccountBalanceWallet, title = "Kelola Wallet", onClick = onNavigateWallets)
                    HorizontalDivider(color = CardBorder)
                    ProfileMenuRow(icon = Icons.Default.Lock, title = "Ubah Password", onClick = {})
                }
            }

            // Section: Preferensi
            Text("PREFERENSI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextMuted, letterSpacing = 1.sp)
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text("Pengingat Harian", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                            Text("Setiap hari pukul 20.00", fontSize = 12.sp, color = TextMuted)
                        }
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { reminderEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ForestDeep),
                        )
                    }
                    HorizontalDivider(color = CardBorder)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Mode Gelap", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                        Switch(
                            checked = darkModeEnabled,
                            onCheckedChange = { darkModeEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ForestDeep),
                        )
                    }
                    HorizontalDivider(color = CardBorder)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text("Mata Uang", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary)
                        Text("IDR (Rp)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ForestDeep)
                    }
                }
            }

            // Logout Button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFBEADF), contentColor = ExpenseRed),
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Keluar", fontWeight = FontWeight.Bold)
            }

            // App version info
            Text(
                "SmartFinance v1.0.0 (Kelompok 6)",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                color = TextMuted,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Keluar dari akun?", fontWeight = FontWeight.Bold) },
            text = { Text("Data kamu tetap tersimpan. Kamu bisa masuk lagi kapan saja.") },
            confirmButton = {
                Button(
                    onClick = {
                        FinanceRepository.logout()
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ExpenseRed),
                ) {
                    Text("Ya, Keluar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
        )
    }
}

@Composable
private fun StatItem(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = TextPrimary)
        Text(label, fontSize = 12.sp, color = TextMuted)
    }
}

@Composable
private fun ProfileMenuRow(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = ForestLight, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(14.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextPrimary, modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
    }
}

