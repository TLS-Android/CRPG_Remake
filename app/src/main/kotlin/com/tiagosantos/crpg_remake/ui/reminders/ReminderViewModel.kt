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
        ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE
    )

    @SuppressLint("SimpleDateFormat")
    fun addReminder(newReminder: Reminder) {
        val customWeekAlarmMutable = mutableListOf<Int>()
        for ((idx, value) in weekDaysBoolean.withIndex()) {
            if (value) {
                when (idx) {
                    0 -> customWeekAlarmMutable.add(MONDAY)
                    1 -> customWeekAlarmMutable.add(TUESDAY)
                    2 -> customWeekAlarmMutable.add(WEDNESDAY)
                    3 -> customWeekAlarmMutable.add(THURSDAY)
                    4 -> customWeekAlarmMutable.add(FRIDAY)
                    5 -> customWeekAlarmMutable.add(SATURDAY)
                    6 -> customWeekAlarmMutable.add(SUNDAY)
                }
            }
        }

        val customWeekAlarm = customWeekAlarmMutable.toCollection(ArrayList<Int>())

        //data do dia de hoje
        val formatDDMMYYYY = SimpleDateFormat("ddMMyyyy")
        val date = Calendar.getInstance().time
        val formattedDateToday = formatDDMMYYYY.format(date)

        //data do dia de amanha
        val formattedDateTomorrow = getTomorrowDate(formatDDMMYYYY)

        setDateOnReminder(formattedDateToday, formattedDateTomorrow)

        when (newReminder.alarm_type) {
            AlarmType.SOM -> setAlarmSoundOnly(
                fullWeekAlarm.toCollection(ArrayList()),
                customWeekAlarm
            )
            AlarmType.VIBRAR -> setAlarmVibrateOnly(
                fullWeekAlarm.toCollection(ArrayList()),
                customWeekAlarm
            )
            AlarmType.AMBOS -> setAlarmBoth(
                fullWeekAlarm.toCollection(ArrayList()),
                customWeekAlarm
            )
            else -> { setAlarmSoundOnly(fullWeekAlarm.toCollection(ArrayList()), customWeekAlarm) }
        }

        flagReminderAdded = true
        mReminderList.add(newReminder)
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

    private fun setAlarmVibrateOnly(
        fullWeekAlarm: ArrayList<Int>,
        customWeekAlarm: ArrayList<Int>
    ) {
        this.alarmIntent = when (mockReminder.alarm_freq) {
            HOJE -> Intent(ACTION_SET_ALARM).apply {
                putExtra(EXTRA_MESSAGE, mockReminder.title)
                putExtra(EXTRA_HOUR, startTimeHours.toInt())
                putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(EXTRA_VIBRATE, TRUE)
                putExtra(VALUE_RINGTONE_SILENT, TRUE)
            }
            AMANHA -> Intent(ACTION_SET_ALARM).apply {
                putExtra(EXTRA_MESSAGE, mockReminder.title)
                putExtra(EXTRA_VIBRATE, TRUE)
                putExtra(VALUE_RINGTONE_SILENT, TRUE)
            }
            TODOS_OS_DIAS -> Intent(ACTION_SET_ALARM).apply {
                    putExtra(EXTRA_MESSAGE, mockReminder.title)
                    putExtra(EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(EXTRA_VIBRATE, TRUE)
                    putExtra(VALUE_RINGTONE_SILENT, TRUE)
                    putExtra(EXTRA_DAYS, fullWeekAlarm)
                }
            PERSONALIZADO -> Intent(ACTION_SET_ALARM).apply {
                    putExtra(EXTRA_MESSAGE, mockReminder.title)
                    putExtra(EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(EXTRA_VIBRATE, TRUE)
                    putExtra(VALUE_RINGTONE_SILENT, TRUE)
                    putExtra(EXTRA_DAYS, customWeekAlarm)
                }
            else -> { return }
        }

    }

    private fun setAlarmBoth(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        this.alarmIntent = when (mockReminder.alarm_freq) {
            HOJE -> Intent(ACTION_SET_ALARM).apply {
                putExtra(EXTRA_MESSAGE, mockReminder.title)
                putExtra(EXTRA_HOUR, startTimeHours.toInt())
                putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(EXTRA_VIBRATE, TRUE)
            }
            AMANHA ->Intent(ACTION_SET_ALARM).apply {
                putExtra(EXTRA_MESSAGE, mockReminder.title)
                putExtra(EXTRA_VIBRATE, TRUE)
            }
           TODOS_OS_DIAS ->
                Intent(ACTION_SET_ALARM).apply {
                    putExtra(EXTRA_MESSAGE, mockReminder.title)
                    putExtra(EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(EXTRA_VIBRATE, TRUE)
                    putExtra(EXTRA_DAYS, fullWeekAlarm)
                }
            PERSONALIZADO ->
                Intent(ACTION_SET_ALARM).apply {
                    putExtra(EXTRA_MESSAGE, mockReminder.title)
                    putExtra(EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(EXTRA_VIBRATE, TRUE)
                    putExtra(EXTRA_DAYS, customWeekAlarm)
                }
            else -> { return }
        }
    }

    private fun setAlarmSoundOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {

        this.alarmIntent = Intent(ACTION_SET_ALARM).apply {
            putExtra(EXTRA_MESSAGE, mockReminder.title)
            putExtra(EXTRA_HOUR, startTimeHours.toInt())
            putExtra(EXTRA_MINUTES, startTimeMin.toInt())
            putExtra(EXTRA_VIBRATE, FALSE)
            when(mockReminder.alarm_freq){
                TODOS_OS_DIAS -> putExtra(EXTRA_DAYS, fullWeekAlarm)
                PERSONALIZADO -> putExtra(EXTRA_DAYS, customWeekAlarm)
                else -> { println("No valid frequency was found") }
            }
        }
    }
}