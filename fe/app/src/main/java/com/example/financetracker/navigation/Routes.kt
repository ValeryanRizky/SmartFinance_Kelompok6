package com.example.financetracker.navigation

object Routes {
    // Auth & onboarding
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Tab utama (bottom nav)
    const val DASHBOARD = "dashboard"
    const val TRANSACTIONS = "transactions"
    const val WALLET = "wallet"
    const val GOALS = "goals"

    // Transaksi
    const val ADD_TRANSACTION = "add_transaction"
    const val TRANSACTION_DETAIL = "transaction_detail/{id}"
    const val FILTER = "filter"

    // Wallet (id opsional: kosong = tambah baru, ada = edit)
    const val TRANSFER = "transfer"
    const val WALLET_FORM = "wallet_form?id={id}"

    // Goals (id opsional pada form: kosong = buat baru, ada = edit)
    const val GOAL_FORM = "goal_form?id={id}"
    const val GOAL_DETAIL = "goal_detail/{id}"
    const val ADD_SAVING = "add_saving/{goalId}"

    // Lainnya
    const val INSIGHT = "insight"
    const val PROFILE = "profile"
    const val CATEGORIES = "categories"

    fun transactionDetail(id: String) = "transaction_detail/$id"
    fun walletForm(id: String? = null) = if (id == null) "wallet_form" else "wallet_form?id=$id"
    fun goalForm(id: String? = null) = if (id == null) "goal_form" else "goal_form?id=$id"
    fun goalDetail(id: String) = "goal_detail/$id"
    fun addSaving(goalId: String) = "add_saving/$goalId"
}
