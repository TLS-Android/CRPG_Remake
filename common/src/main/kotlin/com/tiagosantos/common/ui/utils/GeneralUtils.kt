package com.tiagosantos.common.ui.utils


object GeneralUtils {

    const val LOG_TAG_DEBUG = "app.debug"
    const val LOG_TAG_ERROR = "app.error"

    fun mealPickHelper(command: String, meal: String): Boolean  {
        return (command.contains(meal, true)
            || (command.contains("Prato", true) && (command.contains(meal, true))))
    }

}