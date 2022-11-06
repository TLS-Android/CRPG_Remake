package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName

data class Reminder(
    var title: String = "title",
    val info: String = "info",
    var date: String? = "x",
    var hours: Int,
    var mins: Int,
    var reminder_type: ReminderType ?= ReminderType.REFEICAO,
    var alarm_type: AlarmType ?= AlarmType.AMBOS,
    var alarm_freq: AlarmFrequency ?= AlarmFrequency.HOJE,
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}"
    }
}

enum class ReminderType {
    @SerializedName("MEDICACAO")
    MEDICACAO,
    @SerializedName("TRANSPORTE")
    TRANSPORTE,
    @SerializedName("REFEICAO")
    REFEICAO,
    @SerializedName("PERSONALIZADO")
    PERSONALIZADO
}

enum class AlarmType {
    @SerializedName("SOM")
    SOM,
    @SerializedName("VIBRAR")
    VIBRAR,
    @SerializedName("AMBOS")
    AMBOS
}

enum class AlarmFrequency {
    @SerializedName("HOJE")
    HOJE,
    @SerializedName("AMANHA")
    AMANHA,
    @SerializedName("TODOS_OS_DIAS")
    TODOS_OS_DIAS,
    @SerializedName("PERSONALIZADO")
    PERSONALIZADO
}

enum class Visibility{
    VISIBLE,
    INVISIBLE
}
