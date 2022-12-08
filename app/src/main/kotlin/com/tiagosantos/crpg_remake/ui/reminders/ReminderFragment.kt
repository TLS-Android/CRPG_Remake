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
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.hide
import com.tiagosantos.common.ui.extension.invisible
import com.tiagosantos.common.ui.extension.setEmptyText
import com.tiagosantos.common.ui.extension.show
import com.tiagosantos.common.ui.model.AlarmFrequency.*
import com.tiagosantos.common.ui.model.AlarmType.SOM
import com.tiagosantos.common.ui.model.AlarmType.VIBRAR
import com.tiagosantos.common.ui.model.AlarmType.AMBOS
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.model.ReminderType.MEDICACAO
import com.tiagosantos.common.ui.model.ReminderType.TRANSPORTE
import com.tiagosantos.common.ui.model.ReminderType.REFEICAO
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.newReminder
import com.tiagosantos.crpg_remake.ui.reminders.helpers.HoursHelper

class ReminderFragment() : BaseModalFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminder_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_reminders,
        sharedPreferencesBooleanName = R.string.remindersHasRun.toString(),
    ),
    ttsSettings = TTSSettings(
        contextualHelp =  "Crie um novo lembrete",
    ),
    srSettings = SRSettings(
        isListening = false,
    )
) {

    private var _viewIntro: ReminderActivityIntroBinding? = null
    private val viewIntro get() = _viewIntro!!

    private var _viewSuccess: ReminderActivitySuccessBinding? = null
    private val viewSuccess get() = _viewSuccess!!

    private val reminderVM: ReminderViewModel by viewModels()

    private var hoursInt = 0
    private var minsInt = 0
    private var hoursMinutesFlag = false

    private lateinit var et: EditText
    private lateinit var etMin: EditText

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

    private fun setLayoutClickListeners(list: List<ExpandableLayout>) =
        viewB.parentLayout.setOnClickListener{ list.forEach { it.performClick() } }

    private fun setExpandablesClickListeners(list: List<ExpandableLayout>) {
        list.forEach { it -> it.parentLayout.setOnClickListener { it.performClick()} } }

    private var successFlag = alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
            && lembrarButtonPressed != 0 && hoursMinutesFlag

    private fun launchIntent() {
        if (activity?.packageManager?.let { it1 ->
            reminderVM.alarmIntent.resolveActivity(it1)
        } != null) {
            startActivity(reminderVM.alarmIntent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewIntro){
            reminderIntroHintLayout.show()
            createReminderActionButton.setOnClickListener { reminderIntroHintLayout.hide() }
        }

        with(viewB) {
            setLayoutClickListeners(listOf(expandableDia, expandableLembrar, expandableDia))
            setExpandablesClickListeners(listOf(
                expandableHoras,
                expandableNotas,
                expandableAlerta,
                expandableDia
            ))

            when{
                successFlag || hoursInt in 0..23 -> println("Hello")

            }

            with(viewSuccess){
                if (successFlag) {
                    reminderVM.addReminder(newReminder)
                    if (reminderVM.flagReminderAdded) {
                        avisoCampos.hide()
                        successLayout.show()
                        buttonOk.setOnClickListener { successLayout.hide() }
                        launchIntent()
                    }
                } else if (hoursInt in 0..23 || minsInt in 0.. 59) {
                    avisoCampos.run { text = context.getString(R.string.horas_invalidas); show() }
                } else {
                    avisoCampos.run { text = "Campos obrigatorios em falta!"; show() }
                }
            }
        }
        setupUI(reminderVM)
    }

    /** Kotlin function parameters are read-only values and are not assignable. **/
    private fun setupUI(reminderVM: ReminderViewModel) {
        with(viewB){
            with(secondLembrar){
                button0.setOnClickListener { setLembrarLayout(
                    this,
                    1,
                    isVisible = true,
                    isTextVisible = true,
                ) }
                button1.setOnClickListener { setLembrarLayout(
                    this, 2, isVisible = false, isTextVisible = false) }
                button2.setOnClickListener { setLembrarLayout(
                    this, 3,  isVisible = false, isTextVisible = false) }
                button3.setOnClickListener { setLembrarLayout(
                    this, 4, isVisible = true, isTextVisible = true) }
            }

            with(secondHoras){
                et = editHours.apply {
                    filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))
                }
                etMin = editMinutes.apply {
                    filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2))
                }
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


                /** ----- CANCELAR  ------ **/

                buttonCancel.setOnClickListener {
                    avisoCampos.hide()

                    listOf(checkboxSom,checkboxVibrar,checkboxAmbos).forEach { it.hide() }
                    lembrarButtonPressed = 0
                    alarmTypeButtonPressed = 0
                    alarmFreqButtonPressed = 0

                    with(secondHoras){
                        editHours.setEmptyText()
                        editMinutes.setEmptyText()
                    }

                    secondNotas.editTextNotes.setEmptyText()
                }

            }
            
            /** ----- CONFIRMAR ------ **/

            buttonConfirm.setOnClickListener {
                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    reminderVM.setTime(et, etMin)
                    hoursInt = secondHoras.editHours.text.toString().toInt()
                    minsInt = secondHoras.editMinutes.text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.run { text = "Valor em falta"; show() }
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

                    setTypeAndFrequency(newReminder)
                }

            }

        }

    }

    private fun setTypeAndFrequency(newReminder: Reminder){
        with(newReminder){
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
        cbSom.show()
        cbVib.invisible()

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
        with(viewLembrar){
            lembrarButtonPressed = value
            setButtonColorsReminder(this, lembrarButtonPressed)
            when {
                isVisible -> inserirTituloLembretePersonalizado.show()
                !isVisible -> inserirTituloLembretePersonalizado.invisible()
            }

            when {
                isTextVisible -> textEditPersonalizado.show()
                !isTextVisible -> textEditPersonalizado.invisible()
            }

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

    private fun clickAndFocus() { expandable: ExpandableLayout ->
        expandable.performClick(); expandable.requestFocus() }

    private fun performActionWithVoiceCommand(
        view: ReminderFragmentBinding,
        command: String,
    ) {
        helper.run { checkHoursCommand(view, command); checkMinutesCommand(view, command) }

        with(view) {
            mapOf(
                "Horas" to expandableHoras.run { clickAndFocus() },
                "Dia" to expandableDia.run { clickAndFocus() },
                "Alerta" to expandableAlerta.run { clickAndFocus() },
                "Notas" to expandableNotas.run { clickAndFocus() },
                "Lembrete" to parentLayout.performClick(),
                "Cancelar" to buttonCancel.performClick(),
                "Guardar" to buttonConfirm.performClick(),
            ).getOrElse(command) { println("No command found.") }

            with(secondLembrar) {
                mapOf(
                    "Tomar Medicação" to button0, "Apanhar Transporte" to button1,
                    "Escolher Almoço" to button2, "O Meu Lembrete" to button3,
                )
            }[command]?.performClick() ?: println("hello")

            with(secondLembrar) {
                mapOf(
                    "Tomar Medicação" to button0, "Apanhar Transporte" to button1,
                    "Escolher Almoço" to button2, "O Meu Lembrete" to button3,
                )
            }[command]?.performClick() ?: println("hello")

            with(secondAlerta) {
                mapOf(
                    "Som" to imageButtonSom,
                    "Vibrar" to imageButtonVibrar,
                    "Ambos" to imageButtonAmbos,
                )
            }[command]?.performClick() ?: println("hello")

            with(secondDia) {
                mapOf(
                    "Hoje" to buttonHoje.performClick(),
                    "Sempre" to buttonTodosDias.performClick(),
                    "Escolher Dias" to buttonPersonalizado.performClick(),
                )
            }.getOrElse(command) { println("No command found.") }

            if (command.contains("Todos", true)) {
                listOf(
                    expandableLembrar, expandableDia, expandableHoras,
                    expandableAlerta, expandableNotas
                ).forEach { _ -> clickAndFocus() }
            }
        }
    }

    fun toggleLayout(list: List<ExpandableLayout>) = viewB.parentLayout.setOnClickListener{
        list.forEach { it.performClick() }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}
