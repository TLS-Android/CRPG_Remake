package com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun String.formatDateTime(originalFormat: String, ouputFormat: String): String {
    val date = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(originalFormat, Locale.ENGLISH))
    return date.format(DateTimeFormatter.ofPattern(ouputFormat, Locale.ENGLISH))
}