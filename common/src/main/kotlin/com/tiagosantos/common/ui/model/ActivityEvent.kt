package com.tiagosantos.common.ui.model

data class ActivityEvent(
    override val title: String?,
    override val info: String?,
    override val type: EventType = EventType.ACTIVITY,
    override val timestampData: TimestampData?,
    val activityType: ActivityType? = ActivityType.AULA
): Event()

enum class ActivityType {
    PASSEIO,
    AULA,
}


