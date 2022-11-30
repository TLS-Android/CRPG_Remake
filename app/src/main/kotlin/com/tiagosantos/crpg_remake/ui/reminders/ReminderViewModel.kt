package com.tiagosantos.crpg_remake.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock.*
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.model.AlarmFrequency.HOJE
import com.tiagosantos.common.ui.model.AlarmFrequency.AMANHA
import com.tiagosantos.common.ui.model.AlarmFrequency.TODOS_OS_DIAS
import com.tiagosantos.common.ui.model.AlarmFrequency.PERSONALIZADO
import com.tiagosantos.common.ui.model.AlarmType
import com.tiagosantos.common.ui.model.AlarmType.SOM
import com.tiagosantos.common.ui.model.AlarmType.VIBRAR
import com.tiagosantos.common.ui.model.AlarmType.AMBOS
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.fullWeekAlarm
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

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

    var startTimeHours: String = EMPTY_STRING
    var startTimeMin: String = EMPTY_STRING
    var flagReminderAdded = false

    var mockReminder = Reminder(
        "", "", "x", 11, 0,
        ReminderType.MEDICACAO, SOM, HOJE
    )

    companion object{
        val weekMap = mapOf(
            0 to MONDAY, 1 to TUESDAY, 2 to WEDNESDAY, 3 to THURSDAY,
            4 to FRIDAY, 5 to SATURDAY, 6 to SUNDAY
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun addReminder(newReminder: Reminder) {

        weekDaysBoolean.associateWith {  }

        val customWeekAlarm = mutableListOf<Int>().apply {
            for ((idx, value) in weekDaysBoolean.withIndex()) {
                if(value) add(weekMap.getOrDefault(idx, null)!!)
            }

            //data do dia de hoje
            val formatDDMMYYYY = SimpleDateFormat("ddMMyyyy")
            val date = getInstance().time
            val formattedDateToday = formatDDMMYYYY.format(date)

            //data do dia de amanha
            val formattedDateTomorrow = getTomorrowDate(formatDDMMYYYY)
            setDateOnReminder(formattedDateToday, formattedDateTomorrow)

            flagReminderAdded = true
            mReminderList.add(newReminder)

            setAlarm(fullWeekAlarm.toCollection(ArrayList()),
                customWeekAlarm.toCollection(ArrayList<Int>()), newReminder.alarm_type)
        }
    }

    private fun setAlarm(
        fullWeekAlarm: ArrayList<Int>,
        customWeekAlarm: ArrayList<Int>,
        alarmType: AlarmType?
    ) {
        this.alarmIntent = Intent(ACTION_SET_ALARM).apply {
            putExtra(EXTRA_MESSAGE, mockReminder.title)
            putExtra(EXTRA_HOUR, startTimeHours.toInt())
            putExtra(EXTRA_MINUTES, startTimeMin.toInt())
            when(mockReminder.alarm_freq){
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

    //data do dia de amanha
    private fun getTomorrowDate(formatDDMMYYYY: SimpleDateFormat): String {
        val c = getInstance()
        c.add(DATE, 1)
        return formatDDMMYYYY.format(c.time)
    }

    private fun setDateOnReminder(formattedDateToday: String, formattedDateTomorrow: String) {
        when (mockReminder.alarm_freq) {
            HOJE -> mockReminder.date = formattedDateToday
            AMANHA -> mockReminder.date = formattedDateTomorrow
            TODOS_OS_DIAS -> mockReminder.date = null
            PERSONALIZADO -> mockReminder.date = null
            else -> {
                mockReminder.date = "x"
            }
        }
    }
}
