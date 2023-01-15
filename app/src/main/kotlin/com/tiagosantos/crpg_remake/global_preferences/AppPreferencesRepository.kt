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

    val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", 
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", 
            |"start_time": "2000","end_time": "2100","date": "2021-03-17"},
            |{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", 
            |"start_time": "0830","end_time": "1330","date": "2021-03-17"},
            |{"title": "Actividade","info":"Sala 12","type": "ACTIVITY",
            | "start_time": "0830","end_time": "1330","date": "2021-03-17"}]""".trimMargin()

}
