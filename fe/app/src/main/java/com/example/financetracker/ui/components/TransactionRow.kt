package com.example.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.Transaction
import com.example.financetracker.data.TxType
import com.example.financetracker.data.amountLabel
import com.example.financetracker.ui.theme.Expense
import com.example.financetracker.ui.theme.Income
import com.example.financetracker.ui.theme.Info
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary

data class TxStyle(val bg: Color, val fg: Color, val amount: Color)

fun TxType.style() = when (this) {
    TxType.INCOME -> TxStyle(Color(0xFFE3ECE5), Color(0xFF24563F), Income)
    TxType.EXPENSE -> TxStyle(Color(0xFFFBEADF), Color(0xFFA3501E), Expense)
    TxType.TRANSFER -> TxStyle(Color(0xFFE2EAF4), Color(0xFF2F5F96), Info)
}

@Composable
fun TransactionRow(
    t: Transaction,
    onClick: () -> Unit,
    showDate: Boolean = false,
) {
    val s = t.type.style()
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(Modifier.size(44.dp).background(s.bg, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Text(t.initial, color = s.fg, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f)) {
            Text(t.title, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
            Text(t.meta, color = TextMuted, style = MaterialTheme.typography.labelSmall)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(t.amountLabel(), color = s.amount, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            if (showDate && t.date.isNotEmpty()) {
                Text(t.date, color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
