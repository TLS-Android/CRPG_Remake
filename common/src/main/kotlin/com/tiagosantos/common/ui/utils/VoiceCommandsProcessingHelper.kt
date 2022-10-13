package com.tiagosantos.common.ui.utils

object VoiceCommandsProcessingHelper {

    fun mealPickHelper(command: String, meal: String): Boolean  {
        return (command.contains(meal, true)
                || (command.contains("Prato", true) && (command.contains(meal, true))))
    }

    fun hourHelper(
        command: String,
        hourEnglish: String,
        hourNumeral: String,
        dayTime: String,
    ): Boolean {
        (command.contains(hourEnglish, true) || command.contains(hourNumeral, true))
                && command.contains(dayTime, true)
    }

}