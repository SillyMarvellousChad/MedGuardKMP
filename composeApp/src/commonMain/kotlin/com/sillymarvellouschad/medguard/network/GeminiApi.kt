package com.sillymarvellouschad.medguard.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// 1️⃣ HTTP Client (shared-safe)
val client = HttpClient {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        )
    }
}

// 2️⃣ API Function
suspend fun checkDrugInteractions(
    apiKey: String,
    medications: List<String>
): String {

    val prompt = """
        You are a medication safety assistant.
        Analyze the following medications for possible interactions.
        Use non-diagnostic, patient-friendly language.
        Medications: ${medications.joinToString(", ")}

        If no major interactions exist, say:
        "No major interactions found, but consult a healthcare professional."
    """.trimIndent()

    return try {
        val response = client.post(
            "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=$apiKey"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    contents = listOf(
                        Content(
                            parts = listOf(
                                Part(text = prompt)
                            )
                        )
                    )
                )
            )
        }

        val raw = response.bodyAsText()

        if (!response.status.isSuccess()) {
            "Google API Error: $raw"
        } else {
            // 🔥 DYNAMIC & FUTURE-PROOF PARSING
            val root = Json.parseToJsonElement(raw).jsonObject

            val text = root["candidates"]
                ?.jsonArray
                ?.firstOrNull()
                ?.jsonObject
                ?.get("content")
                ?.jsonObject
                ?.get("parts")
                ?.jsonArray
                ?.joinToString("\n") {
                    it.jsonObject["text"]?.jsonPrimitive?.content ?: ""
                }

            text?.ifBlank {
                "AI returned an empty response."
            } ?: "No AI response available."
        }
    } catch (e: Exception) {
        "Network Error: ${e.message}"
    }
}

// 3️⃣ REQUEST MODELS (STABLE – KEEP THESE)
@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)
