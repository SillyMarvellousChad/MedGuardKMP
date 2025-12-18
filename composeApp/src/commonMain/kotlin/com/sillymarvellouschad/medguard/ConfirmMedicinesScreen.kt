package com.sillymarvellouschad.medguard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmMedicinesScreen(
    initialText: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    var editableText by remember { mutableStateOf(initialText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Confirm Medicines",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Please confirm or edit the detected medicines before continuing.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = editableText,
            onValueChange = { editableText = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            label = { Text("Detected medicines") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onConfirm(editableText) }
        ) {
            Text("✔ Confirm")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onCancel
        ) {
            Text("Cancel")
        }
    }
}


