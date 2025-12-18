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
import com.sillymarvellouschad.medguard.network.checkDrugInteractions
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

// --------------------
// Risk Engine (DEFINED ONCE — SAFE)
// --------------------
enum class RiskLevel { HIGH, MODERATE, LOW }

fun evaluateRisk(medications: List<String>): RiskLevel {
    val meds = medications.map { it.lowercase().trim() }

    val illegalDrugs = setOf(
        "cocaine", "heroin", "meth", "methamphetamine",
        "mdma", "ecstasy", "lsd"
    )

    val opioids = setOf(
        "opioid", "morphine", "oxycodone",
        "hydrocodone", "codeine", "fentanyl"
    )

    val benzos = setOf(
        "benzodiazepine", "diazepam",
        "alprazolam", "lorazepam", "clonazepam"
    )

    val alcohol = setOf(
        "alcohol", "ethanol", "beer", "wine", "whiskey"
    )

    // 🚨 Illegal drugs → HIGH RISK
    if (meds.any { it in illegalDrugs }) {
        return RiskLevel.HIGH
    }

    // 🚨 Opioids + Benzos / Alcohol
    if (
        meds.any { it in opioids } &&
        (meds.any { it in benzos } || meds.any { it in alcohol })
    ) {
        return RiskLevel.HIGH
    }

    // 🚨 Benzos + Alcohol
    if (meds.any { it in benzos } && meds.any { it in alcohol }) {
        return RiskLevel.HIGH
    }

    // 🚨 Aspirin + Warfarin
    if ("aspirin" in meds && "warfarin" in meds) {
        return RiskLevel.HIGH
    }

    // ⚠️ Polypharmacy
    if (meds.size >= 5) {
        return RiskLevel.MODERATE
    }

    return RiskLevel.LOW
}

fun userFriendlyMessage(aiResult: String): String =
    if (
        aiResult.startsWith("Network Error") ||
        aiResult.startsWith("Google API Error")
    ) {
        "AI explanation is temporarily unavailable. Displaying rule-based medication safety information."
    } else aiResult

// --------------------
// UI
// --------------------
@Composable
@Preview
fun App(apiKey: String) {

    MaterialTheme {

        var medsInput by remember { mutableStateOf("") }
        var result by remember { mutableStateOf("Tap 'Check' to analyze medication safety.") }
        var riskLevel by remember { mutableStateOf<RiskLevel?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var accessibilityMode by remember { mutableStateOf(false) }
        var showScanner by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()

        val bodyText =
            if (accessibilityMode)
                MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
            else
                MaterialTheme.typography.bodyLarge

        val smallText =
            if (accessibilityMode) 16.sp else 12.sp

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

        // --------------------
        // MAIN SCREEN
        // --------------------
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Switch(
                    checked = accessibilityMode,
                    onCheckedChange = { accessibilityMode = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.LightGray
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

                    riskLevel = evaluateRisk(meds)

                    scope.launch {
                        isLoading = true
                        try {
                            result = checkDrugInteractions(apiKey, meds)
                        } catch (e: Exception) {
                            result = "Network Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Check Interactions", style = bodyText)
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {

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

                    Text(userFriendlyMessage(result), style = bodyText)
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text =
                    "Medical Disclaimer:\n" +
                            "MedGuard AI provides informational medication safety insights only. " +
                            "It is not a diagnostic tool and does not replace professional medical advice. " +
                            "Always consult a qualified healthcare professional before starting, stopping, " +
                            "or combining medications.",
                fontSize = smallText,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
