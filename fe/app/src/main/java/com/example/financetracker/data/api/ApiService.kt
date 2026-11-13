package com.example.financetracker.data.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // --- Authentication ---
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<ApiResponse<AuthResponse>>

    @GET("auth/profile")
    suspend fun getProfile(): Response<ApiResponse<UserDto>>

    // --- Transactions ---
    @GET("transactions")
    suspend fun getTransactions(
        @Query("month") month: String? = null,
        @Query("type") type: String? = null,
        @Query("search") search: String? = null,
    ): Response<ApiResponse<List<TransactionDto>>>

    @POST("transactions")
    suspend fun createTransaction(@Body body: CreateTransactionRequest): Response<ApiResponse<TransactionDto>>

    @GET("transactions/{id}")
    suspend fun getTransactionDetail(@Path("id") id: String): Response<ApiResponse<TransactionDto>>

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: String): Response<ApiResponse<Unit>>

    // --- Wallets ---
    @GET("wallets")
    suspend fun getWallets(): Response<ApiResponse<List<WalletDto>>>

    @POST("wallets")
    suspend fun createWallet(@Body body: CreateWalletRequest): Response<ApiResponse<WalletDto>>

    @PUT("wallets/{id}")
    suspend fun updateWallet(@Path("id") id: String, @Body body: CreateWalletRequest): Response<ApiResponse<WalletDto>>

    @DELETE("wallets/{id}")
    suspend fun deleteWallet(@Path("id") id: String): Response<ApiResponse<Unit>>

    @POST("wallets/transfer")
    suspend fun transferWallet(@Body body: TransferRequest): Response<ApiResponse<Unit>>

    // --- Financial Goals ---
    @GET("goals")
    suspend fun getGoals(): Response<ApiResponse<List<GoalDto>>>

    @POST("goals")
    suspend fun createGoal(@Body body: CreateGoalRequest): Response<ApiResponse<GoalDto>>

    @GET("goals/{id}")
    suspend fun getGoalDetail(@Path("id") id: String): Response<ApiResponse<GoalDto>>

    @POST("goals/{id}/savings")
    suspend fun addGoalSaving(@Path("id") id: String, @Body body: AddSavingRequest): Response<ApiResponse<Unit>>
}

