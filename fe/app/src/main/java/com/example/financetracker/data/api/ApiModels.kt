package com.example.financetracker.data.api

data class ApiResponse<T>(
    val success: Boolean = true,
    val message: String? = null,
    val data: T? = null,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
)

data class AuthResponse(
    val token: String,
    val user: UserDto,
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val totalWallets: Int = 0,
    val totalTransactions: Int = 0,
    val totalGoals: Int = 0,
)

data class TransactionDto(
    val id: String,
    val title: String,
    val amount: Long,
    val type: String, // "INCOME", "EXPENSE", "TRANSFER"
    val category: String,
    val walletId: String,
    val walletName: String,
    val date: String,
    val time: String? = null,
    val notes: String? = null,
    val receiptUrl: String? = null,
)

data class CreateTransactionRequest(
    val title: String,
    val amount: Long,
    val type: String,
    val category: String,
    val walletId: String,
    val date: String,
    val time: String? = null,
    val notes: String? = null,
)

data class WalletDto(
    val id: String,
    val name: String,
    val type: String, // "BANK", "EWALLET", "CASH", "CREDIT", "INVESTMENT"
    val balance: Long,
    val note: String? = null,
    val colorHex: String? = null,
    val includeInTotal: Boolean = true,
)

data class CreateWalletRequest(
    val name: String,
    val type: String,
    val initialBalance: Long,
    val includeInTotal: Boolean = true,
    val colorHex: String? = null,
)

data class TransferRequest(
    val fromWalletId: String,
    val toWalletId: String,
    val amount: Long,
    val adminFee: Long = 0,
    val date: String,
    val notes: String? = null,
)

data class GoalDto(
    val id: String,
    val name: String,
    val initial: String? = null,
    val targetAmount: Long,
    val currentSaved: Long,
    val targetDate: String,
    val monthsLeft: Int,
    val status: String,
    val projection: String? = null,
)

data class CreateGoalRequest(
    val name: String,
    val initialLetter: String,
    val targetAmount: Long,
    val initialSaved: Long = 0,
    val targetMonths: Int,
    val targetDate: String,
)

data class AddSavingRequest(
    val goalId: String,
    val walletId: String,
    val amount: Long,
    val date: String,
)

