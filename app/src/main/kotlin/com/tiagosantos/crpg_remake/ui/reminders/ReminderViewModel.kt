package com.tiagosantos.crpg_remake.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.provider.AlarmClock
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.crpg_remake.domain.model.AlarmFrequency
import com.tiagosantos.crpg_remake.domain.model.AlarmType
import com.tiagosantos.crpg_remake.domain.model.Reminder
import com.tiagosantos.crpg_remake.domain.model.ReminderType
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.sql.DriverManager.println
import java.text.SimpleDateFormat
import java.util.*

//Reminders sao eventos criados pelo utilizador e que so sao acedidos pelo proprio
class ReminderViewModel(
    application: Application
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext!!
    private var mReminderList = ArrayList<Reminder>()
    val weekDaysBoolean: BooleanArray = booleanArrayOf(false, false, false,
            false, false, false, false)
    lateinit var alarmIntent: Intent

    var startTimeHours : String = ""
    var startTimeMin: String = ""
    var flagReminderAdded = false

    private val fullWeekAlarm: IntArray = intArrayOf(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY)

    var newReminder = Reminder("", "", "", 11, 0,
    "", "", ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE)

    @SuppressLint("SimpleDateFormat")
    fun addReminder(newReminder: Reminder){
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
        for (i in customWeekAlarm) println("> i value: $i")

        //data do dia de hoje
        val formatDDMMYYYY = SimpleDateFormat("ddMMyyyy")
        val date = Calendar.getInstance().time
        val formattedDateToday = formatDDMMYYYY.format(date)

        //data do dia de amanha
        val formattedDateTomorrow = getTomorrowDate(formatDDMMYYYY)

        setDateOnReminder(formattedDateToday, formattedDateTomorrow)

        when(newReminder.alarm_type){
            AlarmType.SOM -> setAlarmSoundOnly(fullWeekAlarm.toCollection(ArrayList()), customWeekAlarm)
            AlarmType.VIBRAR -> setAlarmVibrateOnly(fullWeekAlarm.toCollection(ArrayList()), customWeekAlarm)
            AlarmType.AMBOS -> setAlarmBoth(fullWeekAlarm.toCollection(ArrayList()), customWeekAlarm)
        }

        flagReminderAdded = true
        mReminderList.add(newReminder)
        updateFileWithReminders(mReminderList)
    }

    //data do dia de amanha
    private fun getTomorrowDate(formatDDMMYYYY: SimpleDateFormat): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, 1)
        return formatDDMMYYYY.format(c.time)
    }

    private fun setDateOnReminder(formattedDateToday: String, formattedDateTomorrow: String){
        when(newReminder.alarm_freq){
            AlarmFrequency.HOJE -> newReminder.date = formattedDateToday
            AlarmFrequency.AMANHA -> newReminder.date = formattedDateTomorrow
            AlarmFrequency.TODOS_OS_DIAS -> newReminder.date = "x"
            AlarmFrequency.PERSONALIZADO -> newReminder.date = "custom"
        }
    }

    private fun setAlarmVibrateOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        when (newReminder.alarm_freq) {
            //so vibracao
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> {
                println("Custom week alarm: $customWeekAlarm")
                this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.VALUE_RINGTONE_SILENT, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)}
            }
        }
    }

    fun setAlarmBoth(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        lateinit var alarmIntent: Intent
        println("Full week alarm: $fullWeekAlarm")

        when (newReminder.alarm_freq) {
            //c/ vibracao e som
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                //putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                //putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, TRUE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
            }
        }
    }

    fun setAlarmSoundOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        lateinit var alarmIntent: Intent

        println("Full week alarm: $fullWeekAlarm")

        when (newReminder.alarm_freq) {
            //sem vibracao
            AlarmFrequency.HOJE -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE); println("startTimeHours: $startTimeHours, startTimeMins:$startTimeMin")
            }
            AlarmFrequency.AMANHA -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
            }
            AlarmFrequency.TODOS_OS_DIAS -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                putExtra(AlarmClock.EXTRA_DAYS, fullWeekAlarm)
            }
            AlarmFrequency.PERSONALIZADO -> this.alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, newReminder.title)
                putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                putExtra(AlarmClock.EXTRA_VIBRATE, FALSE)
                putExtra(AlarmClock.EXTRA_DAYS, customWeekAlarm)
            }

        }
    }

    fun setNewReminder() {
        reminderVM.newReminder = Reminder(_, _, startTimeString, 1, 1, _, _, _, _, _)
        reminderVM.newReminder.start_time = startTimeString
        reminderVM.newReminder.hours = 1
        reminderVM.newReminder.mins = 1
    }
}


//private var reminder: MutableLiveData<String>? = null
// garantir que o lembrete e associado a data certa -> check
//    //garantir que os alarmes no telemovel sao ativados com a frequencia pretendida -> falta ativar alarme para dia seguinte
//    //garantir que o tipo de alarme e adequado ao que foi pedido pelo utilizador -> check
//
// fun retrieveListReminders(){}
//
// println("entrou no addReminder")
//        /*
//        val fullWeekAlarm = ArrayList<Int>()
//        fullWeekAlarm.add(Calendar.SUNDAY)
//        fullWeekAlarm.add(Calendar.MONDAY)
//        fullWeekAlarm.add(Calendar.TUESDAY)
//        fullWeekAlarm.add(Calendar.WEDNESDAY)
//        fullWeekAlarm.add(Calendar.THURSDAY)
//        fullWeekAlarm.add(Calendar.FRIDAY)
//        fullWeekAlarm.add(Calendar.SATURDAY)
//        */


//        /*
//        val c = Calendar.getInstance()
//        c.add(Calendar.DATE, 1)
//        val formattedDateTomorrow = formatDDMMYYYY.format(c.time)
//        */