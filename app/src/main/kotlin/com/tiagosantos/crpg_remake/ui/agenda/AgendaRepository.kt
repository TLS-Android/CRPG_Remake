package com.tiagosantos.crpg_remake.ui.agenda

import com.tiagosantos.common.ui.model.*

object AgendaRepository {

    var mockLunchEvent = MealEvent(
        title = "Almoço",
        info = "Clicar para escolher refeição",
        timestampData = TimestampData(
            startTime = "12:00",
            endTime = "13:00", date = "21"
        ),
    )

    var mockDinnerEvent = MealEvent(
        title = "Jantar",
        info = "Clicar para escolher refeição",
        timestampData = TimestampData(
            startTime = "20:00",
            endTime = "21:00",
            date = "21"
        )
    )

    var mockActivityEvent = ActivityEvent(
        title = "Actividade",
        info = "Clicar para mais info",
        timestampData = TimestampData(
            startTime = "16:00",
            endTime = "17:00",
            date = "21"
        )
    )

    var mockActivityStrollEvent = ActivityEvent(
        title = "Passeio",
        info = "Clicar para mais info",
        timestampData = TimestampData(
            startTime = "18:00",
            endTime = "19:00",
            date = "21"
        ),
        activityType = ActivityType.PASSEIO
    )
}
