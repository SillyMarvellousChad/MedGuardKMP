package com.sillymarvellouschad.medguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {

                // ✅ ANDROID HEADER (SAFE PLACE FOR LOGO)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_medguard_logo),
                        contentDescription = "MedGuard Logo",
                        modifier = Modifier.size(80.dp)
                    )
                }

                // ✅ COMMON UI
                App(apiKey = BuildConfig.GEMINI_API_KEY)
            }
        }
    }
}
