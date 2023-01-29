package com.tiagosantos.crpg_remake.global_preferences

import android.content.Context
import android.content.SharedPreferences
import com.tiagosantos.common.ui.utils.Constants.MODALITY

class AppPreferencesRepository(
    ctx: Context
) {
    private lateinit var appPreferences: AppPreferencesStorage
    private fun saveAppPreferences(appPreferences: AppPreferences) {}

    private val literalValue = listOf("meditationHasRun", "selectionHasRun",
        "remindersHasRun", "agendaHasRun"
    )

    private val sharedPreferences: SharedPreferences? = ctx.getSharedPreferences(
        MODALITY,
        Context.MODE_PRIVATE
    )

    fun resetAppPreferences() = sharedPreferences!!.apply {
        for (i in 1..literalValue.size)  edit().putBoolean(literalValue[i], false).apply()
    }

}
