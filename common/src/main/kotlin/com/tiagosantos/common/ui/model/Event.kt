package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

data class Event(
    var title: String? = EMPTY_STRING,
    val info: String? = EMPTY_STRING,
    val notes: String? = EMPTY_STRING,
    var type: EventType,
    val mealChoice: MealChoice,
    val timestampData: TimestampData,
) {
    override fun toString(): String {
        return "EVENT: title: ${this.title}, info: ${this.info}, " +
                "type: ${this.type}, meal_Int: ${this.mealChoice} \\n "
    }
}

enum class EventType {
    @SerializedName("ACTIVITY")
    ACTIVITY,
    @SerializedName("MEAL")
    MEAL,
}
