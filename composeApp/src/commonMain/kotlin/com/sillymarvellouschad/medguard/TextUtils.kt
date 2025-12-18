package com.sillymarvellouschad.medguard

/**
 * Cleans raw medicine text from scanner or user input.
 */
fun cleanMedicineText(rawText: String): String {
    return rawText
        .lowercase()
        .replace(Regex("\\b\\d+\\s?(mg|ml|mcg|g)\\b"), "")
        .replace(Regex("\\b(tablet|tab|capsule|cap|syrup|injection|inj)\\b"), "")
        .replace(Regex("[()\\[\\]]"), "")
        .split(",", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .distinct()
        .joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } }
}

/**
 * Detects network / AI failure.
 */
fun isAiUnavailable(result: String): Boolean {
    return result.startsWith("Network Error") ||
            result.startsWith("Google API Error")
}

/**
 * Professional fallback message for patients.
 */
fun aiFallbackMessage(original: String): String {
    return """
        Detailed AI explanation is temporarily unavailable.
        The safety assessment below is based on established medication interaction rules.

        $original
    """.trimIndent()
}
