package com.example.financetracker.ui.goals

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.SampleGoals
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.data.monthlyNeed
import com.example.financetracker.data.percent
import com.example.financetracker.data.remaining
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Income
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

@Composable
fun GoalDetailScreen(
    goalId: String?,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddSaving: () -> Unit,
) {
    val goal = SampleGoals.find(goalId)
    if (goal == null) {
        Box(Modifier.fillMaxSize().background(Background), contentAlignment = Alignment.Center) { Text("Goal tidak ditemukan") }
        return
    }
    val history = SampleGoals.historyFor(goal.id)

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary) }
                Text(goal.name, Modifier.weight(1f).padding(start = 4.dp), style = MaterialTheme.typography.headlineMedium, color = Primary)
                IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, contentDescription = "Edit goal", tint = Primary) }
            }

            // Kartu progres
            Column(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(24.dp)).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(Modifier.size(184.dp), contentAlignment = Alignment.Center) {
                    Canvas(Modifier.fillMaxSize()) {
                        val stroke = 16.dp.toPx()
                        val topLeft = Offset(stroke / 2, stroke / 2)
                        val arcSize = Size(size.width - stroke, size.height - stroke)
                        drawArc(
                            color = Color(0xFFEEECE5), startAngle = -90f, sweepAngle = 360f, useCenter = false,
                            topLeft = topLeft, size = arcSize, style = Stroke(stroke),
                        )
                        drawArc(
                            color = goal.status.bar, startAngle = -90f, sweepAngle = 360f * goal.percent / 100f, useCenter = false,
                            topLeft = topLeft, size = arcSize, style = Stroke(stroke, cap = StrokeCap.Round),
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${goal.percent}%", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                        Text("tercapai", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(formatRupiah(goal.saved), style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Text("dari ${formatRupiah(goal.target)}", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoBox("Kurang", formatRupiah(goal.remaining), Modifier.weight(1f))
                    InfoBox("Target", goal.dateLabel, Modifier.weight(1f))
                }
            }

            // Status
            Row(
                Modifier.fillMaxWidth().background(goal.status.chipBg, RoundedCornerShape(18.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(Modifier.size(12.dp).background(goal.status.bar, CircleShape))
                Column {
                    Text(
                        "${goal.status.label.removePrefix("● ")} · ${formatRupiah(goal.monthlyNeed)}/bulan",
                        fontWeight = FontWeight.Bold, color = goal.status.chipFg, style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(goal.projection, color = goal.status.chipFg, style = MaterialTheme.typography.labelSmall)
                }
            }

            // Riwayat
            Column {
                Text("Riwayat Tabungan", style = MaterialTheme.typography.titleMedium, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                Column(Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp))) {
                    history.forEach { h ->
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Box(Modifier.size(36.dp).background(Color(0xFFE3ECE5), CircleShape), contentAlignment = Alignment.Center) {
                                Text("+", color = Income, fontWeight = FontWeight.Bold)
                            }
                            Column(Modifier.weight(1f)) {
                                Text(h.date, fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                                Text("dari ${h.wallet}", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                            }
                            Text("+ ${formatRupiah(h.amount)}", color = Income, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Tambah Tabungan", onClick = onAddSaving)
        }
    }
}

@Composable
private fun InfoBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Color(0xFFF3F1EA), RoundedCornerShape(16.dp)).padding(14.dp)) {
        Text(label, color = TextSecondary, style = MaterialTheme.typography.labelSmall)
        Text(value, fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
    }
}
