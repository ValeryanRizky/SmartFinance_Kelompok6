package com.example.financetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Bar progres. [fraction] = 0f..1f.
 * [previewFraction] (opsional) digambar di belakang [fraction], untuk menunjukkan progres setelah aksi.
 */
@Composable
fun ProgressBar(
    fraction: Float,
    color: Color,
    modifier: Modifier = Modifier,
    track: Color = Color(0xFFEEECE5),
    previewFraction: Float? = null,
    previewColor: Color = Color(0xFFB8D8C2),
) {
    val shape = RoundedCornerShape(4.dp)
    Box(modifier.fillMaxWidth().height(8.dp).background(track, shape)) {
        if (previewFraction != null) {
            Box(Modifier.fillMaxWidth(previewFraction.coerceIn(0f, 1f)).height(8.dp).background(previewColor, shape))
        }
        Box(Modifier.fillMaxWidth(fraction.coerceIn(0f, 1f)).height(8.dp).background(color, shape))
    }
}
