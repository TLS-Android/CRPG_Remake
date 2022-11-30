package com.tiagosantos.crpg_remake.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.model.AlarmFrequency
import com.tiagosantos.common.ui.model.AlarmType
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.utils.fullWeekAlarm
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.text.SimpleDateFormat
import java.util.*

class ReminderViewModel(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context: Context = application.applicationContext
    private var mReminderList = ArrayList<Reminder>()
    private val weekDaysBoolean: BooleanArray = booleanArrayOf(
        false, false, false,
        false, false, false, false
    )
    lateinit var alarmIntent: Intent

    var startTimeHours: String = ""
    var startTimeMin: String = ""
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
                    0 -> customWeekAlarmMutable.add(Calendar.MONDAY)
                    1 -> customWeekAlarmMutable.add(Calendar.TUESDAY)
                    2 -> customWeekAlarmMutable.add(Calendar.WEDNESDAY)
                    3 -> customWeekAlarmMutable.add(Calendar.THURSDAY)
                    4 -> customWeekAlarmMutable.add(Calendar.FRIDAY)
                    5 -> customWeekAlarmMutable.add(Calendar.SATURDAY)
                    6 -> customWeekAlarmMutable.add(Calendar.SUNDAY)
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
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, 1)
        return formatDDMMYYYY.format(c.time)
    }

    private fun setDateOnReminder(formattedDateToday: String, formattedDateTomorrow: String) {
        when (mockReminder.alarm_freq) {
            AlarmFrequency.HOJE -> mockReminder.date = formattedDateToday
            AlarmFrequency.AMANHA -> mockReminder.date = formattedDateTomorrow
            AlarmFrequency.TODOS_OS_DIAS -> mockReminder.date = null
            AlarmFrequency.PERSONALIZADO -> mockReminder.date = null
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
            AlarmFrequency.HOJE -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.AMANHA -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                    putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                    putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
                }
            AlarmFrequency.PERSONALIZADO -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                    putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                    putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
                }
            else -> { return }
        }

    }

    private fun setAlarmBoth(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        this.alarmIntent = when (mockReminder.alarm_freq) {
            AlarmFrequency.HOJE -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.AMANHA ->Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS ->
                Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                    putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
                }
            AlarmFrequency.PERSONALIZADO ->
                Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                    putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
                }
            else -> { return }
        }
    }

    private fun setAlarmSoundOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        this.alarmIntent =when (mockReminder.alarm_freq) {
            //sem vibracao
            AlarmFrequency.HOJE ->  Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
            }
            AlarmFrequency.AMANHA -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                    putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
                }
            AlarmFrequency.PERSONALIZADO -> Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_MESSAGE, mockReminder.title)
                    putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                    putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                    putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
                }
            else -> { return }
        }
    }
}