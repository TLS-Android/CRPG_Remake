package com.tiagosantos.crpg_remake.ui.agenda

import com.tiagosantos.common.ui.model.*

object AgendaRepository {

    var mockLunchEvent = Event(
        "Almoço",
        "Clicar para escolher refeição",
        "description",
        EventType.MEAL,
        MealChoice(ChosenMealDish.MEAT, MealType.LUNCH),
        TimestampData(startTime = "12:00", endTime = "13:00", date = "21")
    )

    var mockDinnerEvent = Event(
        "Jantar",
        "Clicar para escolher refeição",
        "description",
        EventType.MEAL,
        MealChoice(ChosenMealDish.DIET, MealType.DINNER),
        TimestampData(startTime = "2000", endTime = "2100", date = "21")
    )
}
