package com.sillymarvellouschad.medguard.domain.model.risk



data class RiskEvaluation(
    val level: RiskLevel,
    val explanation: String
)

object RiskEngine {

    fun evaluateWithExplanation(medications: List<String>): RiskEvaluation {
        val meds = medications.map { it.lowercase().trim() }

        // Illegal / recreational drugs
        val illegalDrugs = setOf(
            "cocaine", "heroin", "meth", "methamphetamine",
            "mdma", "ecstasy", "lsd"
        )

        val opioids = setOf(
            "morphine", "oxycodone", "hydrocodone",
            "codeine", "fentanyl"
        )

        val benzos = setOf(
            "diazepam", "alprazolam", "lorazepam", "clonazepam"
        )

        val alcohol = setOf(
            "alcohol", "ethanol", "beer", "wine", "whiskey"
        )

        val nsaids = setOf(
            "ibuprofen", "naproxen", "diclofenac", "indomethacin"
        )

        // 🚨 Illegal drugs
        if (meds.any { it in illegalDrugs }) {
            return RiskEvaluation(
                RiskLevel.HIGH,
                "Use of illegal or recreational substances poses severe health risks and unpredictable interactions."
            )
        }

        // 🚨 Opioids + benzos or alcohol
        if (
            meds.any { it in opioids } &&
            (meds.any { it in benzos } || meds.any { it in alcohol })
        ) {
            return RiskEvaluation(
                RiskLevel.HIGH,
                "Combining opioids with benzodiazepines or alcohol significantly increases the risk of respiratory depression and overdose."
            )
        }

        // 🚨 Benzos + alcohol
        if (meds.any { it in benzos } && meds.any { it in alcohol }) {
            return RiskEvaluation(
                RiskLevel.HIGH,
                "Using benzodiazepines together with alcohol can cause extreme sedation, breathing suppression, and coma."
            )
        }

        // 🚨 Aspirin + warfarin
        if ("aspirin" in meds && "warfarin" in meds) {
            return RiskEvaluation(
                RiskLevel.HIGH,
                "Combining aspirin with warfarin greatly increases the risk of internal bleeding."
            )
        }

        // ⚠️ Multiple NSAIDs
        if (meds.count { it in nsaids } >= 2) {
            return RiskEvaluation(
                RiskLevel.MODERATE,
                "Using multiple NSAIDs together increases the risk of stomach bleeding and kidney damage."
            )
        }

        // ⚠️ Polypharmacy
        if (meds.size >= 5) {
            return RiskEvaluation(
                RiskLevel.MODERATE,
                "Taking many medications together increases the likelihood of unintended drug interactions."
            )
        }

        return RiskEvaluation(
            RiskLevel.LOW,
            "No high-risk interactions detected based on the entered medications."
        )
    }
}
