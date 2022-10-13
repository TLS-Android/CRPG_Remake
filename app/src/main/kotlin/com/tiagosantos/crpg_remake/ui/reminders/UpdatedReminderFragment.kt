package com.tiagosantos.crpg_remake.ui.reminders

import android.widget.LinearLayout
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.hourHelper

class UpdatedReminderFragment {

    private fun performActionWithVoiceCommand(command: String, view: LinearLayout){
        checkHoursCommand(command)
        checkMinutesCommand(command)

        when {
            command.contains("Lembrete", true) -> view.findViewById<ExpandableLayout>(R.id.expandable_lembrar).parentLayout.performClick()
            command.contains("Horas", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.requestFocus()
            }
            command.contains("Dia", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_dia).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_dia).parentLayout.requestFocus()
            }

            command.contains("Alerta", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_alerta).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_alerta).parentLayout.requestFocus()
            }
            command.contains("Notas", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_notas).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_notas).parentLayout.requestFocus()
            }
            command.contains("Cancelar", true) -> button_cancel.performClick()
            command.contains("Guardar", true) -> button_confirm.performClick()
            command.contains("Todos", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_lembrar).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_dia).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_alerta).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_notas).parentLayout.performClick()
            }
            command.contains("Tomar Medicação", true) -> button0.performClick()
            command.contains("Apanhar Transporte", true) -> button1.performClick()
            command.contains("Escolher Almoço", true)-> button2.performClick()
            command.contains("O Meu Lembrete", true)-> button3.performClick()
            command.contains("Som", true)-> imageButtonSom.performClick()
            command.contains("Vibrar", true) -> imageButtonVibrar.performClick()
            command.contains("Ambos", true) -> imageButtonAmbos.performClick()
            command.contains("Hoje", true) -> button_hoje.performClick()
            command.contains("Sempre", true) -> button_todos_dias.performClick()
            command.contains("Escolher Dias", true) -> button_personalizado.performClick()
        }
    }

    private fun checkHoursCommand(command: String) {
        when {
            hourHelper("yo","oito","8","da manhã") ->
                edit_hours.setText("08")
            (command.contains("oito", true) || command.contains("8", true))  && command.contains("da noite", true) ->edit_hours.setText("20")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da manhã", true) ->edit_hours.setText("09")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da noite", true) ->edit_hours.setText("21")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da manhã", true) ->edit_hours.setText("10")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da noite", true) ->edit_hours.setText("22")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da manhã", true) ->edit_hours.setText("11")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da noite", true) ->edit_hours.setText("23")
            (command.contains("meio-dia", true)  || command.contains("12", true)) ->edit_hours.setText("12")
            (command.contains("meia-noite", true)  || command.contains("0", true)) ->edit_hours.setText("00")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da manhã", true) ->edit_hours.setText("01")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da tarde", true) ->edit_hours.setText("13")
            (command.contains("duas", true) || command.contains("2", true)) && command.contains("da manhã", true) ->edit_hours.setText("02")
            (command.contains("duas", true)  || command.contains("2", true))&& command.contains("da tarde", true) ->edit_hours.setText("14")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da manhã", true) ->edit_hours.setText("03")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da tarde", true) ->edit_hours.setText("15")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da manhã", true) ->edit_hours.setText("04")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da tarde", true) ->edit_hours.setText("16")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da manhã", true) ->edit_hours.setText("05")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da tarde", true) ->edit_hours.setText("17")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da manhã", true) ->edit_hours.setText("06")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da tarde", true) ->edit_hours.setText("18")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da manhã", true) ->edit_hours.setText("07")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da tarde", true) ->edit_hours.setText("19")
        }

    }

    private fun checkMinutesCommand(command: String) {
        when {
            command.contains("e cinco", true) || command.contains(":05", true) ->edit_minutes.setText("05")
            command.contains("e um quarto", true) || command.contains(":15", true) ->edit_minutes.setText("15")
            command.contains("e meia", true) || command.contains(":30", true) ->edit_minutes.setText("30")
        }

    }

}