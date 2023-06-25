package com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions

import android.text.InputFilter
import android.widget.EditText
import com.tiagosantos.common.ui.utils.InputFilterMinMax
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun String.formatDateTime(originalFormat: String, ouputFormat: String): String {
    val date = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(originalFormat, Locale.ENGLISH))
    return date.format(DateTimeFormatter.ofPattern(ouputFormat, Locale.ENGLISH))
}

fun EditText.filterTime(min: String, max: String): EditText {
     filters = arrayOf(InputFilterMinMax("00", "23"), InputFilter.LengthFilter(2))
}
