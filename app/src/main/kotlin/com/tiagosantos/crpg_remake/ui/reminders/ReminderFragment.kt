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
import com.tiagosantos.crpg_remake.ui.reminders.helpers.RemindersHelper
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
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = EMPTY_STRING
    private var hoursMinutesFlag = false

    private var hoursInt = 0
    private var minsInt = 0

    private lateinit var et: EditText
    private lateinit var etMin: EditText

    private lateinit var cbSom: ImageView
    private lateinit var cbVib: ImageView
    private lateinit var cbAmbos: ImageView

    private val helper = RemindersHelper()

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
            this.reminderIntroHintLayout.visibility = VISIBLE
            this.createReminderActionButton.setOnClickListener {
                this.reminderIntroHintLayout.visibility = GONE
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

            with(secondLembrar){
                this.button0.setOnClickListener { helper.setLembrarLayout(
                    secondLembrar, 1,
                    isVisible = true,
                    isTextVisible = true,
                    lembrarButtonPressed = lembrarButtonPressed
                ) }
                this.button1.setOnClickListener { helper.setLembrarLayout(
                    secondLembrar, 2, false, false, lembrarButtonPressed) }
                this.button2.setOnClickListener { helper.setLembrarLayout(
                    secondLembrar, 3, false, false, lembrarButtonPressed) }
                this.button3.setOnClickListener { helper.setLembrarLayout(
                    secondLembrar, 4, true, true, lembrarButtonPressed) }
            }

            with(secondHoras){
                val et = this.editHours
                et.filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))

                val etMin = this.editMinutes
                etMin.filters = arrayOf(InputFilterMinMax("00", "59"), InputFilter.LengthFilter(2))
            }


            with(secondDia){
                this.buttonHoje.setOnClickListener {
                    helper.setSecondLayout(this,1, false,
                        isGroupVisible =  false,
                        alarmFreqButtonPressed) }
                this.buttonTodosDias.setOnClickListener {
                    helper.setSecondLayout(this,2, false,
                        isGroupVisible = false,
                        alarmFreqButtonPressed) }
                this.buttonPersonalizado.setOnClickListener {
                    helper.setSecondLayout(this,3, true,
                        isGroupVisible = true,
                        alarmFreqButtonPressed) }
            }

            with(secondAlerta){
                    cbSom = this.checkboxSom
                    cbVib = this.checkboxVibrar
                    cbAmbos = this.checkboxAmbos

                    this.imageButtonSom.setOnClickListener{
                        setSoundLogosVisible(
                            view = this,
                            1,
                            soundVisible = true,
                            vibVisible = false,
                            bothVisible = false) }
                    this.imageButtonVibrar.setOnClickListener{
                        setSoundLogosVisible(this,2,
                            false, true, false) }
                    this.imageButtonAmbos.setOnClickListener{
                        setSoundLogosVisible(this,3,
                            false, false, true) }
            }

            //--------------------- CANCELAR ---------------------------------------

            val avisoCampos = avisoCampos
            val buttonCancel = buttonCancel

            buttonCancel.setOnClickListener {
                avisoCampos.visibility = GONE

                lembrarButtonPressed = 0
                alarmTypeButtonPressed = 0
                alarmFreqButtonPressed = 0

                //reset set Hours section
                with(secondHoras){
                    this.editHours.setText(EMPTY_STRING)
                    this.editMinutes.setText(EMPTY_STRING)
                }

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
                        this.reminder_type = reminderType
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
                        val materialButtonToggleGroup = this.toggleButtonGroup
                        val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                        for (id in ids) {
                            val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                            when (this.secondLayout.resources.getResourceName(materialButton.id).takeLast(3)) {
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
                                reminderVM.alarmIntent.resolveActivity(it1)
                            } != null) {
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

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}
