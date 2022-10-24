package com.tiagosantos.common.ui.preferences

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.common.ui.utils.Constants

class ModalSharedPreferences (
    application: Application
) {

    private val _flag = MutableLiveData<String?>()
    private val flag: LiveData<String?> = _flag

    fun ol(){
        val modalityPreferences =
            this.requireActivity().getSharedPreferences(Constants.MODALITY, Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean(Constants.TTS, false)
        val srFlag = modalityPreferences.getBoolean(Constants.SR, false)
        val hasRun = modalityPreferences.getBoolean(flag.toString(), false)
    }
}