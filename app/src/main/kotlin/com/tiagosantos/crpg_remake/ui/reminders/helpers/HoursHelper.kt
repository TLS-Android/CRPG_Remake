package com.tiagosantos.crpg_remake.ui.reminders.helpers

import android.annotation.SuppressLint
import com.tiagosantos.common.ui.utils.Constants.t
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.afternoonHoursMap
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.daytimeHoursMap
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.nightTimeHoursMap

object HoursHelper {

    private fun getTime(
        command: String,
        map: Map<String, String>
    ): String? { val value = map[command]; return value }

    fun checkHoursCommand(command: String): String = command.apply {
        when {
            contains("da manhã", t) -> checkDaytimeHoursCommand(command)
            contains("da tarde", t) -> checkAfternoonHoursCommand(command)
            contains("da noite", t) -> checkNighttimeHoursCommand(command)
            contains("meio-dia", t) -> "12"
            contains("meia-noite", t) -> "00"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkDaytimeHoursCommand(command: String) = getTime(command, daytimeHoursMap)
    private fun checkAfternoonHoursCommand(command: String) = getTime(command, afternoonHoursMap)
    private fun checkNighttimeHoursCommand(command: String) = getTime(command, nightTimeHoursMap)

    fun checkMinutesCommand(command: String): String {
        return when {
            command.contains("e cinco", t) || command.contains(
                ":05", t
            ) -> "05"

            command.contains("e um quarto", t) || command.contains(
                ":15", t
            ) -> "15"

            command.contains("e meia", t) || command.contains(
                ":30", t
            ) -> "30"

            else -> "00"
        }
    }
}
