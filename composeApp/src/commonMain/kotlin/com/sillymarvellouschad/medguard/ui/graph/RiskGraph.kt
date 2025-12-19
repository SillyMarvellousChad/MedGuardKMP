package com.sillymarvellouschad.medguard.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sillymarvellouschad.medguard.domain.model.risk.RiskLevel

@Composable
fun RiskGraph(level: RiskLevel) {

    val (low, med, high) = when (level) {
        RiskLevel.LOW -> Triple(1f, 0.3f, 0.1f)
        RiskLevel.MODERATE -> Triple(0.3f, 1f, 0.3f)
        RiskLevel.HIGH -> Triple(0.1f, 0.3f, 1f)
    }

    Column {
        Bar("Low", low, Color(0xFF2E7D32))
        Bar("Moderate", med, Color(0xFFFFA000))
        Bar("High", high, Color.Red)
    }
}

@Composable
private fun Bar(label: String, value: Float, color: Color) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Box(
            modifier = Modifier
                .fillMaxWidth(value)
                .height(10.dp)
                .background(color)
        )
        Spacer(Modifier.height(6.dp))
    }
}
