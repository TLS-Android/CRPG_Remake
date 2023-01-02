package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName

data class Reminder(
    var title: String = "ipsis",
    val info: String = "lorem",
    var date: String? = "01/01/2000",
    var hours: Int? = 23,
    var mins: Int? = 59,
    var reminderType: ReminderType ?= ReminderType.REFEICAO,
    var alarmType: AlarmType ?= AlarmType.AMBOS,
    var alarmFreq: AlarmFrequency ?= AlarmFrequency.HOJE,
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
    @SerializedName("TODOS_OS_DIAS")
    TODOS_OS_DIAS,
    @SerializedName("PERSONALIZADO")
    PERSONALIZADO
}

enum class Visibility{
    VISIBLE,
    INVISIBLE
}
