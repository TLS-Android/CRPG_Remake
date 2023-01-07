package com.tiagosantos.common.ui.utils

object VoiceCommandsProcessingHelper {

    val map = mapOf(
        "um" to 1, "dois" to 2, "três" to 3, "quatro" to 4,
        "cinco" to 5, "seis" to 6, "sete" to 7, "oito" to 8, "nove" to 9, "dez" to 10
    )

    val numberList = (1..10).toList()

    fun generalHelper(command: String, actionMap: Map<String, Any>){
        val idx = actionMap.getOrDefault(command, "empty")
        actionMap[idx]
    }

    fun mealPickHelper(command: String, meal: String): Boolean  {
        return (command.contains(meal, true)
                || (command.contains("Prato", true) && (command.contains(meal, true))))
    }

    fun hourHelper(
        command: String,
        hourEnglish: String,
        hourNumeral: String,
        dayTime: String,
    ) {
        (command.contains(hourEnglish, true) || command.contains(hourNumeral, true))
                && command.contains(dayTime, true)
    }

}