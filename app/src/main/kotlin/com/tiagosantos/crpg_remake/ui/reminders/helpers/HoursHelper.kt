package com.tiagosantos.crpg_remake.ui.reminders.helpers

import com.tiagosantos.crpg_remake.databinding.*

class HoursHelper(
    view: ReminderFragmentBinding,
    command: String,
) {
    val t = true

    private fun checkHoursCommand(
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

    private fun checkDaytimeHoursCommand(
        view: ReminderFragmentBinding,
        command: String
    ) {
        with(view.secondHoras) {
            when {
                command.contains("uma") || command.contains(
                    "1", t) -> editHours.setText("01")

                command.contains("duas") || command.contains(
                    "2", t) -> editHours.setText("02")

                command.contains("três") || command.contains(
                    "3", t) -> editHours.setText("03")

                command.contains("quatro") || command.contains(
                    "4",) -> editHours.setText("04")

                command.contains("cinco") || command.contains(
                    "5", t) -> editHours.setText("05")

                command.contains("seis") || command.contains(
                    "6", t) -> editHours.setText("06")

                command.contains("sete") || command.contains(
                    "7", t) -> editHours.setText("07")

                command.contains("oito", t) || command.contains(
                    "8", t) -> editHours.setText("08")

                command.contains("nove", t) || command.contains(
                    "9", t) -> editHours.setText("09")

                command.contains("dez", t) || command.contains(
                    "10", t) -> editHours.setText("10")

                command.contains("onze") || command.contains(
                    "11", t) -> editHours.setText("11")
            }
        }
    }

    private fun checkAfternoonHoursCommand(
        view: ReminderFragmentBinding,
        command: String
    ) {
        with(view.secondHoras) {
            when {
                command.contains("uma", t) || command.contains(
                    "1", t) -> editHours.setText("13")

                command.contains("duas") || command.contains(
                    "2", t) -> editHours.setText("14")

                command.contains("três") || command.contains(
                    "3", t) -> editHours.setText("15")

                command.contains("quatro") || command.contains(
                    "4", t) -> editHours.setText("16")

                command.contains("cinco") || command.contains(
                    "5", t) -> editHours.setText("17")

                command.contains("seis") || command.contains(
                    "6", t) -> editHours.setText("18")

                command.contains("sete") || command.contains(
                    "7", t) -> editHours.setText("19")
            }
        }
    }

    private fun checkNighttimeHoursCommand(
        view: ReminderFragmentBinding,
        command: String
    ) {
        with(view.secondHoras) {
            when {
                command.contains("oito",t) || command.contains("8",t) -> editHours.setText("20")
                command.contains("nove") || command.contains(
                    "9", t) -> editHours.setText("21")
                command.contains("dez") || command.contains(
                    "10", t) -> editHours.setText("22")
                command.contains("onze") || command.contains(
                    "11", t) -> editHours.setText("23")
            }
        }
    }

    private fun checkMinutesCommand(view: ReminderFragmentBinding, command: String) {
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