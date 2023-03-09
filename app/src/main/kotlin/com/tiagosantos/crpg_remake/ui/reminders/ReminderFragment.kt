package com.tiagosantos.crpg_remake.ui.reminders

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.extension.*
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

class ReminderFragment : BaseModalFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminder_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_reminders,
        sharedPreferencesBooleanName = R.string.remindersHasRun.toString(),
    ),
) {

    @Arg
    override var ttsSettings = TTSSettings(
        contextualHelp =  "Crie um novo lembrete",
    )

    @Arg
    override var srSettings = SRSettings(
        commandList = listOf(
            "Horas", "Dia", "Alerta", "Notas", "Lembrete", "Cancelar", "Guardar",
            "Tomar Medicação", "Apanhar Transporte",
            "Escolher Almoço", "O Meu Lembrete",
            "Som","Vibrar","Ambos","Hoje","Sempre","Escolher Dias"
        ),
        isListening = false,
    )

    private val viewModel: ReminderViewModel by viewModels()

    private var _viewIntro: ReminderActivityIntroBinding? = null
    private val viewIntro get() = _viewIntro!!

    private var _viewSuccess: ReminderActivitySuccessBinding? = null
    private val viewSuccess get() = _viewSuccess!!

    private var hoursInt = 0
    private var minsInt = 0
    private var hoursMinutesFlag = false

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    companion object {
        val weekMap = mapOf(
            "Seg" to 1, "Ter" to 2, "Qua" to 3,
            "Qui" to 4, "Sex" to 5, "Sab" to 6, "Dom" to 7
        )

        var lembrarButtonPressed = 0
        var alarmTypeButtonPressed = 0
        var alarmFreqButtonPressed = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewB.apply {
            actionList = mutableListOf(
                expandableHoras, expandableDia, expandableAlerta, expandableNotas, parentLayout,
                buttonCancel, buttonConfirm, secondLembrar.button0, secondLembrar.button1, secondLembrar.button2,
                secondLembrar.button3, secondAlerta.imageButtonSom, secondAlerta.imageButtonVibrar,
                secondAlerta.imageButtonAmbos, secondDia.buttonHoje, secondDia.buttonTodosDias, secondDia.buttonPersonalizado,
            )
        }
        super.onCreate(savedInstanceState)
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

    private var successFlag = alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
            && lembrarButtonPressed != 0 && hoursMinutesFlag

    private fun launchIntent() {
        if (activity?.packageManager?.let { it -> viewModel.alarmIntent.resolveActivity(it) } != null) {
            startActivity(viewModel.alarmIntent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewIntro) {
            reminderIntroHintLayout.show()
            createReminderActionButton.setOnClickListener { reminderIntroHintLayout.hide() }
        }

        with(viewB) {
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

            with(viewSuccess) {
                if (successFlag) {
                    viewModel.addReminder(newReminder)
                    if (viewModel.flagReminderAdded) {
                        avisoCampos.hide()
                        successLayout.show()
                        buttonOk.setOnClickListener { successLayout.hide() }
                        launchIntent()
                    }
                } else if (hoursInt in 0..23 || minsInt in 0..59) {
                    avisoCampos.run { text = getString(R.string.horas_invalidas); show() }
                } else {
                    avisoCampos.run { text = getString(R.string.campos_obrigatorios); show() }
                }
            }
        }

        setupUI()

    }

    /** Kotlin function parameters are read-only values and are not assignable. **/
    override fun setupUI() {
        with(viewB) {
            with(secondLembrar) {
                button0.setOnClickListener {
                    setLembrarLayout(
                        this,
                        1,
                        isVisible = true,
                        isTextVisible = true,
                    )
                }
                button1.setOnClickListener {
                    setLembrarLayout(
                        this, 2, isVisible = false, isTextVisible = false
                    )
                }
                button2.setOnClickListener {
                    setLembrarLayout(
                        this, 3, isVisible = false, isTextVisible = false
                    )
                }
                button3.setOnClickListener {
                    setLembrarLayout(
                        this, 4, isVisible = true, isTextVisible = true
                    )
                }
            }

            /** ---- FILTRAR O TIME INPUT -------- **/
            restrictTimeInput(this, this@ReminderFragment)

            with(secondDia) {
                buttonHoje.setOnClickListener {
                    setSecondLayout(this, 1, false, isGroupVisible = false)
                }
                buttonTodosDias.setOnClickListener {
                    setSecondLayout(this, 2, false, isGroupVisible = false)
                }
                buttonPersonalizado.setOnClickListener {
                    setSecondLayout(this, 3, true, isGroupVisible = true)
                }
            }

            with(secondAlerta) {
                imageButtonSom.setOnClickListener {
                    setSoundLogosVisible(
                        view = secondDia,
                        1,
                        soundVisible = true,
                        vibVisible = false,
                        bothVisible = false,
                        checkboxAmbos,
                        checkboxSom
                    )
                }
                imageButtonVibrar.setOnClickListener {
                    setSoundLogosVisible(
                        secondDia, 2, false,
                        true, false, checkboxVibrar, checkboxSom
                    )
                }
                imageButtonAmbos.setOnClickListener {
                    setSoundLogosVisible(
                        secondDia, 3, false,
                        false, true, checkboxVibrar, checkboxSom
                    )
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
                    hoursInt = secondHoras.editHours.text.toString().toInt()
                    minsInt = secondHoras.editMinutes.text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.run { text = getString(R.string.valor_em_falta); show() }
                }

                with(viewModel.newReminder) {
                    fun updateButton(title: String, reminderType: ReminderType) {
                        this.title = title
                        this.reminderType = reminderType
                    }

                    when (lembrarButtonPressed) {
                        1 -> updateButton(getString(R.string.tomar_medicacao), MEDICACAO)
                        2 -> updateButton(getString(R.string.apanhar_bus), TRANSPORTE)
                        3 -> updateButton(getString(R.string.lembrar_escolha_almoco), REFEICAO)
                        4 -> {
                            updateButton(
                                secondLembrar.textEditPersonalizado.text.toString(),
                                ReminderType.PERSONALIZADO
                            )
                        }
                        else -> {
                            println("lembrarButtonPressed is neither!")
                        }
                    }

                    with(secondDia) {
                        for (id in toggleButtonGroup.checkedButtonIds) {
                            val materialButton: MaterialButton = toggleButtonGroup.findViewById(id)
                            weekMap[resources.getResourceName(materialButton.id)
                                .takeLast(3)]
                        }
                    }

                    setTypeAndFrequency(newReminder)
                }

            }
        }


    private fun setupCancelButton() =
        with(viewB) {
            with(secondAlerta) {
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

                    with(secondHoras) {
                        editHours.setEmptyText()
                        editMinutes.setEmptyText()
                    }

                    secondNotas.editTextNotes.setEmptyText()
                }
            }
        }


    private fun restrictTimeInput(
        reminderFragmentBinding: ReminderFragmentBinding,
        reminderFragment: ReminderFragment
    ) {
        with(reminderFragmentBinding.secondHoras) {
            reminderFragment.et = editHours.apply {
                filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))
            }
            reminderFragment.etMin = editMinutes.apply {
                filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2))
            }
        }
    }

    private fun setTypeAndFrequency(newReminder: Reminder) {
        newReminder.apply {
            alarmType = when (alarmTypeButtonPressed) {
                1 -> SOM
                2 -> VIBRAR
                3 -> AMBOS
                else -> {
                    SOM
                }
            }

            alarmFreq = when (alarmFreqButtonPressed) {
                1 -> HOJE
                2 -> TODOS_OS_DIAS
                3 -> PERSONALIZADO
                else -> {
                    HOJE
                }
            }
        }
    }

    private fun setButtonColorsReminder(view: LayoutSecondLembrarBinding, pos: Int) {

        with(view) {
            button0.setBackgroundResource(R.color.md_blue_100)
            button1.setBackgroundResource(R.color.md_blue_100)
            button2.setBackgroundResource(R.color.md_blue_100)
            button3.setBackgroundResource(R.color.md_blue_100)

            when (pos) {
                1 -> button0.setBackgroundResource(R.color.md_blue_200)
                2 -> button1.setBackgroundResource(R.color.md_blue_200)
                3 -> button2.setBackgroundResource(R.color.md_blue_200)
                4 -> button0.setBackgroundResource(R.color.md_blue_200)
            }
        }
    }

    private fun setButtonColorsDays(view: LayoutSecondDiaBinding, pos: Int) {

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
        bothVisible: Boolean,
        checkboxVib: ImageView,
        checkboxSom: ImageView
    ) {
        alarmTypeButtonPressed = value
        checkboxSom.show()
        checkboxVib.invisible()

        view.buttonSelecionarDias.visibility = when {
            soundVisible || vibVisible || bothVisible -> VISIBLE
            !soundVisible || !vibVisible || !bothVisible -> INVISIBLE
            else -> {
                INVISIBLE
            }
        }
    }

    private fun setLembrarLayout(
        viewLembrar: LayoutSecondLembrarBinding,
        value: Int,
        isVisible: Boolean,
        isTextVisible: Boolean,
    ) {
        with(viewLembrar) {
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
    ) {
        alarmFreqButtonPressed = value
        setButtonColorsDays(view, alarmFreqButtonPressed)
        view.buttonSelecionarDias.visibility = when {
            isbuttonVisible -> VISIBLE
            !isbuttonVisible -> INVISIBLE
            else -> {
                INVISIBLE
            }
        }

        view.toggleButtonGroup.visibility = when {
            isGroupVisible -> VISIBLE
            !isGroupVisible -> INVISIBLE
            else -> {
                INVISIBLE
            }
        }
    }

    override fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String, Any>
    ) {
        HoursHelper.run {
            checkHoursCommand(viewB, command); checkMinutesCommand(viewB, command)
        }

        super.performActionWithVoiceCommand(command, actionMap)

        viewB.apply { if (command.contains("Todos", true)) {
                expandableGroup.forEach { it.clickAndFocus() }
            }
        }
    }
}
