package com.tiagosantos.crpg_remake.global_preferences

import android.content.Context
import android.content.SharedPreferences
import com.tiagosantos.common.ui.utils.Constants.MODALITY

class AppPreferencesRepository(
    private val ctx: Context
) {
    private lateinit var appPreferences: AppPreferencesStorage
    private fun saveAppPreferences(appPreferences: AppPreferences) {}

    private val literalValue = listOf(
        "meditationHasRun", "notesHasRun", "notesTextHasRun",
        "selectionHasRun", "remindersHasRun", "agendaHasRun"
    )

    fun resetAppPreferences() {
        val sharedPreferences = ctx.getSharedPreferences(MODALITY, Context.MODE_PRIVATE).apply {
            for (i in 1..literalValue.size)  edit().putBoolean(literalValue[i], false).apply()
        }
    }

    val sharedPreferences: SharedPreferences? = ctx.getSharedPreferences(
        MODALITY,
        Context.MODE_PRIVATE
    )
}
