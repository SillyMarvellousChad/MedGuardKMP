package com.sillymarvellouschad.medguard

import androidx.compose.runtime.Composable

@Composable
expect fun ScanScreen(
    onResult: (String) -> Unit,
    onCancel: () -> Unit
)
