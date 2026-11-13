package com.example.financetracker.ui.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.financetracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightScreen(
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Pengeluaran", "Pemasukan", "Tabungan")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Insight", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = ForestDeep,
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
                    )
                }
            }

            // Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = ForestDeep),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Pengeluaran Oktober (17 hari)", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                    Spacer(Modifier.height(6.dp))
                    Text("Rp 3.210.000", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(4.dp))
                    Text("Rata-rata Rp 188.800 / hari", color = Color(0xFFD4A373), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }

            // Category breakdown
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp)),
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Alokasi Pengeluaran", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)

                    CategoryProgress(name = "Belanja", percent = 39, amount = "Rp 1.250.000", color = Color(0xFF2F5F96))
                    CategoryProgress(name = "Makanan & Minuman", percent = 28, amount = "Rp 900.000", color = Color(0xFFB3412E))
                    CategoryProgress(name = "Tagihan & Utilitas", percent = 15, amount = "Rp 480.000", color = Color(0xFFC9922A))
                    CategoryProgress(name = "Transportasi", percent = 10, amount = "Rp 320.000", color = Color(0xFF24563F))
                    CategoryProgress(name = "Lainnya", percent = 8, amount = "Rp 260.000", color = Color(0xFF8E8E93))
                }
            }

            // Smart Insight Cards
            Text("Insight Untukmu", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = TextPrimary)

            InsightCard(
                icon = Icons.Default.TrendingUp,
                iconColor = ExpenseRed,
                iconBg = Color(0xFFFBEADF),
                title = "Pengeluaran Makanan Naik 30%",
                desc = "Dibandingkan bulan lalu, kamu lebih sering memesan makanan lewat ojek online. Coba kurangi frekuensi jajan untuk hemat hingga Rp 300rb.",
            )

            InsightCard(
                icon = Icons.Default.Lightbulb,
                iconColor = WarningAmber,
                iconBg = Color(0xFFF7E8C4),
                title = "Peluang Menabung",
                desc = "Kamu memiliki surplus cash flow Rp 1,5 jt bulan ini. Sangat baik jika dialokasikan langsung ke Goal 'Laptop Baru'.",
            )

            InsightCard(
                icon = Icons.Default.AutoAwesome,
                iconColor = IncomeGreen,
                iconBg = Color(0xFFD7EEDD),
                title = "Target Dana Darurat On Track",
                desc = "Progres tabunganmu sudah mencapai 50%. Pertahankan ritme ini untuk selesai sesuai jadwal.",
            )

            // 6-Month Trend Chart
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(18.dp)),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Tren Pengeluaran 6 Bulan", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                    Spacer(Modifier.height(18.dp))

                    val months = listOf("Mei" to 0.45f, "Jun" to 0.6f, "Jul" to 0.5f, "Agu" to 0.75f, "Sep" to 0.65f, "Okt" to 0.85f)
                    Row(
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        months.forEach { (m, ratio) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height((100 * ratio).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (m == "Okt") ForestDeep else Color(0xFFD4E0D9)),
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(m, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryProgress(name: String, percent: Int, amount: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text("$amount ($percent%)", fontSize = 12.sp, color = TextMuted)
        }
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = Color(0xFFECEAE3),
        )
    }
}

@Composable
private fun InsightCard(
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    title: String,
    desc: String,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Spacer(Modifier.height(4.dp))
                Text(desc, fontSize = 12.sp, color = TextSecondary, lineHeight = 17.sp)
            }
        }
    }
}

