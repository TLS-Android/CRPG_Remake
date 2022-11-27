package com.tiagosantos.common.ui.utils

import java.util.*

object Constants {
    const val LOG_TAG_DEBUG = "app.debug"
    const val LOG_TAG_ERROR = "app.error"

    // MY LOCALE
    const val EMPTY_STRING = ""
    private const val LANG_PORTUGUESE = "pt_PT"
    private const val REGION_PORTUGAL = "POR"

    val myLocale = Locale(LANG_PORTUGUESE, REGION_PORTUGAL)

    // SHARED PREFERENCES
    const val MODALITY = "MODALITY"
    const val TTS = "TTS"
    const val SR = "SR"

    // AUDIO EXTENSIONS
    const val WAV_EXTENSION = ".wav"

    //NUMBERS
    const val ONE = 1

    //PERMISSIONS
    const val PERMITIR = "PERMITIR"
    const val RECUSAR = "RECUSAR"

    //EVENT FILES
    const val EVENT_FILENAME = "event.json"
    const val REMINDER_FILENAME = "reminder.json"

    //TAGS
    private const val TAG_AGENDA = "AgendaRepository"

    //BOOL
    const val t = true
    const val f = false

    //TIMELINE
    const val chosenMealisBlankText = "Nenhuma refeição selecionada, clique para selecionar a sua refeição"
    const val selectLunchText = "CLIQUE AQUI PARA SELECIONAR ALMOÇO"
    const val selectDinnerText = "CLIQUE AQUI PARA SELECIONAR JANTAR"

    //AGENDA VIEWMODEL
    val fileContent = """[{"title": "ALMOÇO","info":"test","type": "MEAL", 
            |"start_time": "1130","end_time": "1230","date": "2021-03-17"},
            |{"title": "JANTAR","info":"test","type":"MEAL", "start_time": 
            |"2000","end_time": "2100","date": "2021-03-17"},
            |{"title": "TRANSPORTE","info":"test","type": "TRANSPORT", 
            |"start_time": "0830","end_time": "1330","date": "2021-03-17"},
            |{"title": "Actividade","info":"Sala 12","type": "ACTIVITY", 
            |"start_time": "0830","end_time": "1330","date": "2021-03-17"}]""".trimMargin()

    const val eventFilename = "event.json"

}
