package com.tiagosantos.common.ui.model

data class TimestampData(
    var startTime: String,
    val endTime: String,
    var date: String,
) {
    override fun toString(): String {
        return "start_time: ${this.startTime}, " + "end_time: ${this.endTime}, date: ${this.date}"
    }
}
