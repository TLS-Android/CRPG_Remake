package com.tiagosantos.crpg_remake.ui.agenda

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

class SharedViewModel(
    application: Application
) : AndroidViewModel(application) {

    var selectedDate = EMPTY_STRING

    val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", 
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", 
            |"start_time": "2000","end_time": "2100","date": "2021-03-17"},
            |{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", 
            |"start_time": "0830","end_time": "1330","date": "2021-03-17"},
            |{"title": "Actividade","info":"Sala 12","type": "ACTIVITY",
            | "start_time": "0830","end_time": "1330","date": "2021-03-17"}]""".trimMargin()
}
