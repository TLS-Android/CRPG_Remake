package com.tiagosantos.crpg_remake.ui.reminders.helpers

import android.annotation.SuppressLint
import com.tiagosantos.common.ui.utils.Constants.t
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.afternoonHoursMap
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.daytimeHoursMap
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.nightTimeHoursMap

object HoursHelper {

    /**
    val colorMap = mapOf (
        'A' to R.color.colorBgShade1,
        'B' to R.color.colorBgShade2,
        ...
    ...
    )

    val char = 'G'
    val bgColor = colorMap[char] ?: R.color.colorBgShade27
    background.setTint(ContextCompat.getColor(context, bgColor)

    */

    private fun getTime(
        command: String,
        map: Map<String, String>,
        view: ReminderFragmentBinding,
    ) {
        with(view.secondHoras) {
            val value = map[command]
            editHours.setText(value)
        }
    }

        fun checkHoursCommand(
            view: ReminderFragmentBinding,
            command: String
        ) {

            with(view.secondHoras) {
                when {
                    command.contains("da manhã", t) -> checkDaytimeHoursCommand(view, command)
                    command.contains("da tarde", t) -> checkAfternoonHoursCommand(view, command)
                    command.contains("da noite", t) -> checkNighttimeHoursCommand(view, command)
                    command.contains("meio-dia", t) -> editHours.setText("12")
                    command.contains("meia-noite", t) -> editHours.setText("00")
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun checkDaytimeHoursCommand(view: ReminderFragmentBinding, command: String)
                = getTime(command, daytimeHoursMap, view)

        private fun checkAfternoonHoursCommand(view: ReminderFragmentBinding, command: String)
                = getTime(command, afternoonHoursMap, view)

        private fun checkNighttimeHoursCommand(view: ReminderFragmentBinding, command: String)
                = getTime(command, nightTimeHoursMap, view)


        fun checkMinutesCommand(
            view: ReminderFragmentBinding,
            command: String
        ) {
            with(view.secondHoras.editMinutes) {
                when {
                    command.contains("e cinco", t) || command.contains(
                        ":05", t) -> setText("05")
                    command.contains("e um quarto", t) || command.contains(
                        ":15", t) -> setText("15")
                    command.contains("e meia", t) || command.contains(
                        ":30", t) -> setText("30")
                    else -> {}
                }
            }
        }

}
