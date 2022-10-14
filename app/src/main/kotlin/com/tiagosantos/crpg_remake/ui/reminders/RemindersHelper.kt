package com.tiagosantos.crpg_remake.ui.reminders

import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*

class RemindersHelper {

    fun setButtonColorsReminder(view: ReminderFragmentBinding,pos: Int){
        expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundResource(R.drawable.layout_button_round_top)
        expandableLembrar.secondLayout.findViewById<Button>(R.id.button1).setBackgroundResource(R.color.md_blue_100)
        expandableLembrar.secondLayout.findViewById<Button>(R.id.button2).setBackgroundResource(R.color.md_blue_100)
        expandableLembrar.secondLayout.findViewById<Button>(R.id.button3).setBackgroundResource(R.drawable.layout_button_round_bottom)

        when(pos){
            1 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button0).setBackgroundResource(R.drawable.layout_button_round_top_selected)
            2 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button1).setBackgroundResource(R.color.md_blue_200)
            3 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button2).setBackgroundResource(R.color.md_blue_200)
            4 ->  expandableLembrar.secondLayout.findViewById<Button>(R.id.button3).setBackgroundResource(R.drawable.layout_button_round_bottom_selected)
        }

    }

    fun setButtonColorsDays(view: ReminderFragmentBinding, pos: Int){
        expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top)
        expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_100)
        expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom)

        when(pos){
            1 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top_selected)
            2 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_200)
            3 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom_selected)
        }
    }

    fun setSoundLogosVisible(
        view: LayoutSecondAlertaBinding,
        value: Int,
        soundVisible: Boolean,
        vibVisible: Boolean,
        bothVisible: Boolean
    ){
        alarmTypeButtonPressed = value
        cbSom.visibility = View.VISIBLE

        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = when {
            soundVisible -> View.VISIBLE
            !soundVisible -> View.INVISIBLE
        }
        cbVib.visibility = View.INVISIBLE

        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = when {
            vibVisible -> View.VISIBLE
            !vibVisible -> View.INVISIBLE
        }
        cbAmbos.visibility = View.INVISIBLE

        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = when {
            bothVisible -> View.VISIBLE
            !bothVisible -> View.INVISIBLE
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
            isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = View.VISIBLE
            !isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = View.INVISIBLE
        }

        when {
            isTextVisible -> viewLembrar.textEditPersonalizado.visibility = View.VISIBLE
            !isTextVisible -> viewLembrar.textEditPersonalizado.visibility = View.INVISIBLE
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
            isbuttonVisible -> View.VISIBLE
            !isbuttonVisible -> View.INVISIBLE
            else -> { println("hello") }
        }

        view.toggleButtonGroup.visibility = when {
            isGroupVisible -> View.VISIBLE
            !isGroupVisible -> View.INVISIBLE
            else -> { println("ok") }
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
                    this.parentLayout.performClick()
                    view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.requestFocus()
                }
                command.contains("Dia", true) -> {
                    view.expandableDia.
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
                command.contains("Escolher Almoço", true) -> button2.performClick()
                command.contains("O Meu Lembrete", true) -> button3.performClick()
                command.contains("Som", true) -> imageButtonSom.performClick()
                command.contains("Vibrar", true) -> imageButtonVibrar.performClick()
                command.contains("Ambos", true) -> imageButtonAmbos.performClick()
                command.contains("Hoje", true) -> button_hoje.performClick()
                command.contains("Sempre", true) -> button_todos_dias.performClick()
                command.contains("Escolher Dias", true) -> button_personalizado.performClick()
            }
        }
    }

    private fun checkHoursCommand(view: ReminderFragmentBinding, command: String) {
        when {
            (command.contains("oito", true) || command.contains(
                "8",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("08")
            (command.contains("oito", true) || command.contains(
                "8",
                true
            )) && command.contains("da noite", true) -> edit_hours.setText("20")
            (command.contains("nove", true) || command.contains(
                "9",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("09")
            (command.contains("nove", true) || command.contains(
                "9",
                true
            )) && command.contains("da noite", true) -> edit_hours.setText("21")
            (command.contains("dez", true) || command.contains(
                "10",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("10")
            (command.contains("dez", true) || command.contains(
                "10",
                true
            )) && command.contains("da noite", true) -> edit_hours.setText("22")
            (command.contains("onze", true) || command.contains(
                "11",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("11")
            (command.contains("onze", true) || command.contains(
                "11",
                true
            )) && command.contains("da noite", true) -> edit_hours.setText("23")
            (command.contains("meio-dia", true) || command.contains(
                "12",
                true
            )) -> edit_hours.setText("12")
            (command.contains("meia-noite", true) || command.contains(
                "0",
                true
            )) -> edit_hours.setText("00")
            (command.contains("uma", true) || command.contains(
                "1",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("01")
            (command.contains("uma", true) || command.contains(
                "1",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("13")
            (command.contains("duas", true) || command.contains(
                "2",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("02")
            (command.contains("duas", true) || command.contains(
                "2",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("14")
            (command.contains("três", true) || command.contains(
                "3",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("03")
            (command.contains("três", true) || command.contains(
                "3",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("15")
            (command.contains("quatro", true) || command.contains(
                "4",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("04")
            (command.contains("quatro", true) || command.contains(
                "4",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("16")
            (command.contains("cinco", true) || command.contains(
                "5",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("05")
            (command.contains("cinco", true) || command.contains(
                "5",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("17")
            (command.contains("seis", true) || command.contains(
                "6",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("06")
            (command.contains("seis", true) || command.contains(
                "6",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("18")
            (command.contains("sete", true) || command.contains(
                "7",
                true
            )) && command.contains("da manhã", true) -> edit_hours.setText("07")
            (command.contains("sete", true) || command.contains(
                "7",
                true
            )) && command.contains("da tarde", true) -> edit_hours.setText("19")
        }

    }

    private fun checkMinutesCommand(view: ReminderFragmentBinding, command: String) {
        when {
            command.contains("e cinco", true) || command.contains(
                ":05",
                true
            ) -> view.edit_minutes.setText("05")
            command.contains("e um quarto", true) || command.contains(
                ":15",
                true
            ) -> edit_minutes.setText("15")
            command.contains("e meia", true) || command.contains(
                ":30",
                true
            ) -> edit_minutes.setText("30")
        }

    }
    /*
    fun performActionWithVoiceCommand(view: ReminderFragmentBinding,command: String){
        when {
            command.contains("Abrir secção lembrete", true) ->
                view.expandableLembrar.parentLayout.performClick()
            command.contains("Abrir secção horas", true) ->
                view.expandableHoras.parentLayout.performClick()
            command.contains("Abrir secção dia", true) ->
                view.expandableDia.parentLayout.performClick()
            command.contains("Abrir secção alerta", true) ->
                view.expandableAlerta.parentLayout.performClick()
            command.contains("Abrir secção notas", true) ->
                view.expandableNotas.parentLayout.performClick()
        }
    }*/



}