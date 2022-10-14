package com.tiagosantos.crpg_remake.data.local

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.model.Meal
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.domain.model.Reminder
import com.tiagosantos.common.ui.utils.GeneralUtils.LOG_TAG_DEBUG
import com.tiagosantos.crpg_remake.domain.model.AlarmFrequency
import com.tiagosantos.crpg_remake.domain.model.AlarmType
import com.tiagosantos.crpg_remake.domain.model.ReminderType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type
import java.sql.DriverManager
import java.util.*

class ImplReminderPublicLocalSource(application: Application) :
    RemindersPublicLocalSource {

    var newReminder = Reminder(
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        0,
        0,
        EMPTY_STRING,
        EMPTY_STRING,
        ReminderType.MEDICACAO,
        AlarmType.SOM,
        AlarmFrequency.HOJE
    )

    override fun getFakeRemindersList(Reminder: Reminder): Flow<List<Reminder>> =
        flow {
            Log.d(LOG_TAG_DEBUG,
                "getFakeMessagesList: Thread : ${Thread.currentThread().name}")

            val messagesList = ArrayList<Reminder>()

            val userFirst = Reminder(
                UUID.randomUUID().toString(),
                "Fake Name",
                "https://i.pravatar.cc/128?img=5",
                11,
                12,


            )

            val userSecond = Reminder(
                UUID.randomUUID().toString(),
                "Fake Name",
                "https://i.pravatar.cc/128?img=5",
                "ola",
                "adeus"
            )

            val messages = listOf(
                "Wuz Up! Lorem Ipsum is simply dummy text of printing",
                "How are you? =)",
                "It has survived not only five centuries, but also the leap into electronic typesetting",
                "Contrary to popular belief. is the Lorem Ipsum is not simply then random text",
                "Hi. I want to see you!",
                "Yeah. Me too. Let's go out"
            )

            messages.forEachIndexed { _, message ->
                Reminder(
                    UUID.randomUUID().toString(),
                    message,
                    message,
                    message,
                    message
                )
                    .let(messagesList::add)
                    .also {
                        emit(messagesList)
                    }

                delay(1000)
            }

        }

    override fun getFakeRemindersList(meal: Meal): Flow<List<Meal>> {
        TODO("Not yet implemented")
    }

    private fun populateFile() {
        val filename = "reminder.json"
        val fullFilename = application .filesDir.toString() + "/" + filename
        val file = File(fullFilename)

        // create a new file
        val isNewFileCreated : Boolean = file.createNewFile()

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

        DriverManager.println("> From JSON Meal String Reminder Collection:" + reminderList.toString())

        return reminderList

    }

    private fun updateFileWithReminders(mReminderList: ArrayList<Reminder>) {

        DriverManager.println("Reminder list: $mReminderList")

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
}