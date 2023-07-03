package com.tiagosantos.crpg_remake.ui.reminders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.*
import com.tiagosantos.common.ui.model.AlarmFrequency.*
import com.tiagosantos.common.ui.model.AlarmType.SOM
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.model.ReminderType.MEDICACAO
import com.tiagosantos.common.ui.model.ReminderType.TRANSPORTE
import com.tiagosantos.common.ui.model.ReminderType.REFEICAO
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.actionMap
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.helper.ResourcesProvider
import com.tiagosantos.crpg_remake.ui.FeatureType
import com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions.filterTime
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.newReminder
import com.tiagosantos.crpg_remake.ui.reminders.helpers.HoursHelper
import java.time.DayOfWeek

/**
 * You can't inherit an attribute, you have to inherit a style.
 *
 * Choose a button borderless style which matchs your theme to inherit.
 * All Android internal styles can be found at http://developer.android.com/reference/android/R.style.html
 */
@FragmentWithArgs
class ReminderFragment : BaseModalFragment<ReminderFragmentBinding>() {

    @Arg override var layoutId = R.layout.reminder_fragment

    //private val feature = FeatureType.LEMBRETES

    @delegate:Arg
    override val settings by lazy {
        FragmentSettings(
            appBarTitle = getString(R.string.title_reminders),
            sharedPreferencesBooleanName = getString(R.string.remindersHasRun),
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(contextualHelp =  R.string.contextual_reminder)
    }

    @delegate:Arg
    override val srSettings by lazy {
        SRSettings(
            commandList = feature!!.actionMap,
            isListening = false,
        )
    }

    private val viewModel: ReminderViewModel by viewModels()

    private var _viewIntro: ReminderFragmentIntroBinding? = null
    private val viewIntro get() = _viewIntro!!

    private var _viewSuccess: ReminderFragmentSuccessBinding? = null
    private val viewSuccess get() = _viewSuccess!!

    private lateinit var provider: ResourcesProvider

    private var hoursInt = 0
    private var minsInt = 0
    private var hoursMinutesFlag = false

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    companion object {
        val TAG = "REMINDER_FRAGMENT"

        /*
        val weekMap = mapOf(
            "Seg" to 1, "Ter" to 2, "Qua" to 3,
            "Qui" to 4, "Sex" to 5, "Sab" to 6, "Dom" to 7
        )*/

        var lembrarButtonPressed = 0
        var alarmTypeButtonPressed = 0
        var alarmFreqButtonPressed = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _viewIntro = ReminderFragmentIntroBinding.inflate(inflater, container, false)
        _viewSuccess = ReminderFragmentSuccessBinding.inflate(inflater, container, false)

        viewB.apply {
            actionList = mutableListOf(
                expandableHoras, expandableDia, expandableAlerta, expandableNotas, parentLayout,
                buttonCancel, buttonConfirm, childLembrar.buttonMedicacao, childLembrar.buttonTransporte, childLembrar.buttonAlmoco,
                childLembrar.buttonLembrete, childAlerta.imageButtonSom, childAlerta.imageButtonVibrar,
                childAlerta.imageButtonAmbos, childDia.buttonHoje, childDia.buttonTodosDias, childDia.buttonPersonalizado,
            )
        }
        return viewB.root
    }

    private var successFlag = alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
            && lembrarButtonPressed != 0 && hoursMinutesFlag

    private fun launchIntent() {
        if (activity?.packageManager?.let { viewModel.alarmIntent.resolveActivity(it) } != null) {
            startActivity(viewModel.alarmIntent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewIntro) {
            reminderIntroHintLayout.show()
            createReminderActionButton.setOnClickListener { reminderIntroHintLayout.hide() }
        }

        with(viewSuccess) {
            if (successFlag) {
                viewModel.addReminder(newReminder)
                if (viewModel.flagReminderAdded) {
                    viewB.avisoCampos.hide()
                    successLayout.show()
                    buttonOk.setOnClickListener { successLayout.hide() }
                    launchIntent()
                }
            } else if (hoursInt in 0..23 || minsInt in 0..59) {
                viewB.avisoCampos.run { text = getString(R.string.horas_invalidas); show() }
            } else {
                viewB.avisoCampos.run { text = getString(R.string.campos_obrigatorios); show() }
            }
        }

        setupUI()
    }

    private fun ReminderFragmentBinding.setupExpandables() {
        listOf(
            expandableDia,
            expandableLembrar,
            expandableDia
        ).setLayoutClickListeners(parentLayout)

        listOf(
            expandableHoras,
            expandableNotas,
            expandableAlerta,
            expandableDia
        ).setExpandablesClickListeners()

        when {successFlag || hoursInt in 0..23 -> println("Hello") }
    }

    /** Kotlin function parameters are read-only values and are not assignable. **/
    override fun setupUI() {
        with(viewB) {
            setupExpandables()
            with(childLembrar) {
                lembrarRadioGroup.addOnButtonCheckedListener { _, checkedId, _ ->
                    lembrarButtonPressed = checkedId
                    when (checkedId) {
                        R.id.button_medicacao, R.id.button_lembrete -> {
                            inserirTituloLembretePersonalizado.show()
                            textEditPersonalizado.show()
                        }

                        R.id.button_transporte, R.id.button_almoco -> {
                            inserirTituloLembretePersonalizado.invisible()
                            textEditPersonalizado.invisible()
                        }
                    }
                }

            }

            /** ---- FILTRO TIME INPUT -------- **/
            childHoras.apply {
                et = editHours.filterTime("00", "23")
                etMin = editMinutes.filterTime("00", "59")
            }

            with(childDia) {
                diaRadioGroup.addOnButtonCheckedListener { _, checkedId, _ ->
                    alarmFreqButtonPressed = checkedId; setButtonColorsDays(alarmFreqButtonPressed) }
            }

            with(childAlerta) {
                alertRadioGroup.setOnCheckedChangeListener { _, optionId -> alarmTypeButtonPressed = optionId
                    run {
                        when (optionId) {
                            R.id.imageButtonSom -> checkboxSom.show()
                            R.id.imageButtonVibrar -> checkboxVibrar.show()
                            R.id.imageButtonAmbos -> checkboxAmbos.show()
                        }
                    }
                }
            }

            /** ----- CANCELAR  ------ **/
            setupCancelButton()

            /** ----- CONFIRMAR ------ **/
            confirmButtonSetup()
        }

    }

    private fun confirmButtonSetup() =
        with(viewB) {
            buttonConfirm.setOnClickListener {
                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    viewModel.setTime(et, etMin)
                    hoursInt = childHoras.editHours.text.toString().toInt()
                    minsInt = childHoras.editMinutes.text.toString().toInt()
                    hoursMinutesFlag = true
                } else { avisoCampos.run { text = getString(R.string.valor_em_falta); show() } }

                with(viewModel.newReminder) {
                    fun updateButton(title: String, reminderType: ReminderType) {
                        this.title = title
                        this.reminderType = reminderType
                    }

                    when (lembrarButtonPressed) {
                        1 -> updateButton(getString(R.string.tomar_medicacao), MEDICACAO)
                        2 -> updateButton(getString(R.string.apanhar_bus), TRANSPORTE)
                        3 -> updateButton(getString(R.string.lembrar_escolha_almoco), REFEICAO)
                        4 -> { updateButton(
                                childLembrar.textEditPersonalizado.text.toString(),
                                ReminderType.PERSONALIZADO
                            )
                        } else -> { println("lembrarButtonPressed is neither!") }
                    }

                    with(childDia) {
                        for (id in toggleButtonGroup.checkedButtonIds) {
                            val materialButton: MaterialButton = toggleButtonGroup.findViewById(id)
                            for(day in DayOfWeek.values()) day.value
                            resources.getResourceName(materialButton.id)
                            //weekMap[resources.getResourceName(materialButton.id).takeLast(3)]
                        }
                    }
                    setTypeAndFrequency(newReminder)
                }

            }
        }


    private fun setupCancelButton() =
        with(viewB) {
            with(childAlerta) {
                buttonCancel.setOnClickListener {
                    avisoCampos.hide()

                    listOf(
                        checkboxSom,
                        checkboxVibrar,
                        checkboxAmbos
                    ).forEach { it.hide() }

                    lembrarButtonPressed = 0
                    alarmTypeButtonPressed = 0
                    alarmFreqButtonPressed = 0

                    with(childHoras) {
                        editHours.setEmptyText()
                        editMinutes.setEmptyText()
                    }

                    childNotas.editTextNotes.setEmptyText()
                }
            }
        }

    private fun setTypeAndFrequency(newReminder: Reminder) = newReminder.apply {
        provider.alarmTypeStates.getOrDefault(alarmTypeButtonPressed, SOM)
        provider.alarmFreqStates.getOrDefault(alarmFreqButtonPressed, PERSONALIZADO)
    }

    /** TO DO: USE COLOR STATE LIST **/
    private fun setButtonColorsDays(pos: Int) {
        with(viewB.childDia) {
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

    override fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String, Any>
    ) {
        HoursHelper.run {
            checkHoursCommand(command)
            checkMinutesCommand(command)
        }

        super.performActionWithVoiceCommand(command, actionMap)

        if (command.contains("Todos", true)) {
            viewB.expandableGroup.forEach { it.clickAndFocus() }
        }
    }
}
