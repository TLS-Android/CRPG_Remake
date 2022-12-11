package com.tiagosantos.crpg_remake.data.local

import android.app.Application
import com.google.gson.Gson
import com.tiagosantos.common.ui.model.*
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.Constants.REMINDER_FILENAME
import com.tiagosantos.common.ui.utils.Constants.SLASH
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.util.*

class ImplReminderPublicLocalSource(
    application: Application
) : RemindersPublicLocalSource {

    /**
    The init block will execute immediately after the primary constructor.
    Initializer blocks effectively become part of the primary constructor.

    The constructor is the secondary constructor. Delegation to the primary constructor happens
    as the first statement of a secondary constructor, so the code in all initializer blocks
    is executed before the secondary constructor body.

    The primary constructor cannot contain any code.
    Initialization code can be placed in initializer blocks, which are prefixed with the init keyword.

    (from https://stackoverflow.com/questions/55356837/what-is-the-difference-between-init-block-and-constructor-in-kotlin)
    */
    init {
        val ctx = application.applicationContext
        val gson = Gson()
        val filename = REMINDER_FILENAME
        val fullFilename = ctx.filesDir.toString() + SLASH + filename
        val file = File(fullFilename)
        val fileExists = file.exists()
        val messagesList = ArrayList<Reminder>()
        val isNewFileCreated : Boolean = file.createNewFile()
    }

    val newReminder = Reminder(
        EMPTY_STRING,
        EMPTY_STRING,
        EMPTY_STRING,
        0,
        0,
        ReminderType.MEDICACAO,
        AlarmType.SOM,
        AlarmFrequency.HOJE
    )

    override fun getFakeRemindersList(meal: Meal): Flow<List<Meal>> {
        TODO("Not yet implemented")
    }

    private val mockUserOne = Reminder(
        UUID.randomUUID().toString(),
        "Fake Name",
        "https://i.pravatar.cc/128?img=5",
        11,
        12,
    )

    private val mockUserTwo = Reminder(
        UUID.randomUUID().toString(),
        "Fake Name",
        "https://i.pravatar.cc/128?img=5",
        12,
        13
    )

    private val messages = listOf(
        "Wuz Up! Lorem Ipsum is simply dummy text of printing",
        "How are you? =)",
        "It has survived not only five centuries, but also the leap into electronic typesetting",
        "Contrary to popular belief. is the Lorem Ipsum is not simply then random text",
        "Hi. I want to see you!",
        "Yeah. Me too. Let's go out"
    )

    val fileContent = """[{"title": "Tomar Medicação","info":"benuron","start_time": 
        |"1130","hours":11,"minutes":30,"notas":""}]""".trimMargin()

}