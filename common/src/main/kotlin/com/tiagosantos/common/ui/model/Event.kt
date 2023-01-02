package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

data class Event(
    var eventTitle: String? = EMPTY_STRING,
    val eventInfo: String? = EMPTY_STRING,
    val eventDescription: String? = EMPTY_STRING,
    var eventType: EventType,
    val mealChoice: MealChoice,
    val timestampData: TimestampData,
) {
    override fun toString(): String {
        return "EVENT: title: ${this.eventTitle}, info: ${this.eventInfo}, " +
                "description: ${this.eventDescription} \\n "
    }
}

enum class EventType {
    @SerializedName("ACTIVITY")
    ACTIVITY,
    @SerializedName("MEAL")
    MEAL,
}
