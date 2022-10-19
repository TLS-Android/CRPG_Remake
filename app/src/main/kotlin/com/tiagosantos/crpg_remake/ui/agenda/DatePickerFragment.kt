package com.tiagosantos.crpg_remake.ui.agenda

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.tiagosantos.common.ui.singlerowcalendar.calendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.tiagosantos.common.ui.singlerowcalendar.utils.DateUtils
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_date_picker.*
import kotlinx.android.synthetic.main.reminder_activity.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable
import java.util.*

class DatePickerFragment(ttsSettings: TTSFragmentSettings) :
    BaseModalFragment<>(
        layoutId = R.layout.meals_fragment,
        FragmentSettings(
        appBarTitle = "ESCOLHER DATA",
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
        ),
        TTSFragmentSettings(
            "Por favor selecione um dia movendo os quadrados amarelos para a esquerda " +
                    "e direita e premindo aquele que pretender selecionar"
        )
) {

    private var currentMonth = 0
    private var selected = false
    private var firstTimeFlag = false
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // ttsDatePickerHint()
        return inflater.inflate(
            R.layout.fragment_date_picker,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel = ViewModelProvider(activity as AppCompatActivity).get(
            SharedViewModel::class.java)

        calendar.time = Date()

        // calendar view manager is responsible for our displaying logic
        val myCalendarViewManager = object :
            CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean,
            ): Int {
                // set date to calendar according to position where we are
                val cal = Calendar.getInstance()
                cal.time = date
                if (!isSelected) tvDate.text = getString(R.string.nenhum_dia_selecionado_msg)

                return if (isSelected)
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            R.layout.selected_calendar_item
                        }
                    }
                else
                // here we return items which are not selected
                    when (cal[Calendar.DAY_OF_WEEK]) {
                        else -> {
                            R.layout.calendar_item
                        }
                    }

                // NOTE: if we don't want to do it this way, we can simply change color of background
                // in bindDataToCalendarView method
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean,
            ) {
                // using this method we can bind data to calendar view
                // good practice is if all views in layout have same IDs in all item views
                holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)
            }
        }

        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                tvDate.text = "${
                    DateUtils.getDayName(date)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}," +
                    " ${DateUtils.getDayNumber(date)} de ${
                        DateUtils.getMonthName(date)
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}"
                tvDay.text = DateUtils.getDayName(date)
                sharedViewModel.selectedDate = DateUtils.getDayNumber(date) + DateUtils.getMonthNumber(date) + DateUtils.getYear(date)
                super.whenSelectionChanged(isSelected, position, date)
                selected = isSelected
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                // saturday and sunday are disabled as CRPG is not open on these days
                return when (cal[Calendar.DAY_OF_WEEK]) {
                    Calendar.SATURDAY -> false
                    Calendar.SUNDAY -> false
                    else -> true
                }
            }
        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        button_selecionar.setOnClickListener {
            if (selected) {
                no_date_selected_warning.visibility = GONE
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment, "Agenda")
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                onDestroy()
            } else {
                no_date_selected_warning.visibility = VISIBLE
            }
        }

        defineModality(ttsFlag, srFlag, hasRun, singleRowCalendar)
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }


    private fun multimodalOption(singleRowCalendar: SingleRowCalendar) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        // println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        // ("Encerrou TTS")
                        if (activity != null && isAdded) {
                            startVoiceRecognition(singleRowCalendar)
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak(
                    "Diga o dia ", TextToSpeech.QUEUE_FLUSH,
                    null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performActionWithVoiceCommand(command: String, singleRowCalendar: SingleRowCalendar) {

        val numbersMap = mapOf(
            "um" to 1, "dois" to 2, "três" to 3, "quatro" to 4,
            "cinco" to 5, "seis" to 6, "sete" to 7, "oito" to 8, "nove" to 9, "dez" to 10
        )
        val literalValue = numbersMap.getOrElse(command) {
            println("Número não presente na lista") } as Int

        singleRowCalendar.clearSelection()
        if (literalValue > 5) singleRowCalendar.scrollToPosition(literalValue - 1)
        singleRowCalendar.select(literalValue - 1)
    }

    fun startVoiceRecognition(singleRowCalendar: SingleRowCalendar) {
        if (isAdded && isVisible) {
            runnable = Runnable {
                handler.sendEmptyMessage(0)
                Speech.init(requireActivity())
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "date picker speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {}

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString(), singleRowCalendar)
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(
                                TimelineView.TAG,
                                "onSpeechResult: " +
                                    result.lowercase(Locale.getDefault())
                            )
                            // Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible) {
                                            Speech.init(requireActivity())
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

    fun ttsDatePickerHint() {
        if (!firstTimeFlag) {
            textToSpeech = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val ttsLang = textToSpeech!!.setLanguage(myLocale)
                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                        ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                    ) {
                        Log.e("TTS", "The Language is not supported!")
                    } else {
                        Log.i("TTS", "Language Supported.")
                    }
                    Log.i("TTS", "Initialization success.")

                    if (textToSpeech!!.isSpeaking) {
                        ttsFlag = true
                    }

                    if (!textToSpeech!!.isSpeaking) {
                        ttsFlag = false
                    }

                    firstTimeFlag = true
                } else {
                    Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun shutdownTTS() {
        handler.removeCallbacksAndMessages(null)
        if (handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("Shutdown Date Picker SR")
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }
}
