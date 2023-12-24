package com.tiagosantos.crpg_remake.ui.agenda

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.utils.Constants
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

class AppPreferencesViewModel(
    application: Application
) : AndroidViewModel(application) {

    var selectedDate = EMPTY_STRING

    private val modalityPreferences: SharedPreferences =
        application.getSharedPreferences(Constants.MODALITY, Context.MODE_PRIVATE)
    val ttsFlag = modalityPreferences.getBoolean(Constants.TTS, false)
    val srFlag = modalityPreferences.getBoolean(Constants.SR, false)
}
