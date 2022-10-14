package com.tiagosantos.crpg_remake.data.local

import android.util.Log
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
import java.util.*

class ImplReminderPublicLocalSource : RemindersPublicLocalSource {

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
}