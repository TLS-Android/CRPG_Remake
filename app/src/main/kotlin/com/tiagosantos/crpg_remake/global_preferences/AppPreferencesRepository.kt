package com.tiagosantos.crpg_remake.global_preferences

import android.content.Context
import android.content.SharedPreferences
import com.tiagosantos.common.ui.utils.Constants.MODALITY

class AppPreferencesRepository(
    private val ctx: Context
) {
    private lateinit var appPreferences: AppPreferencesStorage
    private fun saveAppPreferences(appPreferences: AppPreferences) {}

    fun resetAppPreferences() {
        val sharedPreferences = ctx.getSharedPreferences(
            MODALITY,
            Context.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putBoolean("meditationHasRun", false).apply()
        editor.putBoolean("notesHasRun", false).apply()
        editor.putBoolean("notesTextHasRun", false).apply()
        editor.putBoolean("selectionHasRun", false).apply()
        editor.putBoolean("transportsHasRun", false).apply()
        editor.putBoolean("publicTransportTimetableHasRun", false).apply()
        editor.putBoolean("remindersHasRun", false).apply()
        editor.putBoolean("agendaHasRun", false).apply()
    }

    val sharedPreferences: SharedPreferences? = ctx.getSharedPreferences(
        MODALITY,
        Context.MODE_PRIVATE
    )
}
