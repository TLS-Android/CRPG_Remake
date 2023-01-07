package com.tiagosantos.crpg_remake.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.*
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.common.ui.model.AlarmFrequency.HOJE
import com.tiagosantos.common.ui.model.AlarmFrequency.TODOS_OS_DIAS
import com.tiagosantos.common.ui.model.AlarmFrequency.PERSONALIZADO
import com.tiagosantos.common.ui.model.AlarmType
import com.tiagosantos.common.ui.model.AlarmType.SOM
import com.tiagosantos.common.ui.model.AlarmType.VIBRAR
import com.tiagosantos.common.ui.model.AlarmType.AMBOS
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.utils.fullWeekAlarm
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

@Suppress("DUPLICATE_LABEL_IN_WHEN")
class ReminderViewModel(
    application: Application,
    context: Context
) : AndroidViewModel(application) {

    private var mReminderList = ArrayList<Reminder>()
    private val weekDaysBoolean: BooleanArray = booleanArrayOf(
        false, false, false,
        false, false, false, false
    )
    lateinit var alarmIntent: Intent

    /** MutableLiveData should always be val; only the contents can be updated **/
    private val _startTimeHours = MutableLiveData<String?>()
    val startTimeHours: LiveData<String?> = _startTimeHours

    private val _startTimeMin = MutableLiveData<String?>()
    val startTimeMin: LiveData<String?> = _startTimeMin

    private val _startTimeString = MutableLiveData<String?>()
    val startTimeString: LiveData<String?> = _startTimeString

    var flagReminderAdded = false

    var newReminder = Reminder(
        "", "", "", 11, 0,
        ReminderType.MEDICACAO, SOM, HOJE
    )

    companion object{
        val weekMap = mapOf(
            0 to MONDAY, 1 to TUESDAY, 2 to WEDNESDAY, 3 to THURSDAY,
            4 to FRIDAY, 5 to SATURDAY, 6 to SUNDAY
        )
        val date: Date = getInstance().time
    }

    @SuppressLint("SimpleDateFormat")
    fun addReminder(newReminder: Reminder) {
        val customWeekAlarm = mutableListOf<Int>().apply {
            for ((idx, value) in weekDaysBoolean.withIndex()) {
                if (value) add(weekMap.getOrDefault(idx, null)!!)
            }
        }

        val formatDDMMYYYY = SimpleDateFormat("ddMMyyyy")
        val formattedDateToday = formatDDMMYYYY.format(date)
        setDateOnReminder(formattedDateToday)

        flagReminderAdded = true
        mReminderList.add(newReminder)

        setAlarm(fullWeekAlarm.toCollection(ArrayList()),
                customWeekAlarm.toCollection(ArrayList<Int>()), newReminder.alarmType)
    }

    private fun setAlarm(
        fullWeekAlarm: ArrayList<Int>,
        customWeekAlarm: ArrayList<Int>,
        alarmType: AlarmType?
    ) {
        this.alarmIntent = Intent(ACTION_SET_ALARM).apply {
            putExtra(EXTRA_MESSAGE, newReminder.title)
            putExtra(EXTRA_HOUR, startTimeHours.value!!.toInt())
            putExtra(EXTRA_MINUTES, startTimeMin.value!!.toInt())
            when(newReminder.alarmFreq){
                TODOS_OS_DIAS -> putExtra(EXTRA_DAYS, fullWeekAlarm)
                PERSONALIZADO -> putExtra(EXTRA_DAYS, customWeekAlarm)
                else -> { println("No valid frequency was found") }
            }
            when(alarmType){
                VIBRAR, AMBOS -> putExtra(EXTRA_VIBRATE, TRUE)
                VIBRAR -> putExtra(VALUE_RINGTONE_SILENT, TRUE)
                SOM -> putExtra(EXTRA_VIBRATE, FALSE)
                else -> { println("setAlarm") }
            }
        }
    }

    private fun getTomorrowDate(formatDDMMYYYY: SimpleDateFormat): String {
        val c = getInstance().apply { add(DATE, 1) }
        return formatDDMMYYYY.format(c.time)
    }

    private fun setDateOnReminder(formattedDateToday: String) {
        when (newReminder.alarmFreq) {
            HOJE -> newReminder.date = formattedDateToday
            TODOS_OS_DIAS -> newReminder.date = null
            PERSONALIZADO -> newReminder.date = null
            else -> {
                newReminder.date = null
            }
        }
    }

    fun setTime(et: EditText, etMin: EditText) {
        _startTimeHours.value = et.text.toString()
        _startTimeMin.value = etMin.text.toString()
        _startTimeString.value = _startTimeHours.value.plus(startTimeMin)
    }
}
