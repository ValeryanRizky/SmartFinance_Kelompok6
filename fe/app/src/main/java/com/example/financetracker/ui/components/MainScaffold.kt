package com.example.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.financetracker.navigation.Routes
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface as SurfaceColor
import com.example.financetracker.ui.theme.TextMuted

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private val leftTabs = listOf(
    TabItem(Routes.DASHBOARD, "Home", Icons.Filled.Home),
    TabItem(Routes.TRANSACTIONS, "Transaksi", Icons.AutoMirrored.Filled.List),
)
private val rightTabs = listOf(
    TabItem(Routes.WALLET, "Wallet", Icons.Filled.AccountBalanceWallet),
    TabItem(Routes.GOALS, "Goals", Icons.Filled.Flag),
)

@Composable
fun MainScaffold(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onAddClick: () -> Unit,
    content: @Composable (Modifier) -> Unit,
) {
    Scaffold(
        containerColor = Background,
        bottomBar = { AppBottomBar(currentRoute, onTabSelected, onAddClick) },
    ) { inner ->
        content(Modifier.padding(inner))
    }
}

@Composable
private fun AppBottomBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    Surface(color = SurfaceColor, shadowElevation = 8.dp) {
        Row(
            Modifier.fillMaxWidth().navigationBarsPadding().padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            leftTabs.forEach { TabButton(it, it.route == currentRoute) { onTabSelected(it.route) } }

            Box(
                Modifier.size(52.dp).background(Primary, CircleShape).clickable(onClick = onAddClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah transaksi", tint = SurfaceColor)
            }

            rightTabs.forEach { TabButton(it, it.route == currentRoute) { onTabSelected(it.route) } }
        }
    }
}

@Composable
private fun TabButton(item: TabItem, selected: Boolean, onClick: () -> Unit) {
    val tint = if (selected) Primary else TextMuted
    Column(
        Modifier.clickable(onClick = onClick).padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(item.icon, contentDescription = item.label, tint = tint)
        Text(item.label, color = tint, style = MaterialTheme.typography.labelSmall)
    }
}
