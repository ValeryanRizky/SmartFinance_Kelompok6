package com.example.financetracker.data.repository

import androidx.compose.ui.graphics.Color
import com.example.financetracker.data.*
import com.example.financetracker.data.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object FinanceRepository {

    // --- State In-Memory (Cached & Reactive) ---
    private val _user = MutableStateFlow(
        UserDto(
            id = "u1",
            name = "Farrel",
            email = "farrel@email.com",
            totalWallets = 5,
            totalTransactions = 128,
            totalGoals = 3,
        )
    )
    val user: StateFlow<UserDto> = _user.asStateFlow()

    private val _wallets = MutableStateFlow<List<Wallet>>(SampleWallets.list)
    val wallets: StateFlow<List<Wallet>> = _wallets.asStateFlow()

    private val _goals = MutableStateFlow<List<Goal>>(SampleGoals.list)
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(
        SampleData.groups.flatMap { it.items }
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // --- Authentication ---
    suspend fun login(email: String, pass: String): Result<UserDto> {
        return try {
            val response = ApiClient.service.login(LoginRequest(email, pass))
            if (response.isSuccessful && response.body()?.data != null) {
                val auth = response.body()!!.data!!
                ApiConfig.authToken = auth.token
                _user.value = auth.user
                Result.success(auth.user)
            } else if (ApiConfig.USE_MOCK_FALLBACK) {
                val mockUser = UserDto("u1", "Farrel", email, null, _wallets.value.size, _transactions.value.size, _goals.value.size)
                _user.value = mockUser
                Result.success(mockUser)
            } else {
                Result.failure(Exception(response.message() ?: "Login failed"))
            }
        } catch (e: Exception) {
            if (ApiConfig.USE_MOCK_FALLBACK) {
                val mockUser = UserDto("u1", "Farrel", email, null, _wallets.value.size, _transactions.value.size, _goals.value.size)
                _user.value = mockUser
                Result.success(mockUser)
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun register(name: String, email: String, pass: String): Result<UserDto> {
        return try {
            val response = ApiClient.service.register(RegisterRequest(name, email, pass))
            if (response.isSuccessful && response.body()?.data != null) {
                val auth = response.body()!!.data!!
                ApiConfig.authToken = auth.token
                _user.value = auth.user
                Result.success(auth.user)
            } else if (ApiConfig.USE_MOCK_FALLBACK) {
                val mockUser = UserDto("u2", name, email, null, _wallets.value.size, _transactions.value.size, _goals.value.size)
                _user.value = mockUser
                Result.success(mockUser)
            } else {
                Result.failure(Exception(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            if (ApiConfig.USE_MOCK_FALLBACK) {
                val mockUser = UserDto("u2", name, email, null, _wallets.value.size, _transactions.value.size, _goals.value.size)
                _user.value = mockUser
                Result.success(mockUser)
            } else {
                Result.failure(e)
            }
        }
    }

    fun logout() {
        ApiConfig.authToken = null
    }

    // --- Transactions ---
    suspend fun addTransaction(
        title: String,
        amount: Long,
        type: TxType,
        category: String,
        walletName: String,
        date: String,
        notes: String = "",
    ): Transaction {
        val newTx = Transaction(
            id = UUID.randomUUID().toString(),
            title = title,
            meta = "$walletName · $category",
            amount = amount,
            type = type,
            initial = category.take(1).uppercase(),
            date = date,
        )

        // Try API
        try {
            val req = CreateTransactionRequest(
                title = title,
                amount = amount,
                type = type.name,
                category = category,
                walletId = "w1",
                date = date,
                notes = notes,
            )
            ApiClient.service.createTransaction(req)
        } catch (_: Exception) {}

        // Update local state
        _transactions.value = listOf(newTx) + _transactions.value

        // Adjust wallet balance
        val currentWallets = _wallets.value.toMutableList()
        val index = currentWallets.indexOfFirst { it.name.contains(walletName, ignoreCase = true) }
        if (index != -1) {
            val w = currentWallets[index]
            val newBalance = when (type) {
                TxType.INCOME -> w.balance + amount
                TxType.EXPENSE -> w.balance - amount
                TxType.TRANSFER -> w.balance
            }
            currentWallets[index] = w.copy(balance = newBalance)
            _wallets.value = currentWallets
        }

        return newTx
    }

    suspend fun deleteTransaction(id: String): Boolean {
        try {
            ApiClient.service.deleteTransaction(id)
        } catch (_: Exception) {}
        _transactions.value = _transactions.value.filter { it.id != id }
        return true
    }

    // --- Wallets ---
    suspend fun addWallet(
        name: String,
        type: WalletType,
        initialBalance: Long,
        colorBg: Color,
        colorFg: Color,
    ): Wallet {
        val newWallet = Wallet(
            id = "w_" + System.currentTimeMillis(),
            name = name,
            type = type,
            balance = initialBalance,
            note = "Aktif",
            bg = colorBg,
            fg = colorFg,
        )
        try {
            val req = CreateWalletRequest(
                name = name,
                type = type.name,
                initialBalance = initialBalance,
            )
            ApiClient.service.createWallet(req)
        } catch (_: Exception) {}

        _wallets.value = _wallets.value + newWallet
        return newWallet
    }

    suspend fun transfer(
        fromWalletName: String,
        toWalletName: String,
        amount: Long,
        adminFee: Long = 0,
    ): Boolean {
        try {
            val req = TransferRequest(
                fromWalletId = fromWalletName,
                toWalletId = toWalletName,
                amount = amount,
                adminFee = adminFee,
                date = "Hari ini",
            )
            ApiClient.service.transferWallet(req)
        } catch (_: Exception) {}

        val current = _wallets.value.toMutableList()
        val fromIdx = current.indexOfFirst { it.name.contains(fromWalletName, ignoreCase = true) }
        val toIdx = current.indexOfFirst { it.name.contains(toWalletName, ignoreCase = true) }

        if (fromIdx != -1) {
            current[fromIdx] = current[fromIdx].copy(balance = current[fromIdx].balance - amount - adminFee)
        }
        if (toIdx != -1) {
            current[toIdx] = current[toIdx].copy(balance = current[toIdx].balance + amount)
        }
        _wallets.value = current

        // Record transfer transaction
        addTransaction(
            title = "Transfer ke $toWalletName",
            amount = amount,
            type = TxType.TRANSFER,
            category = "Transfer",
            walletName = fromWalletName,
            date = "Hari ini",
        )
        return true
    }

    // --- Financial Goals ---
    suspend fun addGoal(
        name: String,
        targetAmount: Long,
        initialSaved: Long,
        months: Int,
        targetDate: String,
    ): Goal {
        val status = if (initialSaved >= targetAmount * 0.2) GoalStatus.ON_TRACK else GoalStatus.BEHIND
        val newGoal = Goal(
            id = "g_" + System.currentTimeMillis(),
            name = name,
            initial = name.take(1).uppercase(),
            dateLabel = targetDate,
            saved = initialSaved,
            target = targetAmount,
            monthsLeft = months,
            status = status,
            bg = Color(0xFFE3ECE5),
            fg = Color(0xFF24563F),
            projection = "Target $targetDate dengan menabung teratur.",
        )
        try {
            val req = CreateGoalRequest(
                name = name,
                initialLetter = name.take(1).uppercase(),
                targetAmount = targetAmount,
                initialSaved = initialSaved,
                targetMonths = months,
                targetDate = targetDate,
            )
            ApiClient.service.createGoal(req)
        } catch (_: Exception) {}

        _goals.value = _goals.value + newGoal
        return newGoal
    }

    suspend fun addGoalSaving(goalId: String, amount: Long, fromWalletName: String): Boolean {
        try {
            val req = AddSavingRequest(
                goalId = goalId,
                walletId = fromWalletName,
                amount = amount,
                date = "Hari ini",
            )
            ApiClient.service.addGoalSaving(goalId, req)
        } catch (_: Exception) {}

        val currentGoals = _goals.value.toMutableList()
        val gIdx = currentGoals.indexOfFirst { it.id == goalId }
        if (gIdx != -1) {
            val g = currentGoals[gIdx]
            val newSaved = g.saved + amount
            val newStatus = if (newSaved >= g.target * 0.5) GoalStatus.ON_TRACK else g.status
            currentGoals[gIdx] = g.copy(saved = newSaved, status = newStatus)
            _goals.value = currentGoals
        }

        // Deduct from wallet
        val currentWallets = _wallets.value.toMutableList()
        val wIdx = currentWallets.indexOfFirst { it.name.contains(fromWalletName, ignoreCase = true) }
        if (wIdx != -1) {
            currentWallets[wIdx] = currentWallets[wIdx].copy(balance = currentWallets[wIdx].balance - amount)
            _wallets.value = currentWallets
        }

        return true
    }
}

