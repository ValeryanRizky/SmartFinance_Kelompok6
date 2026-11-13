package com.example.financetracker.data

import androidx.compose.ui.graphics.Color
import java.util.Locale

enum class GoalStatus(val label: String, val chipBg: Color, val chipFg: Color, val bar: Color) {
    ON_TRACK("● On track", Color(0xFFD7EEDD), Color(0xFF17503A), Color(0xFF24563F)),
    BEHIND("● Sedikit tertinggal", Color(0xFFF7E8C4), Color(0xFF6E4D00), Color(0xFFC9922A)),
    NEEDS_MORE("● Perlu tambah", Color(0xFFF6DDD7), Color(0xFF8A2A1A), Color(0xFFB3412E)),
}

data class Goal(
    val id: String,
    val name: String,
    val initial: String,
    val dateLabel: String,      // contoh: "Apr 2027"
    val saved: Long,
    val target: Long,
    val monthsLeft: Int,
    val status: GoalStatus,
    val bg: Color,
    val fg: Color,
    val projection: String,
)

val Goal.percent: Int get() = if (target == 0L) 0 else ((saved * 100) / target).toInt()
val Goal.remaining: Long get() = (target - saved).coerceAtLeast(0)

// Setoran per bulan, dibulatkan ke 10 ribu terdekat
val Goal.monthlyNeed: Long get() = ((remaining / monthsLeft + 5_000) / 10_000) * 10_000

data class SavingEntry(val date: String, val wallet: String, val amount: Long)

fun formatRupiahShort(n: Long): String =
    if (n >= 1_000_000) "Rp " + String.format(Locale.US, "%.1f", n / 1_000_000.0).replace('.', ',') + " jt"
    else "Rp ${n / 1000}rb"

// Data contoh dari prototype. Nanti diganti data dari ViewModel/API.
object SampleGoals {
    val list = listOf(
        Goal("g1", "Laptop Baru", "L", "Apr 2027", 3_000_000, 12_000_000, 6, GoalStatus.ON_TRACK,
            Color(0xFFE3ECE5), Color(0xFF24563F), "Dengan ritme sekarang, goal tercapai Maret 2027."),
        Goal("g2", "Liburan ke Bali", "B", "Des 2026", 2_400_000, 5_000_000, 2, GoalStatus.BEHIND,
            Color(0xFFE2EAF4), Color(0xFF2F5F96), "Dengan ritme sekarang, goal baru tercapai Januari 2027."),
        Goal("g3", "Dana Pendidikan", "P", "Des 2027", 1_000_000, 20_000_000, 14, GoalStatus.NEEDS_MORE,
            Color(0xFFF2EEDB), Color(0xFF7A6312), "Tambah setoran bulanan agar goal tercapai Des 2027."),
    )

    fun find(id: String?): Goal? = list.find { it.id == id }

    fun historyFor(id: String): List<SavingEntry> = when (id) {
        "g1" -> listOf(
            SavingEntry("10 Okt 2026", "Rekening Utama", 1_500_000),
            SavingEntry("25 Sep 2026", "Rekening Utama", 1_000_000),
            SavingEntry("10 Sep 2026", "Dompet Digital", 200_000),
            SavingEntry("25 Agu 2026", "Rekening Utama", 300_000),
        )
        "g2" -> listOf(
            SavingEntry("5 Okt 2026", "Rekening Utama", 1_400_000),
            SavingEntry("5 Sep 2026", "Dompet Digital", 1_000_000),
        )
        else -> listOf(SavingEntry("1 Okt 2026", "Rekening Utama", 1_000_000))
    }
}
