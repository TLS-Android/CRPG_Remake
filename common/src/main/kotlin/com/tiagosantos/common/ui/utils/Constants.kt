package com.tiagosantos.common.ui.utils

import java.util.*

object Constants {

    const val LOG_TAG_DEBUG = "app.debug"
    const val LOG_TAG_ERROR = "app.error"

    // MY LOCALE
    const val EMPTY_STRING = ""
    const val LANG_PORTUGUESE = "pt_PT"
    const val REGION_PORTUGAL = "POR"

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
    const val FILES_DIR = context?.filesDir.toString()
    const val EVENT_FILENAME = "event.json"
}




