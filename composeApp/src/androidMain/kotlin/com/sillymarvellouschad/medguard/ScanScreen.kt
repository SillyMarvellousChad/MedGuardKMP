package com.sillymarvellouschad.medguard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun ScanScreen(
    onResult: (String) -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Scan Medicines",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Camera scanning is simulated in this build.\n" +
                    "This demonstrates the end-to-end flow safely.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ SAFE simulated scan
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onResult("Aspirin, Warfarin")
            }
        ) {
            Text("Simulate Scan Result")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancel
        ) {
            Text("Cancel")
        }
    }
}
