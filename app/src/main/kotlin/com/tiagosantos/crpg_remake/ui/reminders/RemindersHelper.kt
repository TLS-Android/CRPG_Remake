package com.tiagosantos.crpg_remake.ui.reminders

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*

class RemindersHelper {

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

        with(view.secondHoras){
            when {
                (command.contains("oito", true) || command.contains(
                    "8",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("08")

                (command.contains("oito", true) || command.contains(
                    "8",
                    true
                )) && command.contains("da noite", true) -> editHours.setText("20")
                (command.contains("nove", true) || command.contains(
                    "9",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("09")
                (command.contains("nove", true) || command.contains(
                    "9",
                    true
                )) && command.contains("da noite", true) -> editHours.setText("21")
                (command.contains("dez", true) || command.contains(
                    "10",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("10")
                (command.contains("dez", true) || command.contains(
                    "10",
                    true
                )) && command.contains("da noite", true) -> editHours.setText("22")
                (command.contains("onze", true) || command.contains(
                    "11",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("11")
                (command.contains("onze", true) || command.contains(
                    "11",
                    true
                )) && command.contains("da noite", true) -> editHours.setText("23")
                (command.contains("meio-dia", true) || command.contains(
                    "12",
                    true
                )) -> editHours.setText("12")
                (command.contains("meia-noite", true) || command.contains(
                    "0",
                    true
                )) -> editHours.setText("00")
                (command.contains("uma", true) || command.contains(
                    "1",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("01")
                (command.contains("uma", true) || command.contains(
                    "1",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("13")
                (command.contains("duas", true) || command.contains(
                    "2",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("02")
                (command.contains("duas", true) || command.contains(
                    "2",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("14")
                (command.contains("três", true) || command.contains(
                    "3",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("03")
                (command.contains("três", true) || command.contains(
                    "3",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("15")
                (command.contains("quatro", true) || command.contains(
                    "4",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("04")
                (command.contains("quatro", true) || command.contains(
                    "4",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("16")
                (command.contains("cinco", true) || command.contains(
                    "5",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("05")
                (command.contains("cinco", true) || command.contains(
                    "5",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("17")
                (command.contains("seis", true) || command.contains(
                    "6",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("06")
                (command.contains("seis", true) || command.contains(
                    "6",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("18")
                (command.contains("sete", true) || command.contains(
                    "7",
                    true
                )) && command.contains("da manhã", true) -> editHours.setText("07")
                (command.contains("sete", true) || command.contains(
                    "7",
                    true
                )) && command.contains("da tarde", true) -> editHours.setText("19")
                else -> {}
            }

        }
    }

    private fun checkMinutesCommand(view: ReminderFragmentBinding, command: String) {
        with(view.secondHoras.editMinutes){
            when {
                command.contains("e cinco", true) || command.contains(
                    ":05",
                    true) -> setText("05")
                command.contains("e um quarto", true) || command.contains(
                    ":15",
                    true) -> setText("15")
                command.contains("e meia", true) || command.contains(
                    ":30",
                    true) -> setText("30")
                else -> {}
            }

        }

    }

}