package com.example.financetracker.data

import androidx.compose.ui.graphics.Color

enum class WalletType(val label: String, val english: String, val short: String) {
    BANK("Bank", "Bank Account", "BNK"),
    EWALLET("E-Wallet", "E-Wallet", "EW"),
    CASH("Tunai", "Cash", "Rp"),
    CREDIT("Kredit", "Credit Card", "CC"),
    INVESTMENT("Investasi", "Investment", "INV"),
}

data class Wallet(
    val id: String,
    val name: String,
    val type: WalletType,
    val balance: Long,
    val note: String,
    val bg: Color,
    val fg: Color,
)

fun Wallet.balanceLabel(): String =
    if (balance < 0) "− ${formatRupiah(-balance)}" else formatRupiah(balance)

// Data contoh dari prototype. Nanti diganti data dari ViewModel/API.
object SampleWallets {
    val list = listOf(
        Wallet("w1", "Rekening Utama", WalletType.BANK, 9_420_500, "Aktif", Color(0xFF163A2C), Color.White),
        Wallet("w2", "Reksa Dana", WalletType.INVESTMENT, 3_880_000, "▲ 4,2%", Color(0xFF6E9A82), Color.White),
        Wallet("w3", "Dompet Digital", WalletType.EWALLET, 830_000, "Aktif", Color(0xFFE2EAF4), Color(0xFF2F5F96)),
        Wallet("w4", "Uang Tunai", WalletType.CASH, 350_000, "Aktif", Color(0xFFF2EEDB), Color(0xFF7A6312)),
        Wallet("w5", "Kartu Kredit", WalletType.CREDIT, -2_000_000, "Limit Rp 10 jt", Color(0xFFFBEADF), Color(0xFFA3501E)),
    )
}
