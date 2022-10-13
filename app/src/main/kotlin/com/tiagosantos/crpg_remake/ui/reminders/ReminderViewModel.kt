package com.plataforma.crpg.ui.reminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.*
import java.io.File
import java.io.FileReader
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.lang.reflect.Type
import java.sql.DriverManager.println
import java.text.SimpleDateFormat
import java.util.*

//Reminders sao eventos criados pelo utilizador e que so sao acedidos pelo proprio
class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext!!
    // a lista de Reminders contem todos os reminders criados pelo utilizador que ainda vao acontecer no futuro
    private var mReminderList = ArrayList<Reminder>()
    //for each day of the week, a boolean indicates if the alarm is set for that day
    val weekDaysBoolean: BooleanArray = booleanArrayOf(false, false, false,
            false, false, false, false)
    lateinit var alarmIntent: Intent

    var newReminder = Reminder("", "", "", 0, 0, "","",
            ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE)

    var startTimeHours : String = ""
    var startTimeMin: String = ""
    var flagReminderAdded = false

    private val fullWeekAlarm: IntArray = intArrayOf(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
            Calendar.WEDNESDAY, Calendar.THURSDAY,Calendar.FRIDAY,Calendar.SATURDAY)

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun addReminder(){

        println(">add Reminder")

        newReminder = Reminder("", "", "", 11, 0,
                "", "", ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE)
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

        //println("Custom week alarm mutable: $customWeekAlarmMutable")

        println("Full week: $fullWeekAlarm")

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


    private fun populateFile() {
        val filename = "reminder.json"
        val fullFilename = context.filesDir.toString() + "/" + filename
        val file = File(fullFilename)

        // create a new file
        val isNewFileCreated : Boolean = file.createNewFile()

//        var title: String,
//        val info: String,
//        var start_time: String,
//        var date: String,
//        val notas: String,
//        var reminder_type: ReminderType,
//        var alarm_type: AlarmType,
//        var alarm_freq: AlarmFrequency

        if(isNewFileCreated){
            kotlin.io.println("$fullFilename is created successfully.")
        } else{
            kotlin.io.println("$fullFilename already exists.")
        }
        val fileContent = """[{"title": "Tomar Medicação","info":"benuron","start_time": "1130","hours":11,"minutes":30,"notas":""}]""".trimMargin()

        File(fullFilename).writeText(fileContent)
    }

    fun startNewFileAndPopulate(){
        populateFile()
        getAllRemindersList()
    }


    fun getAllRemindersList(): ArrayList<Reminder> {

        val gson = Gson()
        val filename = "reminder.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val type: Type = object : TypeToken<ArrayList<Reminder>>() {}.type
        val reminderList: ArrayList<Reminder> = gson.fromJson(FileReader(fullFilename), type)

        println("> From JSON Meal String Reminder Collection:" + reminderList.toString())

        return reminderList

    }

    private fun updateFileWithReminders(mReminderList: ArrayList<Reminder>) {

        println("Reminder list: $mReminderList")

        val gson = Gson()
        val filename = "reminders.json"
        val fullFilename = context.filesDir.toString() + "/" + filename

        val newJSONList = gson.toJson(mReminderList)

        val file = File(fullFilename)
        val fileExists = file.exists()

        if (fileExists) {
            File(fullFilename).writeText(newJSONList)
        }
    }

    //data do dia de amanha
    private fun getTomorrowDate(formatDDMMYYYY: SimpleDateFormat): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, 1)
        val formattedDateTomorrow = formatDDMMYYYY.format(c.time)
        return formattedDateTomorrow
    }

    private fun setDateOnReminder(formattedDateToday: String, formattedDateTomorrow: String){
        when(newReminder.alarm_freq){
            AlarmFrequency.HOJE -> newReminder.date = formattedDateToday
            AlarmFrequency.AMANHA -> newReminder.date = formattedDateTomorrow
            AlarmFrequency.TODOS_OS_DIAS -> newReminder.date = "x"
            AlarmFrequency.PERSONALIZADO -> newReminder.date = "custom"
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setAlarmVibrateOnly(fullWeekAlarm: ArrayList<Int>, customWeekAlarm: ArrayList<Int>) {
        println("Custom week alarm: $customWeekAlarm")
        println("Full week alarm: $fullWeekAlarm")

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
                //putExtra(AlarmClock.EXTRA_HOUR, startTimeHours.toInt())
                //putExtra(AlarmClock.EXTRA_MINUTES, startTimeMin.toInt())
                //AlarmClock nao permite definir alarme para dia especifico
                //Tentar ver se e possivel usando Intents
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
            else -> println("Não entrou em nenhuma das opcoes")
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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