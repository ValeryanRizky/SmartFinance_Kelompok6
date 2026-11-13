package com.example.financetracker.data

import java.util.Locale

enum class TxType { INCOME, EXPENSE, TRANSFER }

data class Transaction(
    val id: String,
    val title: String,
    val meta: String,
    val amount: Long,
    val type: TxType,
    val initial: String,
    val date: String = "",
)

data class TxGroup(
    val label: String,
    val total: String,
    val items: List<Transaction>,
)

fun formatRupiah(value: Long): String =
    "Rp " + String.format(Locale.US, "%,d", value).replace(',', '.')

fun Transaction.amountLabel(): String = when (type) {
    TxType.INCOME -> "+ ${formatRupiah(amount)}"
    TxType.EXPENSE -> "− ${formatRupiah(amount)}"
    TxType.TRANSFER -> formatRupiah(amount)
}

// Data contoh dari prototype. Nanti diganti data dari ViewModel/API.
object SampleData {
    val groups = listOf(
        TxGroup(
            "Hari ini · 17 Okt", "+ Rp 8.432.000",
            listOf(
                Transaction("1", "Gaji Oktober", "Rekening Bank · Gaji", 8_500_000, TxType.INCOME, "G", "17 Okt"),
                Transaction("2", "Makan Siang", "E-Wallet · Makanan", 45_000, TxType.EXPENSE, "M", "17 Okt"),
                Transaction("3", "Ojek Online", "E-Wallet · Transportasi", 23_000, TxType.EXPENSE, "T", "17 Okt"),
            ),
        ),
        TxGroup(
            "Kemarin · 16 Okt", "− Rp 1.250.000",
            listOf(
                Transaction("4", "Top Up E-Wallet", "Bank → E-Wallet · Transfer", 500_000, TxType.TRANSFER, "Tf", "16 Okt"),
                Transaction("5", "Belanja Bulanan", "Kartu Kredit · Belanja", 1_250_000, TxType.EXPENSE, "B", "16 Okt"),
            ),
        ),
        TxGroup(
            "15 Okt", "− Rp 186.000",
            listOf(
                Transaction("6", "Tagihan Internet", "Rekening Bank · Tagihan", 150_000, TxType.EXPENSE, "L", "15 Okt"),
                Transaction("7", "Kopi", "Tunai · Makanan", 36_000, TxType.EXPENSE, "K", "15 Okt"),
            ),
        ),
    )
}
