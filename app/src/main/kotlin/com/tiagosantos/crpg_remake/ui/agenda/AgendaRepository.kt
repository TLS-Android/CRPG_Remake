package com.tiagosantos.crpg_remake.ui.agenda

import com.tiagosantos.common.ui.model.*

object AgendaRepository {

    var mockLunchEvent = Event(
        eventTitle = "Almoço",
        eventInfo = "Clicar para escolher refeição",
        eventDescription ="description",
        EventType.MEAL,
        MealChoice(ChosenMealDish.MEAT, MealType.LUNCH),
        TimestampData(startTime = "12:00", endTime = "13:00", date = "21")
    )

    var mockDinnerEvent = Event(
        eventTitle = "Jantar",
        eventInfo = "Clicar para escolher refeição",
        eventDescription = "description",
        EventType.MEAL,
        MealChoice(ChosenMealDish.DIET, MealType.DINNER),
        TimestampData(startTime = "20:00", endTime = "21:00", date = "21")
    )

    var mockActivityEvent = Event(
        eventTitle = "Actividade",
        eventInfo = "Clicar para mais info",
        eventDescription = "description",
        EventType.ACTIVITY,
        null,
        TimestampData(startTime = "16:00", endTime = "17:00", date = "21")
    )
}
