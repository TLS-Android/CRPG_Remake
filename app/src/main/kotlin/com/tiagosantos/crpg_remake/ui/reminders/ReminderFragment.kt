package com.tiagosantos.crpg_remake.ui.reminders

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.viewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.ReminderActivityIntroBinding
import com.tiagosantos.crpg_remake.databinding.ReminderActivitySuccessBinding
import com.tiagosantos.crpg_remake.databinding.ReminderFragmentBinding
import com.tiagosantos.crpg_remake.domain.model.AlarmFrequency
import com.tiagosantos.crpg_remake.domain.model.AlarmType
import com.tiagosantos.crpg_remake.domain.model.ReminderType
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

    private var lembrarButtonPressed = 0
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = EMPTY_STRING
    private var hoursMinutesFlag = false

    private val helper = RemindersHelper()
    private val textEditPersonalizado =
        root.findViewById<EditText>(R.id.text_edit_personalizado)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val reminderVM: ReminderViewModel by viewModels()
        reminderVM.setNewReminder()
        setupUI(reminderVM)
        return view
    }

    private fun setupUI(reminderVM: ReminderViewModel){
        viewIntro.reminderIntroHintLayout.visibility = View.VISIBLE
        viewIntro.createReminderActionButton.setOnClickListener{
            viewIntro.reminderIntroHintLayout.visibility = View.GONE
        }

        fun setSecondLayout(value: Int, isVisible: Boolean, isTextVisible: Boolean){
            lembrarButtonPressed = value
            setButtonColorsReminder(lembrarButtonPressed)

            when {
                isVisible -> View.VISIBLE
                !isVisible -> View.INVISIBLE
            }

            when {
                isVisible ->
                    root.findViewById<TextView>(R.id
                    .inserir_titulo_lembrete_personalizado).visibility = View.VISIBLE
                !isVisible ->
                    root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility =
                    View.INVISIBLE
            }


            root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
            textEditPersonalizado.visibility = View.INVISIBLE
        }

            expandableLembrar.parentLayout.setOnClickListener { expandableLembrar.toggleLayout() }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button0)
                    .setOnClickListener {

                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button1)
                    .setOnClickListener {
                        lembrarButtonPressed = 2
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button2)
                    .setOnClickListener {
                        lembrarButtonPressed = 3
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
                    }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button3)
                    .setOnClickListener {
                        lembrarButtonPressed = 4
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.VISIBLE
                        textEditPersonalizado.visibility = View.VISIBLE
                    }

            expandableHoras.parentLayout.setOnClickListener { expandableHoras.toggleLayout() }

            val et = expandableHoras.secondLayout.findViewById(R.id.edit_hours) as EditText
            et.filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))

            val etMin = expandableHoras.secondLayout.findViewById(R.id.edit_minutes) as EditText
            etMin.filters = arrayOf(InputFilterMinMax("00", "59"),InputFilter.LengthFilter(2))

            val cbSom = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_som)
            val cbVib = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_vibrar)
            val cbAmbos = expandableAlerta.secondLayout.findViewById<ImageView>(R.id.checkbox_ambos)

        view.parentLayout.setOnClickListener { view.expandableDia }
            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }

            expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje)
                    .setOnClickListener { setSecondLayout(1, false, false) }

            expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias)
                .setOnClickListener { setSecondLayout(2, false, false) }

            expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado)
                .setOnClickListener { setSecondLayout(3, true, true) }

        expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonSom)
                    .setOnClickListener { setSoundLogosVisible(1,true, false, false) }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonVibrar)
                .setOnClickListener { setSoundLogosVisible(2,false, true, false) }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonAmbos)
                .setOnClickListener { setSoundLogosVisible(3, false, false, true) }
                expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }


            //--------------------- CANCELAR ---------------------------------------
            val avisoCampos = root.findViewById<TextView>(R.id.aviso_campos)

            root.findViewById<Button>(R.id.button_cancel).setOnClickListener {
                avisoCampos.visibility = View.GONE

                lembrarButtonPressed = 0
                alarmTypeButtonPressed = 0
                alarmFreqButtonPressed = 0

                //reset set Hours section
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_hours).setText("")
                expandableHoras.secondLayout.findViewById<EditText>(R.id.edit_minutes).setText("")

                // reset alarmType section
                cbSom.visibility = View.INVISIBLE
                cbVib.visibility = View.INVISIBLE
                cbAmbos.visibility = View.INVISIBLE

                expandableNotas.secondLayout.findViewById<EditText>(R.id.editTextNotes).setText("")
            }

            //------------------------- CONFIRMAR -------------------------------------------------

            root.findViewById<Button>(R.id.button_confirm).setOnClickListener {
                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    reminderVM.startTimeHours = et.text.toString()
                    reminderVM.startTimeMin = etMin.text.toString()
                    startTimeString = reminderVM.startTimeHours.plus(reminderVM.startTimeMin)
                    hoursInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    minsInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.text = getString(R.string.valor_horas_minutos_falta)
                    avisoCampos.visibility = View.VISIBLE
                }

                when (lembrarButtonPressed) {
                    1 -> {reminderVM.newReminder.title = "Tomar medicacao"
                        reminderVM.newReminder.reminder_type = ReminderType.MEDICACAO }
                    2 -> {reminderVM.newReminder.title = "Apanhar bus do CRPG"
                        reminderVM.newReminder.reminder_type = ReminderType.TRANSPORTE }
                    3 -> {reminderVM.newReminder.title = "Lembrar escolha de almoço"
                        reminderVM.newReminder.reminder_type = ReminderType.REFEICAO }
                    //definir titulo personalizado
                    4 -> {reminderVM.newReminder.title = textEditPersonalizado.text.toString()
                        reminderVM.newReminder.reminder_type = ReminderType.PERSONALIZADO }
                    else -> { println("lembrarButtonPressed is neither one of the values") }
                }

                val materialButtonToggleGroup =
                        expandableDia.secondLayout.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup)

                val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                for (id in ids) {
                    val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                    when (expandableDia.secondLayout.resources.getResourceName(materialButton.id).takeLast(3)) {
                        "Seg" -> reminderVM.weekDaysBoolean[0] = true
                        "Ter" -> reminderVM.weekDaysBoolean[1] = true
                        "Qua" -> reminderVM.weekDaysBoolean[2] = true
                        "Qui" -> reminderVM.weekDaysBoolean[3] = true
                        "Sex" -> reminderVM.weekDaysBoolean[4] = true
                        "Sab" -> reminderVM.weekDaysBoolean[5] = true
                        "Dom" -> reminderVM.weekDaysBoolean[6] = true
                    }
                }

                when (alarmTypeButtonPressed) {
                    1 -> reminderVM.newReminder.alarm_type = AlarmType.SOM
                    2 -> reminderVM.newReminder.alarm_type = AlarmType.VIBRAR
                    3 -> reminderVM.newReminder.alarm_type = AlarmType.AMBOS
                }

                when (alarmFreqButtonPressed) {
                    1 -> reminderVM.newReminder.alarm_freq = AlarmFrequency.HOJE
                    2 -> reminderVM.newReminder.alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                    3 -> reminderVM.newReminder.alarm_freq = AlarmFrequency.PERSONALIZADO
                }

                if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
                        && lembrarButtonPressed != 0 && hoursMinutesFlag) {
                    reminderVM.addReminder()
                    if (reminderVM.flagReminderAdded) {
                        avisoCampos.visibility = View.GONE
                        viewSuccess.successLayout.visibility = View.VISIBLE
                        viewSuccess.buttonOk.setOnClickListener {
                            viewSuccess.successLayout.visibility = View.GONE
                        }
                        if (activity?.packageManager?.let { it1 -> reminderVM.alarmIntent.resolveActivity(it1) } != null) {
                            startActivity(reminderVM.alarmIntent)
                        }
                    }
                } else if (hoursInt > 23 || minsInt > 59) {
                    avisoCampos.text = getString(R.string.hora_minutos_invalido)
                    avisoCampos.visibility = View.VISIBLE
                } else {
                    avisoCampos.text = getString(R.string.campos_obrigatorios_falta)
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
