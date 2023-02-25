package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName

/**
 * A sealed class is abstract by itself,
 * it cannot be instantiated directly and can have abstract members.
 */
sealed class Event {
    abstract val title: String?
    abstract val info: String?
    abstract val type: EventType
    abstract val timestampData: TimestampData?

    override fun toString(): String {
        return "EVENT: title: ${this.title}, info: ${this.info}, " +
                "type: ${this.type} \\n "
    }
}

enum class EventType {
    @SerializedName("ACTIVITY")
    ACTIVITY,
    @SerializedName("MEAL")
    MEAL,
}
