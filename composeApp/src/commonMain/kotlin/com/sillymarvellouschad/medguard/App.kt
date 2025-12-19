package com.sillymarvellouschad.medguard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sillymarvellouschad.medguard.domain.model.risk.RiskEngine
import com.sillymarvellouschad.medguard.domain.model.risk.RiskLevel
import com.sillymarvellouschad.medguard.network.checkDrugInteractions
import com.sillymarvellouschad.medguard.ui.graph.RiskGraph
import kotlinx.coroutines.launch

// --------------------
// AI MESSAGE SANITIZER
// --------------------
private fun sanitizeAiMessage(raw: String): String {
    val lower = raw.lowercase()
    return when {
        "quota" in lower ||
                "rate" in lower ||
                "billing" in lower ||
                "limit" in lower ||
                "network" in lower ||
                "error" in lower ->
            "AI analysis is temporarily unavailable. The rule-based safety assessment above should be followed."
        else -> raw
    }
}

@Composable
fun App(apiKey: String) {

    MaterialTheme {

        var medsInput by remember { mutableStateOf("") }
        var riskLevel by remember { mutableStateOf<RiskLevel?>(null) }
        var ruleExplanation by remember { mutableStateOf("") }
        var aiExplanation by remember { mutableStateOf<String?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var accessibilityMode by remember { mutableStateOf(false) }
        var showScanner by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        val bodyText =
            if (accessibilityMode)
                MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
            else
                MaterialTheme.typography.bodyMedium

        // --------------------
        // SCANNER SCREEN
        // --------------------
        if (showScanner) {
            ScanScreen(
                onResult = {
                    medsInput = it.replace("\n", ", ")
                    showScanner = false
                },
                onCancel = { showScanner = false }
            )
            return@MaterialTheme
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "MedGuard AI",
                fontSize = if (accessibilityMode) 28.sp else 22.sp,
                color = Color(0xFF1E3A8A)
            )

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = accessibilityMode,
                    onCheckedChange = { accessibilityMode = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF4CAF50)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text("Accessibility Mode (Large Text)", style = bodyText)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = medsInput,
                onValueChange = { medsInput = it },
                label = { Text("Enter medications (comma separated)", style = bodyText) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                textStyle = bodyText
            )

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showScanner = true }
            ) {
                Text("📷 Scan Medicines", style = bodyText)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                onClick = {
                    val meds = medsInput
                        .split(",")
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    val evaluation = RiskEngine.evaluateWithExplanation(meds)
                    riskLevel = evaluation.level
                    ruleExplanation = evaluation.explanation

                    scope.launch {
                        isLoading = true
                        aiExplanation = try {
                            sanitizeAiMessage(
                                checkDrugInteractions(apiKey, meds)
                            )
                        } catch (_: Exception) {
                            "AI analysis temporarily unavailable."
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Check Interactions", style = bodyText)
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {

                    riskLevel?.let {
                        RiskGraph(it)
                        Spacer(Modifier.height(12.dp))
                    }

                    when (riskLevel) {
                        RiskLevel.HIGH ->
                            RiskBanner("❗", "HIGH RISK", Color.Red, bodyText)
                        RiskLevel.MODERATE ->
                            RiskBanner("⚠️", "MODERATE RISK", Color(0xFFFFA000), bodyText)
                        RiskLevel.LOW ->
                            RiskBanner("✅", "LOW RISK", Color(0xFF2E7D32), bodyText)
                        null -> {}
                    }

                    Spacer(Modifier.height(8.dp))

                    if (ruleExplanation.isNotBlank()) {
                        Text(
                            text = "Why this matters:\n$ruleExplanation",
                            style = bodyText
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    aiExplanation?.let {
                        Text(
                            text = "AI Insight:\n$it",
                            style = bodyText.copy(
                                fontSize = if (accessibilityMode) 18.sp else 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text =
                    "Medical Disclaimer:\n" +
                            "MedGuard AI provides informational medication safety insights only. " +
                            "It is not a diagnostic tool and does not replace professional medical advice. " +
                            "Always consult a qualified healthcare professional before starting, stopping, " +
                            "or combining medications."

            )
        }
    }
}

// --------------------
// Risk Banner
// --------------------
@Composable
fun RiskBanner(
    icon: String,
    text: String,
    color: Color,
    style: androidx.compose.ui.text.TextStyle
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(icon, fontSize = 24.sp, color = color)
        Spacer(Modifier.width(8.dp))
        Text(text, style = style, color = color)
    }
}
