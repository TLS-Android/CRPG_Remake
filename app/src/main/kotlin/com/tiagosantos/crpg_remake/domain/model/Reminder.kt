package com.tiagosantos.crpg_remake.domain.model

import com.google.gson.annotations.SerializedName

data class Reminder(
        var title: String,
        val info: String,
        var start_time: String,
        var hours: Int,
        var mins: Int,
        var date: String,
        val notas: String,
        var reminder_type: ReminderType,
        var alarm_type: AlarmType,
        var alarm_freq: AlarmFrequency
){
    override fun toString(): String {
        return "title: ${this.title}, info: ${this.info}, start_time: ${this.start_time}, " +
                " date: ${this.date}"
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
