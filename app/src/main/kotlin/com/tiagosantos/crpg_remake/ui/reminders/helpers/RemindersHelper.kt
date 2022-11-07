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



}