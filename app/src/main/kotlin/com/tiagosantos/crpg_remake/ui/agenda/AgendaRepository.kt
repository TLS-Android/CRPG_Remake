package com.tiagosantos.crpg_remake.ui.agenda

import com.google.gson.Gson
import com.tiagosantos.common.ui.model.Event
import com.tiagosantos.common.ui.model.EventType

object AgendaRepository {
    val gson = Gson()

    var lunchEvent = Event("Almoço", "Clicar para escolher refeição", EventType.MEAL, "1200", "1300",
        "","","", false, 0)
    var dinnerEvent = Event("Jantar", "Clicar para escolher refeição", EventType.MEAL, "2000", "2100",
        "","","", false, 0)

}
