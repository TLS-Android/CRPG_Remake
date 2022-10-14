package com.tiagosantos.crpg_remake.ui.reminders

import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.domain.model.AlarmFrequency
import com.tiagosantos.crpg_remake.domain.model.AlarmType
import com.tiagosantos.crpg_remake.domain.model.Reminder
import com.tiagosantos.crpg_remake.domain.model.ReminderType
import java.util.*

object ReminderRepository {

    var newReminder = Reminder(
        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
        0, 0, EMPTY_STRING, EMPTY_STRING,
        ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE
    )

    val weekDaysBoolean: BooleanArray = booleanArrayOf(
        false, false, false,
        false, false, false, false
    )

    val fullWeekAlarm: IntArray = intArrayOf(
        Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
        Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
    )

    val list = listOf(
        Triple(1, "Tomar medicacao", ReminderType.MEDICACAO),
        Triple(2, "Apanhar bus do CRPG", ReminderType.TRANSPORTE),
        Triple(3, "Lembrar escolha de almoço", ReminderType.REFEICAO),
    )

    val numbersMap = mapOf(
        0 to Calendar.MONDAY,
        1 to Calendar.TUESDAY,
        2 to Calendar.WEDNESDAY,
        3 to Calendar.THURSDAY,
        4 to Calendar.FRIDAY,
        5 to Calendar.SATURDAY,
        6 to Calendar.SUNDAY
    )

}
