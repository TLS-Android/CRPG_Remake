package com.tiagosantos.crpg_remake.ui.reminders


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.plataforma.crpg.R
import com.plataforma.crpg.TimelineView
import com.plataforma.crpg.databinding.ReminderActivityBinding
import com.plataforma.crpg.model.AlarmFrequency
import com.plataforma.crpg.model.AlarmType
import com.plataforma.crpg.model.ReminderType
import com.plataforma.crpg.ui.reminders.ReminderViewModel
import com.skydoves.expandablelayout.ExpandableLayout
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.ReminderFragmentBinding
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*

class ReminderFragment : BaseFragment<ReminderFragmentBinding>(
    layoutId = R.layout.reminders_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {

    private lateinit var view: ReminderFragmentBinding
    private var flagMealChosen = false

    companion object {
        fun newInstance() = ReminderFragment()
    }

    private var lembrarButtonPressed = 0
    private var alarmTypeButtonPressed = 0
    private var alarmFreqButtonPressed = 0
    private var startTimeString = EMPTY_STRING
    private var hoursMinutesFlag = false


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val modalityPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)
        val hasRun = modalityPreferences.getBoolean("remindersHasRun", false)

        val binding = ReminderActivityBinding.inflate(layoutInflater)
        val view = binding.root
        val newViewModel = ViewModelProvider(activity as AppCompatActivity).get(ReminderViewModel::class.java)

        newViewModel.startNewFileAndPopulate()

        defineModality(ttsFlag, srFlag, hasRun, view)

        with(binding){
            root.findViewById<View>(R.id.reminderIntroHintLayout).visibility = View.VISIBLE
            root.findViewById<FloatingActionButton>(R.id.createReminderActionButton).setOnClickListener{
                root.findViewById<View>(R.id.reminderIntroHintLayout).visibility = View.GONE

            }

            fun setButtonColorsReminder(pos: Int){
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

            fun setButtonColorsDays(pos: Int){
                expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top)
                expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_100)
                expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom)

                when(pos){
                    1 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje).setBackgroundResource(R.drawable.layout_button_round_top_selected)
                    2 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias).setBackgroundResource(R.color.md_blue_200)
                    3 ->  expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado).setBackgroundResource(R.drawable.layout_button_round_bottom_selected)
                }

            }

            val textEditPersonalizado = root.findViewById<EditText>(R.id.text_edit_personalizado)

            expandableLembrar.parentLayout.setOnClickListener {
                expandableLembrar.toggleLayout() }
            expandableLembrar.secondLayout.findViewById<Button>(R.id.button0)
                    .setOnClickListener {
                        lembrarButtonPressed = 1
                        setButtonColorsReminder(lembrarButtonPressed)
                        root.findViewById<TextView>(R.id.inserir_titulo_lembrete_personalizado).visibility = View.INVISIBLE
                        textEditPersonalizado.visibility = View.INVISIBLE
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

            expandableDia.parentLayout.setOnClickListener { expandableDia.toggleLayout() }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_hoje)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 1
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_todos_dias)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 2
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.INVISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.INVISIBLE
                    }
            expandableDia.secondLayout.findViewById<Button>(R.id.button_personalizado)
                    .setOnClickListener {
                        alarmFreqButtonPressed = 3
                        setButtonColorsDays(alarmFreqButtonPressed)
                        root.findViewById<TextView>(R.id.button_selecionar_dias).visibility = View.VISIBLE
                        root.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup).visibility = View.VISIBLE
                    }

            expandableAlerta.parentLayout.setOnClickListener { expandableAlerta.toggleLayout() }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonSom)
                    .setOnClickListener {
                        cbSom.visibility = View.VISIBLE
                        cbVib.visibility = View.INVISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 1
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonVibrar)
                    .setOnClickListener {
                        cbSom.visibility = View.INVISIBLE
                        cbVib.visibility = View.VISIBLE
                        cbAmbos.visibility = View.INVISIBLE
                        alarmTypeButtonPressed = 2
                    }
            expandableAlerta.secondLayout.findViewById<AppCompatImageButton>(R.id.imageButtonAmbos)
                    .setOnClickListener {
                        cbSom.visibility = View.INVISIBLE
                        cbVib.visibility = View.INVISIBLE
                        cbAmbos.visibility = View.VISIBLE
                        alarmTypeButtonPressed = 3
                    }

            expandableNotas.parentLayout.setOnClickListener { expandableNotas.toggleLayout() }

            //--------------------- CANCELAR ---------------------------------------
            val avisoCampos = root.findViewById<TextView>(R.id.aviso_campos)

            root.findViewById<Button>(R.id.button_cancel).setOnClickListener {

                //expandableLembrar.parentLayout.performClick()

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

                var hoursInt = 1
                var minsInt = 1

                if (et.text.toString().length == 2 && etMin.text.toString().length == 2) {
                    println("> Validacao das horas com sucesso")
                    newViewModel.startTimeHours = et.text.toString()
                    newViewModel.startTimeMin = etMin.text.toString()
                    startTimeString = newViewModel.startTimeHours.plus(newViewModel.startTimeMin)
                    hoursInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    minsInt = root.findViewById<EditText>(R.id.edit_minutes).text.toString().toInt()
                    hoursMinutesFlag = true
                } else {
                    avisoCampos.text = getString(R.string.valor_horas_minutos_falta)
                    avisoCampos.visibility = View.VISIBLE
                }

                newViewModel.newReminder.start_time = startTimeString
                newViewModel.newReminder.hours = hoursInt
                newViewModel.newReminder.mins = minsInt

                when (lembrarButtonPressed) {
                    1 -> {newViewModel.newReminder.title = "Tomar medicacao"
                        newViewModel.newReminder.reminder_type = ReminderType.MEDICACAO }
                    2 -> {newViewModel.newReminder.title = "Apanhar bus do CRPG"
                        newViewModel.newReminder.reminder_type = ReminderType.TRANSPORTE }
                    3 -> {newViewModel.newReminder.title = "Lembrar escolha de almoço"
                        newViewModel.newReminder.reminder_type = ReminderType.REFEICAO }
                    //definir titulo personalizado
                    4 -> {newViewModel.newReminder.title = textEditPersonalizado.text.toString()
                        newViewModel.newReminder.reminder_type = ReminderType.PERSONALIZADO }
                    else -> {
                        println("lembrarButtonPressed is neither one of the values")
                    }
                }

                //println(">Titulo personalizado do reminder: " + newViewModel.newReminder.title)

                val materialButtonToggleGroup =
                        expandableDia.secondLayout.findViewById<MaterialButtonToggleGroup>(R.id.toggleButtonGroup)

                val ids: List<Int> = materialButtonToggleGroup.checkedButtonIds
                for (id in ids) {
                    val materialButton: MaterialButton = materialButtonToggleGroup.findViewById(id)
                    val resourceName: String =
                            expandableDia.secondLayout.resources.getResourceName(materialButton.id).takeLast(3)
                    when (resourceName) {
                        "Seg" -> newViewModel.weekDaysBoolean[0] = true
                        "Ter" -> newViewModel.weekDaysBoolean[1] = true
                        "Qua" -> newViewModel.weekDaysBoolean[2] = true
                        "Qui" -> newViewModel.weekDaysBoolean[3] = true
                        "Sex" -> newViewModel.weekDaysBoolean[4] = true
                        "Sab" -> newViewModel.weekDaysBoolean[5] = true
                        "Dom" -> newViewModel.weekDaysBoolean[6] = true
                    }
                }

                println(">alarm type:" + alarmTypeButtonPressed)
                println(">alarm freq:" + alarmFreqButtonPressed)
                when (alarmTypeButtonPressed) {
                    1 -> newViewModel.newReminder.alarm_type = AlarmType.SOM
                    2 -> newViewModel.newReminder.alarm_type = AlarmType.VIBRAR
                    3 -> newViewModel.newReminder.alarm_type = AlarmType.AMBOS
                    else -> { // Note the block
                        println("alarmTypeButtonPressed is neither one of the values")
                    }
                }

                when (alarmFreqButtonPressed) {
                    1 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.HOJE
                    2 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.TODOS_OS_DIAS
                    3 -> newViewModel.newReminder.alarm_freq = AlarmFrequency.PERSONALIZADO
                    else -> { // Note the block
                        println("alarmFreqButtonPressed is neither one of the values")
                    }
                }

                if (alarmFreqButtonPressed != 0 && alarmTypeButtonPressed != 0
                        && lembrarButtonPressed != 0 && hoursMinutesFlag) {
                    println(">chega ao add Reminder")
                    newViewModel.addReminder()
                    if (newViewModel.flagReminderAdded) {
                        avisoCampos.visibility = View.GONE
                        root.findViewById<View>(R.id.successLayout).visibility = View.VISIBLE
                        root.findViewById<Button>(R.id.button_ok).setOnClickListener {
                            root.findViewById<View>(R.id.successLayout).visibility = View.GONE
                        }
                        if (activity?.packageManager?.let { it1 -> newViewModel.alarmIntent.resolveActivity(it1) } != null) {
                            startActivity(newViewModel.alarmIntent)
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

            fun performActionWithVoiceCommand(command: String){
                when {
                    command.contains("Abrir secção lembrete", true) -> expandableLembrar.parentLayout.performClick()
                    command.contains("Abrir secção horas", true) -> expandableHoras.parentLayout.performClick()
                    command.contains("Abrir secção dia", true) -> expandableDia.parentLayout.performClick()
                    command.contains("Abrir secção alerta", true) -> expandableAlerta.parentLayout.performClick()
                    command.contains("Abrir secção notas", true) -> expandableNotas.parentLayout.performClick()
                }
            }
        }


        return view
    }

    //binding must be done on onCreateView, not on onActivityCreated
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //newViewModel = ViewModelProvider(this).get(ReminderViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun performActionWithVoiceCommand(command: String, view: LinearLayout){
        checkHoursCommand(command)
        checkMinutesCommand(command)

        when {
            command.contains("Lembrete", true) -> view.findViewById<ExpandableLayout>(R.id.expandable_lembrar).parentLayout.performClick()
            command.contains("Horas", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.performClick()
                view.findViewById<ExpandableLayout>(R.id.expandable_horas).parentLayout.requestFocus()
            }
            command.contains("Dia", true) -> {
                view.findViewById<ExpandableLayout>(R.id.expandable_dia).parentLayout.performClick()
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
            command.contains("Escolher Almoço", true)-> button2.performClick()
            command.contains("O Meu Lembrete", true)-> button3.performClick()
            command.contains("Som", true)-> imageButtonSom.performClick()
            command.contains("Vibrar", true) -> imageButtonVibrar.performClick()
            command.contains("Ambos", true) -> imageButtonAmbos.performClick()
            command.contains("Hoje", true) -> button_hoje.performClick()
            command.contains("Sempre", true) -> button_todos_dias.performClick()
            command.contains("Escolher Dias", true) -> button_personalizado.performClick()
        }
    }

    private fun checkHoursCommand(command: String) {
        when {
            (command.contains("oito", true) || command.contains("8", true)) && command.contains("da manhã", true) ->edit_hours.setText("08")
            (command.contains("oito", true) || command.contains("8", true))  && command.contains("da noite", true) ->edit_hours.setText("20")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da manhã", true) ->edit_hours.setText("09")
            (command.contains("nove", true)  || command.contains("9", true))&& command.contains("da noite", true) ->edit_hours.setText("21")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da manhã", true) ->edit_hours.setText("10")
            (command.contains("dez", true)  || command.contains("10", true))&& command.contains("da noite", true) ->edit_hours.setText("22")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da manhã", true) ->edit_hours.setText("11")
            (command.contains("onze", true)  || command.contains("11", true))&& command.contains("da noite", true) ->edit_hours.setText("23")
            (command.contains("meio-dia", true)  || command.contains("12", true)) ->edit_hours.setText("12")
            (command.contains("meia-noite", true)  || command.contains("0", true)) ->edit_hours.setText("00")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da manhã", true) ->edit_hours.setText("01")
            (command.contains("uma", true)  || command.contains("1", true))&& command.contains("da tarde", true) ->edit_hours.setText("13")
            (command.contains("duas", true) || command.contains("2", true)) && command.contains("da manhã", true) ->edit_hours.setText("02")
            (command.contains("duas", true)  || command.contains("2", true))&& command.contains("da tarde", true) ->edit_hours.setText("14")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da manhã", true) ->edit_hours.setText("03")
            (command.contains("três", true)  || command.contains("3", true))&& command.contains("da tarde", true) ->edit_hours.setText("15")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da manhã", true) ->edit_hours.setText("04")
            (command.contains("quatro", true)  || command.contains("4", true))&& command.contains("da tarde", true) ->edit_hours.setText("16")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da manhã", true) ->edit_hours.setText("05")
            (command.contains("cinco", true)  || command.contains("5", true))&& command.contains("da tarde", true) ->edit_hours.setText("17")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da manhã", true) ->edit_hours.setText("06")
            (command.contains("seis", true)  || command.contains("6", true))&& command.contains("da tarde", true) ->edit_hours.setText("18")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da manhã", true) ->edit_hours.setText("07")
            (command.contains("sete", true)  || command.contains("7", true))&& command.contains("da tarde", true) ->edit_hours.setText("19")
        }

    }

    private fun checkMinutesCommand(command: String) {
        when {
            command.contains("e cinco", true) || command.contains(":05", true) ->edit_minutes.setText("05")
            command.contains("e um quarto", true) || command.contains(":15", true) ->edit_minutes.setText("15")
            command.contains("e meia", true) || command.contains(":30", true) ->edit_minutes.setText("30")
        }

    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean, view: LinearLayout) {

        println("ttsFlag:  " + ttsFlag)
        println("srFlag: " + srFlag)
        println("hasRun: " + hasRun)

        view.findViewById<ExpandableLayout>(R.id.expandable_lembrar).parentLayout.performClick()

        if (!hasRun){
            when{
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition(view) }
                ttsFlag && srFlag ->{ multimodalOption(view) }
            }
        }

        if(hasRun){
            when{
                !ttsFlag && srFlag -> { startVoiceRecognition(view) }
                ttsFlag && srFlag ->{ startVoiceRecognition(view) }
            }
        }

    }

    private fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Diga o nome da secção para abrir/fechar essa secção" +
                        "e depois diga o nome da opção que quer escolher ou da hora para fazer a seleção", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun multimodalOption(view: LinearLayout) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if(activity != null && isAdded) {
                            startVoiceRecognition(view)
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak("Diga o nome da secção para abrir/fechar essa secção" +
                        "e depois diga o nome da opção que quer escolher ou da hora para fazer a seleção  ", TextToSpeech.QUEUE_FLUSH, null, "ID")

            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun startVoiceRecognition(view: LinearLayout) {
        //MANTER WIFI SEMPRE LIGADO
        //val handler = Handler(Looper.getMainLooper())
        if(isAdded && isVisible && getUserVisibleHint()) {
            runnable = Runnable {
                handler.sendEmptyMessage(0);
                Speech.init(requireActivity())
                //hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {
                            //Log.d("speech", "rms is now: $value")
                        }

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString(), view)
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(TimelineView.TAG, "onSpeechResult: " + result.toLowerCase())
                            //Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible && getUserVisibleHint()) {
                                            Speech.init(requireActivity())
                                            //hasInitSR = true
                                            Speech.getInstance().startListening(this)
                                        }
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)
                            }
                        }
                    })
                } catch (exc: SpeechRecognitionNotAvailable) {
                    Log.e("speech", "Speech recognition is not available on this device!")
                } catch (exc: GoogleVoiceTypingDisabledException) {
                    Log.e("speech", "Google voice typing must be enabled!")
                }
            }

            handler.post(runnable)
        }

    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }


}
