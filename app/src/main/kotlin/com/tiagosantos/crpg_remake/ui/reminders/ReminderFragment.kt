package com.tiagosantos.crpg_remake.ui.reminders

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.skydoves.expandablelayout.ExpandableLayout
import com.tiagosantos.common.ui.model.AlarmFrequency
import com.tiagosantos.common.ui.model.AlarmType
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.crpg_remake.base.BaseFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.newReminder
import com.tiagosantos.crpg_remake.ui.reminders.helpers.HoursHelper
import java.util.*

class ReminderFragment : BaseFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminder_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_reminders,
        sharedPreferencesBooleanName = R.string.remindersHasRun.toString(),
    )
) {
    private lateinit var view: ReminderFragmentBinding
    private lateinit var viewIntro: ReminderActivityIntroBinding
    private lateinit var viewSuccess: ReminderActivitySuccessBinding

    var lembrarButtonPressed = 0
    var alarmTypeButtonPressed = 0
    var alarmFreqButtonPressed = 0
    private var startTimeString = EMPTY_STRING
    private var hoursMinutesFlag = false

    private var hoursInt = 0
    private var minsInt = 0

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    lateinit var cbSom: ImageView
    lateinit var cbVib: ImageView
    lateinit var cbAmbos: ImageView

    lateinit var helper : HoursHelper

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val reminderVM: ReminderViewModel by viewModels()
        setupUI(reminderVM)
        return view.root
    }

    private fun setupUI(reminderVM: ReminderViewModel) {

        with(viewIntro){
            reminderIntroHintLayout.visibility = VISIBLE
            createReminderActionButton.setOnClickListener {
                reminderIntroHintLayout.visibility = GONE
            }
        }

        with(view){

            parentLayout.setOnClickListener { expandableDia.toggleLayout() }
            parentLayout.setOnClickListener { expandableLembrar.toggleLayout() }
            parentLayout.setOnClickListener { expandableDia.toggleLayout() }

            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }
            expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }
            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }


            //Kotlin function parameters are read-only values and are not assignable.
            with(secondLembrar){
                button0.setOnClickListener { setLembrarLayout(
                    secondLembrar, 1,
                    isVisible = true,
                    isTextVisible = true,
                ) }
                button1.setOnClickListener { setLembrarLayout(
                    secondLembrar, 2, false, false) }
                button2.setOnClickListener { setLembrarLayout(
                    secondLembrar, 3, false, false) }
                button3.setOnClickListener { setLembrarLayout(
                    secondLembrar, 4, true, true) }
            }

            with(secondHoras){
                val et = editHours
                et.filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))

                val etMin = editMinutes
                etMin.filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2))
            }

            with(secondDia){
                buttonHoje.setOnClickListener {
                    setSecondLayout(this,1, false, isGroupVisible =  false) }
                buttonTodosDias.setOnClickListener {
                    setSecondLayout(this,2, false, isGroupVisible = false) }
                buttonPersonalizado.setOnClickListener {
                    setSecondLayout(this,3, true, isGroupVisible = true) }
            }

            with(secondAlerta){
                    cbSom = checkboxSom
                    cbVib = checkboxVibrar
                    cbAmbos = checkboxAmbos

                    imageButtonSom.setOnClickListener{
                        setSoundLogosVisible(
                            view = view.secondDia,
                            1,
                            soundVisible = true,
                            vibVisible = false,
                            bothVisible = false) }
                    imageButtonVibrar.setOnClickListener{
                        setSoundLogosVisible(view.secondDia,2,
                            false, true, false) }
                    imageButtonAmbos.setOnClickListener{
                        setSoundLogosVisible(view.secondDia,3,
                            false, false, true) }
            }

            //--------------------- CANCELAR ---------------------------------------

            val avisoCampos = avisoCampos
            val buttonCancel = buttonCancel

            buttonCancel.setOnClickListener {
                avisoCampos.visibility = GONE

                val setZero: (Int, Int, Int) -> Int =
                    { lembrarButtonPressed: Int, alarmTypeButtonPressed: Int,
                      alarmFreqButtonPressed: Int -> 0 }

                lembrarButtonPressed = 0
                alarmTypeButtonPressed = 0
                alarmFreqButtonPressed = 0

                //reset set Hours section
                with(secondHoras){
                    editHours.setText(EMPTY_STRING)
                    editMinutes.setText(EMPTY_STRING)
                }

                val a = { i: Int -> i + 1 }

                // reset alarmType section

                cbSom.visibility = INVISIBLE
                cbVib.visibility = INVISIBLE
                cbAmbos.visibility = INVISIBLE

                secondNotas.editTextNotes.setText(EMPTY_STRING)
            }

            //------------------------- CONFIRMAR -------------------------------------------------

            buttonConfirm.setOnClickListener {
                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    reminderVM.startTimeHours = et.text.toString()
                    reminderVM.startTimeMin = etMin.text.toString()
                    startTimeString = reminderVM.startTimeHours.plus(reminderVM.startTimeMin)
                    hoursInt = secondHoras.editHours.text.toString().toInt()
                    minsInt = secondHoras.editMinutes.text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.run { text = "Valor em falta"; visibility = VISIBLE }
                }

                with(reminderVM.newReminder) {
                    fun updateButton(title: String, reminderType: ReminderType) {
                        this.title = title
                        reminder_type = reminderType
                    }

                    when (lembrarButtonPressed) {
                        1 -> { updateButton("Tomar medicacao", ReminderType.MEDICACAO) }
                        2 -> { updateButton("Apanhar bus do CRPG", ReminderType.TRANSPORTE) }
                        3 -> { updateButton("Lembrar escolha de almoço", ReminderType.REFEICAO) }
                        4 -> { updateButton(
                            secondLembrar.textEditPersonalizado.text.toString(),
                            ReminderType.PERSONALIZADO)
                        } else -> { println("lembrarButtonPressed is neither one of the values") }
                    }

                    with(secondDia){
                        val materialButtonToggleGroup = toggleButtonGroup
                        val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                        for (id in ids) {
                            val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                            when (resources.getResourceName(materialButton.id).takeLast(3)) {
                                "Seg" -> reminderVM.weekDaysBoolean[0] = true
                                "Ter" -> reminderVM.weekDaysBoolean[1] = true
                                "Qua" -> reminderVM.weekDaysBoolean[2] = true
                                "Qui" -> reminderVM.weekDaysBoolean[3] = true
                                "Sex" -> reminderVM.weekDaysBoolean[4] = true
                                "Sab" -> reminderVM.weekDaysBoolean[5] = true
                                "Dom" -> reminderVM.weekDaysBoolean[6] = true
                            }
                        }
                    }

                    when (alarmTypeButtonPressed) {
                        1 -> alarm_type = AlarmType.SOM
                        2 -> alarm_type = AlarmType.VIBRAR
                        3 -> alarm_type = AlarmType.AMBOS
                    }

                    when (alarmFreqButtonPressed) {
                        1 -> alarm_freq = AlarmFrequency.HOJE
                        2 -> alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                        3 -> alarm_freq = AlarmFrequency.PERSONALIZADO
                        else -> { println("hello") }
                    }
                }

                if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
                    && lembrarButtonPressed != 0 && hoursMinutesFlag
                ) {
                    reminderVM.addReminder(newReminder)
                    if (reminderVM.flagReminderAdded) {
                        avisoCampos.visibility = GONE
                        viewSuccess.successLayout.visibility = VISIBLE
                        viewSuccess.buttonOk.setOnClickListener {
                            viewSuccess.successLayout.visibility = GONE
                        }
                        if (activity?.packageManager?.let { it1 ->
                                reminderVM.alarmIntent.resolveActivity(it1) } != null) {
                            startActivity(reminderVM.alarmIntent)
                        }
                    }
                } else if (hoursInt > 23 || minsInt > 59) {
                    avisoCampos.text = "Horas ou minutos invalidos"
                    avisoCampos.visibility = VISIBLE
                } else {
                    avisoCampos.text = "Campos obrigatorios em falta!"
                    avisoCampos.visibility = VISIBLE
                }
            }

        }

    }

    private fun setButtonColorsReminder(view: LayoutSecondLembrarBinding, pos: Int){

        with(view){
            button0.setBackgroundResource(R.color.md_blue_100)
            button1.setBackgroundResource(R.color.md_blue_100)
            button2.setBackgroundResource(R.color.md_blue_100)
            button3.setBackgroundResource(R.color.md_blue_100)

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
            buttonHoje.setBackgroundResource(R.drawable.layout_button_round_top)
            buttonTodosDias.setBackgroundResource(R.color.md_blue_100)
            buttonPersonalizado.setBackgroundResource(R.drawable.layout_button_round_bottom)

            when (pos) {
                1 -> buttonHoje.setBackgroundResource(R.drawable.layout_button_round_top)
                2 -> buttonTodosDias.setBackgroundResource(R.color.md_blue_100)
                3 -> buttonPersonalizado.setBackgroundResource(R.drawable.layout_button_round_bottom)
            }
        }
    }

    private fun setSoundLogosVisible(
        view: LayoutSecondDiaBinding,
        value: Int,
        soundVisible: Boolean,
        vibVisible: Boolean,
        bothVisible: Boolean
    ){
        alarmTypeButtonPressed = value
        cbSom.visibility = VISIBLE
        cbVib.visibility = INVISIBLE

        view.buttonSelecionarDias.visibility = when {
            soundVisible || vibVisible || bothVisible -> VISIBLE
            !soundVisible || !vibVisible || !bothVisible  -> INVISIBLE
            else -> { INVISIBLE }
        }

    }

    fun setLembrarLayout(
        viewLembrar: LayoutSecondLembrarBinding,
        value: Int,
        isVisible: Boolean,
        isTextVisible: Boolean,
    ){
        lembrarButtonPressed = value
        setButtonColorsReminder(viewLembrar, lembrarButtonPressed)
        when {
            isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = VISIBLE
            !isVisible -> viewLembrar.inserirTituloLembretePersonalizado.visibility = INVISIBLE
        }

        when {
            isTextVisible -> viewLembrar.textEditPersonalizado.visibility = VISIBLE
            !isTextVisible -> viewLembrar.textEditPersonalizado.visibility = INVISIBLE
        }
    }

    private fun setSecondLayout(
        view: LayoutSecondDiaBinding,
        value: Int,
        isbuttonVisible: Boolean,
        isGroupVisible: Boolean,
    ){
        alarmFreqButtonPressed = value
        setButtonColorsDays(view, alarmFreqButtonPressed)
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

    val more : (String, Int) -> String = { str, int -> str + int }

    private fun clickAndFocus() = { expandable: ExpandableLayout ->
        expandable.performClick(); expandable.requestFocus() }

    private fun performActionWithVoiceCommand(
        view: ReminderFragmentBinding,
        command: String,
    ) {
        helper.checkHoursCommand(view, command)
        helper.checkMinutesCommand(view, command)

        with(view){
            when {
                command.contains("Lembrete", true) -> parentLayout.performClick()
                command.contains("Horas", true) -> expandableHoras.run { clickAndFocus() }
                command.contains("Dia", true) -> expandableDia.run { clickAndFocus() }
                command.contains("Alerta", true) ->  expandableAlerta.run { clickAndFocus() }
                command.contains("Notas", true) ->  expandableNotas.run { clickAndFocus() }
                command.contains("Cancelar", true) -> buttonCancel.performClick()
                command.contains("Guardar", true) -> buttonConfirm.performClick()
                command.contains("Todos", true) -> {
                    expandableLembrar.performClick()
                    expandableDia.performClick()
                    expandableHoras.performClick()
                    expandableAlerta.performClick()
                    expandableNotas.performClick()
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

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }


}
