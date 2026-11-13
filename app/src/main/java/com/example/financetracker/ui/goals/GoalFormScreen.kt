package com.example.financetracker.ui.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financetracker.data.SampleGoals
import com.example.financetracker.data.formatRupiah
import com.example.financetracker.data.formatRupiahShort
import com.example.financetracker.ui.components.AppTextField
import com.example.financetracker.ui.components.PrimaryButton
import com.example.financetracker.ui.theme.Background
import com.example.financetracker.ui.theme.Outline
import com.example.financetracker.ui.theme.Primary
import com.example.financetracker.ui.theme.PrimaryVariant
import com.example.financetracker.ui.theme.Surface
import com.example.financetracker.ui.theme.TextMuted
import com.example.financetracker.ui.theme.TextPrimary
import com.example.financetracker.ui.theme.TextSecondary

private val goalIcons = listOf(
    "Laptop" to "L", "Handphone" to "H", "Liburan" to "V",
    "Pendidikan" to "P", "Rumah" to "R", "Kendaraan" to "K",
)

private val durations = listOf(3 to "Januari 2027", 6 to "April 2027", 12 to "Oktober 2027", 24 to "Oktober 2028")

@Composable
fun GoalFormScreen(
    goalId: String?,            // null = buat baru
    onBack: () -> Unit,
    onSave: () -> Unit,
) {
    val existing = remember(goalId) { SampleGoals.find(goalId) }
    val isEdit = existing != null

    var icon by remember { mutableStateOf(goalIcons.first().first) }
    var name by remember { mutableStateOf(existing?.name ?: "") }
    var target by remember { mutableStateOf(existing?.target?.toString() ?: "") }
    var initial by remember { mutableStateOf(existing?.saved?.toString() ?: "") }
    var months by remember { mutableStateOf(existing?.monthsLeft?.takeIf { m -> durations.any { it.first == m } } ?: 6) }
    var reminder by remember { mutableStateOf(true) }

    val targetValue = target.toLongOrNull() ?: 0L
    val initialValue = initial.toLongOrNull() ?: 0L
    val remaining = (targetValue - initialValue).coerceAtLeast(0)
    val perMonth = remaining / months
    val perWeek = (remaining / (months * 4.33)).toLong()
    val perDay = (remaining / (months * 30.4)).toLong()
    val dateLabel = durations.first { it.first == months }.second

    Column(Modifier.fillMaxSize().background(Background).statusBarsPadding().imePadding()) {
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Primary) }
                Text(if (isEdit) "Edit Goal" else "Buat Goal Baru", style = MaterialTheme.typography.headlineMedium, color = Primary)
            }

            // Pilih ikon
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Pilih ikon", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    goalIcons.forEach { (iconName, letter) ->
                        val selected = iconName == icon
                        Box(
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .background(if (selected) Color(0xFFE3ECE5) else Surface, RoundedCornerShape(16.dp))
                                .border(2.dp, if (selected) Primary else Color.Transparent, RoundedCornerShape(16.dp))
                                .clickable { icon = iconName },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(letter, color = if (selected) Primary else TextSecondary, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            AppTextField("Nama goal", name, { name = it }, placeholder = "Contoh: Laptop Baru")
            AppTextField("Target nominal", target, { target = it.filter(Char::isDigit).take(12) }, placeholder = "0", keyboardType = KeyboardType.Number)
            AppTextField("Tabungan awal (opsional)", initial, { initial = it.filter(Char::isDigit).take(12) }, placeholder = "0", keyboardType = KeyboardType.Number)

            // Durasi
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Target tercapai dalam", fontWeight = FontWeight.Bold, color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    durations.forEach { (m, _) ->
                        val selected = m == months
                        Box(
                            Modifier
                                .weight(1f)
                                .height(46.dp)
                                .background(if (selected) Primary else Surface, RoundedCornerShape(14.dp))
                                .border(1.dp, if (selected) Primary else Outline, RoundedCornerShape(14.dp))
                                .clickable { months = m },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("$m bln", color = if (selected) Color.White else TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
                Row {
                    Text("Tanggal target: ", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    Text(dateLabel, color = TextPrimary, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Kalkulator (berubah otomatis)
            Column(Modifier.fillMaxWidth().background(Primary, RoundedCornerShape(24.dp)).padding(20.dp)) {
                Text("GOAL CALCULATOR", color = Color(0xFFB8D8C2), style = MaterialTheme.typography.labelSmall)
                Text("Kamu perlu menabung", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(formatRupiah(perMonth), color = Color.White, style = MaterialTheme.typography.headlineLarge)
                    Text(" /bulan", color = Color(0xFFD6E4DA), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 4.dp))
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("≈ ${formatRupiahShort(perWeek)}/minggu", color = Color(0xFFB8D8C2), style = MaterialTheme.typography.labelSmall)
                    Text("≈ ${formatRupiahShort(perDay)}/hari", color = Color(0xFFB8D8C2), style = MaterialTheme.typography.labelSmall)
                }
            }

            // Pengingat
            Row(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(16.dp)).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Ingatkan saya menabung", fontWeight = FontWeight.SemiBold, color = TextPrimary, style = MaterialTheme.typography.bodyMedium)
                    Text("Setiap tanggal 25 (setelah gajian)", color = TextMuted, style = MaterialTheme.typography.labelSmall)
                }
                Switch(checked = reminder, onCheckedChange = { reminder = it }, colors = SwitchDefaults.colors(checkedTrackColor = PrimaryVariant))
            }
        }

        Box(Modifier.fillMaxWidth().background(Background).navigationBarsPadding().padding(horizontal = 20.dp, vertical = 12.dp)) {
            PrimaryButton("Simpan Goal", onClick = onSave, enabled = name.isNotBlank() && targetValue > 0)
        }
    }
}
