package com.example.financetracker.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.financetracker.ui.auth.LoginScreen
import com.example.financetracker.ui.auth.OnboardingScreen
import com.example.financetracker.ui.auth.RegisterScreen
import com.example.financetracker.ui.components.MainScaffold
import com.example.financetracker.ui.dashboard.DashboardScreen
import com.example.financetracker.ui.goals.AddSavingScreen
import com.example.financetracker.ui.goals.GoalDetailScreen
import com.example.financetracker.ui.goals.GoalFormScreen
import com.example.financetracker.ui.goals.GoalsScreen
import com.example.financetracker.ui.transactions.AddTransactionScreen
import com.example.financetracker.ui.transactions.TransactionsScreen
import com.example.financetracker.ui.wallet.TransferScreen
import com.example.financetracker.ui.wallet.WalletFormScreen
import com.example.financetracker.ui.wallet.WalletScreen

@Composable
private fun Placeholder(name: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(name) }
}

// Pindah antar tab utama tanpa menumpuk back stack
private fun NavHostController.navigateTab(route: String) {
    navigate(route) {
        popUpTo(Routes.DASHBOARD) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val goTab: (String) -> Unit = { navController.navigateTab(it) }
    val goAdd: () -> Unit = { navController.navigate(Routes.ADD_TRANSACTION) }

    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {

        // ---- Auth ----
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onStart = { navController.navigate(Routes.REGISTER) },
                onLogin = { navController.navigate(Routes.LOGIN) },
                onSkip = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } } },
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLogin = { navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } } },
                onRegister = { navController.navigate(Routes.REGISTER) },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegister = { navController.navigate(Routes.walletForm()) },
                onLogin = { navController.navigate(Routes.LOGIN) },
            )
        }

        // ---- Tab utama (pakai bottom navigation) ----
        composable(Routes.DASHBOARD) {
            MainScaffold(Routes.DASHBOARD, goTab, goAdd) { mod ->
                DashboardScreen(
                    modifier = mod,
                    onProfile = { navController.navigate(Routes.PROFILE) },
                    onSearch = { goTab(Routes.TRANSACTIONS) },
                    onAddTransaction = goAdd,
                    onTransfer = { navController.navigate(Routes.TRANSFER) },
                    onGoals = { goTab(Routes.GOALS) },
                    onInsight = { navController.navigate(Routes.INSIGHT) },
                    onSeeAll = { goTab(Routes.TRANSACTIONS) },
                    onTransactionClick = { navController.navigate(Routes.transactionDetail(it)) },
                )
            }
        }
        composable(Routes.TRANSACTIONS) {
            MainScaffold(Routes.TRANSACTIONS, goTab, goAdd) { mod ->
                TransactionsScreen(
                    modifier = mod,
                    onOpenFilter = { navController.navigate(Routes.FILTER) },
                    onTransactionClick = { navController.navigate(Routes.transactionDetail(it)) },
                )
            }
        }
        composable(Routes.WALLET) {
            MainScaffold(Routes.WALLET, goTab, goAdd) { mod ->
                WalletScreen(
                    modifier = mod,
                    onAdd = { navController.navigate(Routes.walletForm()) },
                    onTransfer = { navController.navigate(Routes.TRANSFER) },
                    onWalletClick = { navController.navigate(Routes.walletForm(it)) },
                )
            }
        }
        composable(Routes.GOALS) {
            MainScaffold(Routes.GOALS, goTab, goAdd) { mod ->
                GoalsScreen(
                    modifier = mod,
                    onNewGoal = { navController.navigate(Routes.goalForm()) },
                    onGoalClick = { navController.navigate(Routes.goalDetail(it)) },
                    onAddSaving = { navController.navigate(Routes.addSaving(it)) },
                )
            }
        }

        // ---- Transaksi ----
        composable(Routes.ADD_TRANSACTION) {
            AddTransactionScreen(
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
            )
        }
        composable(Routes.TRANSACTION_DETAIL) { Placeholder("Detail Transaksi") }
        composable(Routes.FILTER) { Placeholder("Filter") }

        // ---- Wallet ----
        composable(Routes.TRANSFER) {
            TransferScreen(
                onBack = { navController.popBackStack() },
                onTransfer = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.WALLET_FORM,
            arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }),
        ) { entry ->
            // Setelah simpan: kembali ke tab Wallet; kalau datang dari Register, masuk ke Dashboard
            val finish: () -> Unit = {
                if (!navController.popBackStack(Routes.WALLET, inclusive = false)) {
                    navController.navigate(Routes.DASHBOARD) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
                }
            }
            WalletFormScreen(
                walletId = entry.arguments?.getString("id"),
                onBack = { navController.popBackStack() },
                onSave = finish,
                onDelete = finish,
            )
        }

        // ---- Goals ----
        composable(
            route = Routes.GOAL_FORM,
            arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true; defaultValue = null }),
        ) { entry ->
            GoalFormScreen(
                goalId = entry.arguments?.getString("id"),
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
            )
        }
        composable(Routes.GOAL_DETAIL) { entry ->
            val id = entry.arguments?.getString("id")
            GoalDetailScreen(
                goalId = id,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.goalForm(id)) },
                onAddSaving = { id?.let { navController.navigate(Routes.addSaving(it)) } },
            )
        }
        composable(Routes.ADD_SAVING) { entry ->
            AddSavingScreen(
                goalId = entry.arguments?.getString("goalId"),
                onClose = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
            )
        }

        // ---- Belum dibuat (tahap berikutnya) ----
        composable(Routes.INSIGHT) { Placeholder("Insight") }
        composable(Routes.PROFILE) { Placeholder("Profil") }
        composable(Routes.CATEGORIES) { Placeholder("Kategori") }
    }
}
