package com.tiagosantos.crpg_remake.ui.reminders.helpers

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*

class RemindersHelper {

    val t = true

    private fun setButtonColorsReminder(view: LayoutSecondLembrarBinding, pos: Int){

        with(view){

            this.button0.setBackgroundResource(R.color.md_blue_100)
            this.button1.setBackgroundResource(R.color.md_blue_100)
            this.button2.setBackgroundResource(R.color.md_blue_100)
            this.button3.setBackgroundResource(R.color.md_blue_100)

            when(pos){
                1 -> button0.setBackgroundResource(R.color.md_blue_200)
                2 -> button1.setBackgroundResource(R.color.md_blue_200)
                3 -> button2.setBackgroundResource(R.color.md_blue_200)
                4 -> button0.setBackgroundResource(R.color.md_blue_200)
            }
        }

    }

    private fun setButtonColorsDays(view: LayoutSecondDiaBinding, pos: Int){

        with(view) {
            this.buttonHoje.setBackgroundResource(R.drawable.layout_button_round_top)
            this.buttonTodosDias.setBackgroundResource(R.color.md_blue_100)
            this.buttonPersonalizado.setBackgroundResource(R.drawable.layout_button_round_bottom)

            when (pos) {
                1 -> this.buttonHoje.setBackgroundResource(R.drawable.layout_button_round_top)
                2 -> this.buttonTodosDias.setBackgroundResource(R.color.md_blue_100)
                3 -> buttonPersonalizado.setBackgroundResource(R.drawable.layout_button_round_bottom)
            }
        }
    }


    fun setLembrarLayout(
        viewLembrar: LayoutSecondLembrarBinding,
        value: Int,
        isVisible: Boolean,
        isTextVisible: Boolean,
        lembrarButtonPressed: Int
    ){
        lembrarButtonPressed = value
        setButtonColorsReminder(lembrarButtonPressed)
        when {
            isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = VISIBLE
            !isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = INVISIBLE
        }

        when {
            isTextVisible -> viewLembrar.textEditPersonalizado.visibility = VISIBLE
            !isTextVisible -> viewLembrar.textEditPersonalizado.visibility = INVISIBLE
        }
    }

    fun setSecondLayout(
        view: LayoutSecondDiaBinding,
        value: Int,
        isbuttonVisible: Boolean,
        isGroupVisible: Boolean,
        alarmFreqButtonPressed: Int,
    ){
        alarmFreqButtonPressed = value
        setButtonColorsDays(alarmFreqButtonPressed)
        view.buttonSelecionarDias.visibility = when {
            isbuttonVisible -> VISIBLE
            !isbuttonVisible -> INVISIBLE
            else -> { INVISIBLE }
        }

        view.toggleButtonGroup.visibility = when {
            isGroupVisible -> VISIBLE
            !isGroupVisible -> INVISIBLE
            else -> { INVISIBLE }
        }
    }

    private fun performActionWithVoiceCommand(
        view: ReminderFragmentBinding,
        command: String,
    ) {
        checkHoursCommand(view, command)
        checkMinutesCommand(view, command)

        with(view){
            when {
                command.contains(
                    "Lembrete",
                    true
                ) -> this.parentLayout.performClick()
                command.contains("Horas", true) -> {
                    expandableHoras.run { performClick(); requestFocus() }
                }
                command.contains("Dia", true) -> {
                    expandableDia.run { performClick(); requestFocus() }
                }
                command.contains("Alerta", true) -> {
                    view.expandableAlerta.run { performClick(); requestFocus() }
                }
                command.contains("Notas", true) -> {
                    view.expandableNotas.run { performClick(); requestFocus() }
                }
                command.contains("Cancelar", true) -> buttonCancel.performClick()
                command.contains("Guardar", true) -> buttonConfirm.performClick()
                command.contains("Todos", true) -> {
                    view.expandableLembrar.performClick()
                    view.expandableDia.performClick()
                    view.expandableHoras.performClick()
                    view.expandableAlerta.performClick()
                    view.expandableNotas.performClick()
                }
                command.contains("Tomar Medicação", true) -> secondLembrar.button0.performClick()
                command.contains("Apanhar Transporte", true) -> secondLembrar.button1.performClick()
                command.contains("Escolher Almoço", true) -> secondLembrar.button2.performClick()
                command.contains("O Meu Lembrete", true) -> secondLembrar.button3.performClick()
                command.contains("Som", true) -> secondAlerta.imageButtonSom.performClick()
                command.contains("Vibrar", true) -> secondAlerta.imageButtonVibrar.performClick()
                command.contains("Ambos", true) -> secondAlerta.imageButtonAmbos.performClick()
                command.contains("Hoje", true) -> secondDia.buttonHoje.performClick()
                command.contains("Sempre", true) -> secondDia.buttonTodosDias.performClick()
                command.contains("Escolher Dias", true) -> secondDia.buttonPersonalizado.performClick()
                else -> {}
            }
        }
    }

    private fun checkHoursCommand(
        view: ReminderFragmentBinding,
        command: String)
    {
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
                    "1",t) -> editHours.setText("01")

                command.contains("duas") || command.contains(
                    "2", t) -> editHours.setText("02")

                command.contains("três") || command.contains(
                    "3", t) -> editHours.setText("03")

                command.contains("quatro") || command.contains(
                    "4", ) -> editHours.setText("04")

                command.contains("cinco") || command.contains(
                    "5", t) -> editHours.setText("05")

                command.contains("seis") || command.contains(
                    "6", t) -> editHours.setText("06")

                command.contains("sete") || command.contains(
                    "7", t) -> editHours.setText("07")

                command.contains("oito",t) || command.contains(
                    "8",t)  -> editHours.setText("08")

                command.contains("nove",t) || command.contains(
                    "9",t) -> editHours.setText("09")

                command.contains("dez",t) || command.contains(
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
                command.contains("oito") || command.contains("8",) -> editHours.setText("20")
                command.contains("nove") || command.contains(
                    "9",
                    t
                ) -> editHours.setText("21")
                command.contains("dez") || command.contains(
                    "10", t
                ) -> editHours.setText("22")
                command.contains("onze") || command.contains(
                    "11", t
                ) -> editHours.setText("23")
            }
        }
    }

    private fun checkMinutesCommand(view: ReminderFragmentBinding, command: String) {
        with(view.secondHoras.editMinutes){
            when {
                command.contains("e cinco", t) || command.contains(":05",
                    t) -> setText("05")
                command.contains("e um quarto", t) || command.contains(":15",
                    t) -> setText("15")
                command.contains("e meia", t) || command.contains(":30",
                    t) -> setText("30")
                else -> {}
            }

        }

    }

}