package com.tiagosantos.common.ui.utils

import java.text.SimpleDateFormat
import java.util.*

enum class TimeFormat {
    AM_PM
}

val fullWeekAlarm: IntArray = intArrayOf(
    Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY,
    Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY
)

fun Long.getTimeString(format: TimeFormat): String {
    val date = Date(this)

    return when (format) {
        TimeFormat.AM_PM ->
            SimpleDateFormat(TimePattern.amPm, Locale.getDefault()).apply {
                timeZone = Calendar.getInstance().timeZone
            }.format(date)

    }
}

private object TimePattern {
    const val amPm = "h:mm a"
}
