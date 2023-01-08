package com.tiagosantos.crpg_remake.ui.reminders

import com.tiagosantos.common.ui.model.AlarmFrequency
import com.tiagosantos.common.ui.model.AlarmType
import com.tiagosantos.common.ui.model.Reminder
import com.tiagosantos.common.ui.model.ReminderType
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

import java.util.*

object ReminderRepository {

    val daytimeHoursMap = mapOf(
        "uma" to "01",
        "duas" to "02",
        "três" to "03",
        "quatro" to "04",
        "cinco" to "05",
        "seis" to "06",
        "sete" to "07",
        "oito" to "08",
        "nove" to "09",
        "dez" to "10",
        "onze" to "11",
    )

    val afternoonHoursMap = mapOf(
        "uma" to "13",
        "duas" to "14",
        "três" to "15",
        "quatro" to "16",
        "cinco" to "17",
        "seis" to "18",
        "sete" to "19",
        "sete" to "19",
    )

    val nightTimeHoursMap = mapOf(
        "oito" to "20",
        "nove" to "21",
        "dez" to "22",
        "onze" to "23",
    )

    val internalMap = mapOf(
        "uma" to "1",
        "duas" to "2",
        "três" to "3",
        "quatro" to "4",
        "cinco" to "5",
        "seis" to "6",
        "sete" to "7",
        "oito" to "8",
        "nove" to "9",
        "dez" to "10",
        "onze" to "11",
    )

    var newReminder = Reminder(
        EMPTY_STRING, EMPTY_STRING, EMPTY_STRING,
        0, 0, ReminderType.MEDICACAO, AlarmType.SOM, AlarmFrequency.HOJE
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
