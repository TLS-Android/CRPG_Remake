package com.tiagosantos.crpg_remake.ui.reminders

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.*
import com.tiagosantos.crpg_remake.domain.model.AlarmFrequency
import com.tiagosantos.crpg_remake.domain.model.AlarmType
import com.tiagosantos.crpg_remake.domain.model.ReminderType
import com.tiagosantos.crpg_remake.ui.reminders.ReminderRepository.newReminder
import java.util.*

class ReminderFragment : BaseFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminder_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {
    private lateinit var view: ReminderFragmentBinding
    private lateinit var viewIntro: ReminderActivityIntroBinding
    private lateinit var viewSuccess: ReminderActivitySuccessBinding


    private lateinit var viewLembrar1: LayoutParentLembrarBinding

    private lateinit var viewLembrar: LayoutSecondLembrarBinding
    private lateinit var viewAlerta: LayoutSecondAlertaBinding
    private lateinit var viewDia: LayoutSecondDiaBinding
    private lateinit var viewHoras: LayoutSecondHorasBinding
    private lateinit var viewNotas: LayoutSecondNotasBinding

    private var lembrarButtonPressed = 0
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = EMPTY_STRING
    private var hoursMinutesFlag = false

    private var hoursInt = 0
    private var minsInt = 0

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    lateinit var cbSom: ImageView
    lateinit var cbVib: ImageView
    lateinit var cbAmbos: ImageView

    private val helper = RemindersHelper()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val reminderVM: ReminderViewModel by viewModels()
        reminderVM.setNewReminder()
        setupUI(reminderVM)
        return view
    }

    private fun setupUI(reminderVM: ReminderViewModel) {
        with(view){
            this.parentLayout.setOnClickListener { view.expandableDia.toggleLayout() }
            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }
            view.parentLayout.setOnClickListener { view.expandableLembrar.toggleLayout() }
            view.parentLayout.setOnClickListener { view.expandableDia.toggleLayout() }
            this. expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }
            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }
        }


        with(viewIntro){
            this.reminderIntroHintLayout.visibility = View.VISIBLE
            this.createReminderActionButton.setOnClickListener {
                this.reminderIntroHintLayout.visibility = View.GONE
            }
        }

        with(viewLembrar){
            this.button0.setOnClickListener { helper.setLembrarLayout(
                viewLembrar, 1, true, true, lembrarButtonPressed) }
            this.button1.setOnClickListener { helper.setLembrarLayout(
                viewLembrar, 2, false, false, lembrarButtonPressed) }
            this.button2.setOnClickListener { helper.setLembrarLayout(
                viewLembrar, 3, false, false, lembrarButtonPressed) }
            this.button3.setOnClickListener { helper.setLembrarLayout(
                viewLembrar, 4, true, true, lembrarButtonPressed) }
        }

        with(viewHoras){
            val et = this.editHours
            et.filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))

            val etMin = this.editMinutes
            etMin.filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2))
        }

        with(viewDia){
            this.buttonHoje.setOnClickListener {
                helper.setSecondLayout(this,1, false, false, alarmFreqButtonPressed) }
            this.buttonTodosDias.setOnClickListener {
                helper.setSecondLayout(this,2, false, false,alarmFreqButtonPressed) }
            this.buttonPersonalizado.setOnClickListener {
                helper.setSecondLayout(this,3, true, true, alarmFreqButtonPressed) }
        }

        with(viewAlerta){
            cbSom = this.checkboxSom
            cbVib = this.checkboxVibrar
            cbAmbos = this.checkboxAmbos

            this.imageButtonSom.setOnClickListener{
                helper.setSoundLogosVisible(this,1, true, false, false) }
            this.imageButtonVibrar.setOnClickListener{
                helper.setSoundLogosVisible(this,2, false, true, false) }
            this.imageButtonAmbos.setOnClickListener{
                helper.setSoundLogosVisible(this,3, false, false, true) }
        }

        //--------------------- CANCELAR ---------------------------------------

        val avisoCampos = view.avisoCampos
        val buttonCancel = view.buttonCancel

        buttonCancel.setOnClickListener {
            avisoCampos.visibility = View.GONE

            lembrarButtonPressed = 0
            alarmTypeButtonPressed = 0
            alarmFreqButtonPressed = 0

            //reset set Hours section
            with(viewHoras){
                this.editHours.setText(EMPTY_STRING)
                this.editMinutes.setText(EMPTY_STRING)
            }

            // reset alarmType section
            cbSom.visibility = View.INVISIBLE
            cbVib.visibility = View.INVISIBLE
            cbAmbos.visibility = View.INVISIBLE

            viewNotas.editTextNotes.setText(EMPTY_STRING)
        }

        //------------------------- CONFIRMAR -------------------------------------------------

        view.buttonConfirm.setOnClickListener {
            if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                reminderVM.startTimeHours = et.text.toString()
                reminderVM.startTimeMin = etMin.text.toString()
                startTimeString = reminderVM.startTimeHours.plus(reminderVM.startTimeMin)
                hoursInt = viewHoras.editHours.text.toString().toInt()
                minsInt = viewHoras.editMinutes.text.toString().toInt()
                hoursMinutesFlag = true
            } else {
                avisoCampos.text = "Valor em falta"
                avisoCampos.visibility = View.VISIBLE
            }

            with(reminderVM.newReminder) {
                when (lembrarButtonPressed) {
                    1 -> {
                        this.title = "Tomar medicacao"
                        this.reminder_type = ReminderType.MEDICACAO
                    }
                    2 -> {
                        this.title = "Apanhar bus do CRPG"
                        this.reminder_type = ReminderType.TRANSPORTE
                    }
                    3 -> {
                        this.title = "Lembrar escolha de almoço"
                        this.reminder_type = ReminderType.REFEICAO
                    }
                    4 -> {
                        this.title = viewLembrar.textEditPersonalizado.text.toString()
                        this.reminder_type = ReminderType.PERSONALIZADO
                    }
                    else -> { println("lembrarButtonPressed is neither one of the values") }
                }


                with(viewDia){
                    val materialButtonToggleGroup = this.toggleButtonGroup
                    val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                    for (id in ids) {
                        val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                        when (this.secondLayout.resources.getResourceName(materialButton.id)
                            .takeLast(3)) {
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
                    1 -> this.alarm_type = AlarmType.SOM
                    2 -> this.alarm_type = AlarmType.VIBRAR
                    3 -> this.alarm_type = AlarmType.AMBOS
                }

                when (alarmFreqButtonPressed) {
                    1 -> this.alarm_freq = AlarmFrequency.HOJE
                    2 -> this.alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                    3 -> this.alarm_freq = AlarmFrequency.PERSONALIZADO
                }
            }


            if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
                && lembrarButtonPressed != 0 && hoursMinutesFlag
            ) {
                reminderVM.addReminder(newReminder)
                if (reminderVM.flagReminderAdded) {
                    avisoCampos.visibility = View.GONE
                    viewSuccess.successLayout.visibility = View.VISIBLE
                    viewSuccess.buttonOk.setOnClickListener {
                        viewSuccess.successLayout.visibility = View.GONE
                    }
                    if (activity?.packageManager?.let { it1 ->
                            reminderVM.alarmIntent.resolveActivity(
                                it1
                            )
                        } != null) {
                        startActivity(reminderVM.alarmIntent)
                    }
                }
            } else if (hoursInt > 23 || minsInt > 59) {
                avisoCampos.text = "Horas ou minutos invalidos"
                avisoCampos.visibility = View.VISIBLE
            } else {
                avisoCampos.text = "Campos obrigatorios em falta!"
                avisoCampos.visibility = View.VISIBLE
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
