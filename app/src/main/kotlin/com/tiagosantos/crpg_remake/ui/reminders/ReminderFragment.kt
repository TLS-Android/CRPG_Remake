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
import com.tiagosantos.common.ui.model.AlarmFrequency.*
import com.tiagosantos.common.ui.model.AlarmType.SOM
import com.tiagosantos.common.ui.model.AlarmType.VIBRAR
import com.tiagosantos.common.ui.model.AlarmType.AMBOS
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.model.ReminderType.MEDICACAO
import com.tiagosantos.common.ui.model.ReminderType.TRANSPORTE
import com.tiagosantos.common.ui.model.ReminderType.REFEICAO
import com.tiagosantos.crpg_remake.base.BaseFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.newReminder
import com.tiagosantos.crpg_remake.ui.reminders.helpers.HoursHelper

class ReminderFragment : BaseFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminder_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_reminders,
        sharedPreferencesBooleanName = R.string.remindersHasRun.toString(),
    )
) {

    private var _viewIntro: ReminderActivityIntroBinding? = null
    private val viewIntro get() = _viewIntro!!

    private var _viewSuccess: ReminderActivitySuccessBinding? = null
    private val viewSuccess get() = _viewSuccess!!

    private var hoursMinutesFlag = false

    private val reminderVM: ReminderViewModel by viewModels()

    private var hoursInt = 0
    private var minsInt = 0

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    lateinit var cbSom: ImageView
    lateinit var cbVib: ImageView
    lateinit var cbAmbos: ImageView

    private lateinit var helper : HoursHelper

    companion object{
        val weekMap = mapOf(
            "Seg" to 1, "Ter" to 2, "Qua" to 3,
            "Qui" to 4, "Sex" to 5, "Sab" to 6, "Dom" to 7
        )

        var lembrarButtonPressed = 0
        var alarmTypeButtonPressed = 0
        var alarmFreqButtonPressed = 0
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _viewIntro = ReminderActivityIntroBinding.inflate(inflater, container, false)
        _viewSuccess = ReminderActivitySuccessBinding.inflate(inflater, container, false)
        return viewB.root
    }

    var successFlag = alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
            && lembrarButtonPressed != 0 && hoursMinutesFlag

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewIntro){
            reminderIntroHintLayout.visibility = VISIBLE
            createReminderActionButton.setOnClickListener {
                reminderIntroHintLayout.visibility = GONE
            }
        }

        with(viewB) {
            parentLayout.setOnClickListener { expandableDia.toggleLayout() }
            parentLayout.setOnClickListener { expandableLembrar.toggleLayout() }
            parentLayout.setOnClickListener { expandableDia.toggleLayout() }

            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }
            expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }
            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }

            with(viewSuccess){
                if (successFlag) {
                    reminderVM.addReminder(newReminder)
                    if (reminderVM.flagReminderAdded) {
                        avisoCampos.visibility = GONE
                        successLayout.visibility = VISIBLE
                        buttonOk.setOnClickListener {
                            successLayout.visibility = GONE
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

        setupUI(reminderVM)
    }

    private fun setupUI(reminderVM: ReminderViewModel) {
        with(viewB){
            /** Kotlin function parameters are read-only values and are not assignable. **/
            with(secondLembrar){
                button0.setOnClickListener { setLembrarLayout(
                    secondLembrar, 1,
                    isVisible = true,
                    isTextVisible = true,
                ) }
                button1.setOnClickListener { setLembrarLayout(
                    secondLembrar, 2, isVisible = false, isTextVisible = false) }
                button2.setOnClickListener { setLembrarLayout(
                    secondLembrar, 3,  isVisible = false, isTextVisible = false) }
                button3.setOnClickListener { setLembrarLayout(
                    secondLembrar, 4, isVisible = true, isTextVisible = true) }
            }

            with(secondHoras){
                val et = editHours.apply { filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2)) }
                val etMin = editMinutes.apply { filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2)) }
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
                            view = secondDia,
                            1,
                            soundVisible = true,
                            vibVisible = false,
                            bothVisible = false) }
                    imageButtonVibrar.setOnClickListener{
                        setSoundLogosVisible(secondDia,2,
                            false, true, false) }
                    imageButtonAmbos.setOnClickListener{
                        setSoundLogosVisible(secondDia,3,
                            false, false, true) }
            }

            /**----- CANCELAR  ------**/

            buttonCancel.setOnClickListener {
                avisoCampos.visibility = GONE
                cbSom.visibility = INVISIBLE
                cbVib.visibility = INVISIBLE
                cbAmbos.visibility = INVISIBLE
                lembrarButtonPressed = 0
                alarmTypeButtonPressed = 0
                alarmFreqButtonPressed = 0

                with(secondHoras){
                    editHours.setText(EMPTY_STRING)
                    editMinutes.setText(EMPTY_STRING)
                }

                secondNotas.editTextNotes.setText(EMPTY_STRING)
            }

            /** ----- CONFIRMAR ------**/

            buttonConfirm.setOnClickListener {
                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    reminderVM.setTime(et, etMin)
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
                        1 -> { updateButton("Tomar medicacao", MEDICACAO) }
                        2 -> { updateButton("Apanhar bus do CRPG", TRANSPORTE) }
                        3 -> { updateButton("Lembrar escolha de almoço", REFEICAO) }
                        4 -> { updateButton(
                            secondLembrar.textEditPersonalizado.text.toString(),
                            ReminderType.PERSONALIZADO)
                        } else -> { println("lembrarButtonPressed is neither!") }
                    }

                    with(secondDia){
                        for (id in toggleButtonGroup.checkedButtonIds) {
                            val materialButton: MaterialButton = toggleButtonGroup.findViewById(id)
                            weekMap[resources.getResourceName(materialButton.id).takeLast(3)]
                        }
                    }

                    alarm_type = when (alarmTypeButtonPressed) {
                        1 -> SOM
                        2 -> VIBRAR
                        3 -> AMBOS
                        else -> { SOM }
                    }

                    alarm_freq = when (alarmFreqButtonPressed) {
                        1 -> HOJE
                        2 -> TODOS_OS_DIAS
                        3 -> PERSONALIZADO
                        else -> { HOJE }
                    }
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

    private fun setLembrarLayout(
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
                    expandableLembrar.performClick(); expandableDia.performClick(); expandableHoras.performClick()
                    expandableAlerta.performClick(); expandableNotas.performClick()
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
