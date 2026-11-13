package com.example.financetracker.ui.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.Goal
import com.example.financetracker.data.SampleGoals
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.data.formatRupiahShort
import com.example.financetracker.data.monthlyNeed
import com.example.financetracker.data.percent
import com.example.financetracker.ui.components.ProgressBar
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.PrimaryVariant
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary

@Composable
fun GoalsScreen(
    modifier: Modifier = Modifier,
    onNewGoal: () -> Unit,
    onGoalClick: (String) -> Unit,
    onAddSaving: (String) -> Unit,
) {
    val goals = SampleGoals.list
    val featured = goals.first()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Goals", style = MaterialTheme.typography.headlineMedium, color = Primary)
            Row(
                Modifier.background(Primary, RoundedCornerShape(14.dp)).clickable(onClick = onNewGoal).padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text("Goal Baru", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
        }

        CalculatorCard(featured)

        goals.forEach { g -> GoalCard(g, onClick = { onGoalClick(g.id) }, onAddSaving = { onAddSaving(g.id) }) }
    }
}

@Composable
private fun CalculatorCard(goal: Goal) {
    val need = goal.monthlyNeed
    Column(Modifier.fillMaxWidth().background(Primary, RoundedCornerShape(28.dp)).padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column {
                Text("GOAL CALCULATOR", color = Color(0xFFB8D8C2), style = MaterialTheme.typography.labelSmall)
                Text(goal.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
            }
            Text(
                goal.status.label,
                Modifier.background(goal.status.chipBg, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
                color = goal.status.chipFg,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Spacer(Modifier.height(16.dp))
        Text("Agar tercapai sebelum ${goal.dateLabel}, sisihkan", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(formatRupiah(need), color = Color.White, style = MaterialTheme.typography.headlineLarge)
            Text(" /bulan", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Stat("Per minggu", formatRupiahShort((need / 4.33).toLong()), Modifier.weight(1f))
            Stat("Per hari", formatRupiahShort((need / 30.4).toLong()), Modifier.weight(1f))
            Stat("Sisa waktu", "${goal.monthsLeft} bulan", Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        Text(
            "Rata-rata tabunganmu Rp 1,6 jt/bulan, cukup untuk target ini.",
            color = Color(0xFFB8D8C2),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(PrimaryVariant, RoundedCornerShape(14.dp)).padding(12.dp)) {
        Text(label, color = Color(0xFFD6E4DA), style = MaterialTheme.typography.labelSmall)
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun GoalCard(goal: Goal, onClick: () -> Unit, onAddSaving: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp)).clickable(onClick = onClick).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(44.dp).background(goal.bg, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                Text(goal.initial, color = goal.fg, fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(1f)) {
                Text(goal.name, fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Text("Target ${goal.dateLabel}", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                goal.status.label,
                Modifier.background(goal.status.chipBg, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 4.dp),
                color = goal.status.chipFg,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(formatRupiah(goal.saved), fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Text(" / ${formatRupiah(goal.target)}", color = TextMuted, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(bottom = 2.dp))
            }
            Text("${goal.percent}%", fontWeight = FontWeight.Bold, color = goal.status.bar, style = MaterialTheme.typography.bodyLarge)
        }
        ProgressBar(goal.percent / 100f, goal.status.bar)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Perlu ${formatRupiah(goal.monthlyNeed)}/bulan", color = TextMuted, style = MaterialTheme.typography.labelSmall)
            Text(
                "+ Tabung",
                Modifier.background(Color(0xFFE3ECE5), RoundedCornerShape(12.dp)).clickable(onClick = onAddSaving).padding(horizontal = 12.dp, vertical = 6.dp),
                color = Primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
