package com.tiagosantos.crpg_remake.data.local

import android.util.Log
import com.tiagosantos.common.ui.model.Meal
import com.tiagosantos.common.ui.utils.GeneralUtils.LOG_TAG_DEBUG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class ImplMealPublicLocalSource : MealPublicLocalSource {

    override fun getFakeMessagesList(meal: Meal): Flow<List<Meal>> =
        flow {
            Log.d(LOG_TAG_DEBUG,
                "getFakeMessagesList: Thread : ${Thread.currentThread().name}")

            val messagesList = ArrayList<Meal>()

            val userFirst = Meal(
                UUID.randomUUID().toString(),
                "Fake Name",
                "https://i.pravatar.cc/128?img=5",
                "ola",
                "adeus"
            )

            val userSecond = Meal(
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
                Meal(
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
}