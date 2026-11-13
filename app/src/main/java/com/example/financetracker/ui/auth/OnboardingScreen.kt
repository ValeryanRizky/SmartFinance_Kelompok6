package com.example.financetracker.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Primary
import kotlinx.coroutines.launch

private val OnPrimaryMuted = Color(0xFFD6E4DA)
private val OnboardingAccent = Color(0xFFB8D8C2)

private data class OnboardingPage(val line1: String, val line2: String, val body: String)

private val pages = listOf(
    OnboardingPage(
        "Atur Uangmu,", "Hidup Lebih Tenang",
        "Catat pemasukan, pantau pengeluaran, dan capai target keuanganmu dalam satu aplikasi.",
    ),
    OnboardingPage(
        "Pantau Setiap", "Rupiah Keluar Masuk",
        "Lihat ke mana uangmu pergi lewat ringkasan dan insight yang mudah dipahami.",
    ),
    OnboardingPage(
        "Capai Targetmu,", "Lebih Cepat",
        "Buat goal tabungan, setor secara berkala, dan lihat progresmu terus bertambah.",
    ),
)

@Composable
fun OnboardingScreen(
    onStart: () -> Unit,
    onLogin: () -> Unit,
    onSkip: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()
    val current = pagerState.currentPage
    val isLast = current == pages.lastIndex

    Column(
        Modifier
            .fillMaxSize()
            .background(Primary)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        // Indikator halaman + Lewati
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("0${current + 1} / 0${pages.size}", color = OnPrimaryMuted, style = MaterialTheme.typography.bodyMedium)
            Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(pages.size) { i ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(if (i == current) Color.White else Color(0xFF3C6B55), RoundedCornerShape(2.dp)),
                    )
                }
            }
            TextButton(onClick = onSkip) { Text("Lewati", color = Color.White) }
        }

        // Halaman yang bisa digeser
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f).fillMaxWidth()) { index ->
            val page = pages[index]
            Column(Modifier.fillMaxSize()) {
                Spacer(Modifier.height(32.dp))
                Text(
                    buildAnnotatedString {
                        append(page.line1 + "\n")
                        withStyle(SpanStyle(color = OnboardingAccent)) { append(page.line2) }
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 40.sp),
                )
                Spacer(Modifier.height(16.dp))
                Text(page.body, color = OnPrimaryMuted, style = MaterialTheme.typography.bodyLarge)

                // Ilustrasi sederhana; nanti bisa diganti gambar per halaman
                Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Box(Modifier.height(150.dp).fillMaxWidth(0.7f).background(Color(0xFF24563F), RoundedCornerShape(26.dp)))
                }
            }
        }

        PrimaryButton(
            text = if (isLast) "Mulai Sekarang" else "Lanjut",
            onClick = {
                if (isLast) onStart()
                else scope.launch { pagerState.animateScrollToPage(current + 1) }
            },
            containerColor = Background,
            contentColor = Primary,
        )
        Row(
            Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Sudah punya akun?", color = OnPrimaryMuted, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onLogin) { Text("Masuk", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    }
}
