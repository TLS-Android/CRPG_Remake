@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

package com.tiagosantos.crpg_remake.data.sharedprefs

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS
import java.util.*
import java.util.Calendar.*

class SharedPrefsViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    private val _ttsFlag = MutableLiveData<Boolean>()
    val ttsFlag: LiveData<Boolean> = _ttsFlag

    private val _srFlag = MutableLiveData<Boolean>()
    val srFlag: LiveData<Boolean> = _srFlag

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext
    val modalityPreferences = context?.getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
    val helper = SharedPrefsHelper(context!!)

    private val literalValue = listOf(
        "meditationHasRun", "selectionHasRun",
        "remindersHasRun", "agendaHasRun"
    )

    init {

    }



    fun resetAppPreferences() = modalityPreferences!!.apply {
        for (i in 1..literalValue.size) with(helper) { put(literalValue[i], false) }
    }

    fun fetchModalityPreferences() = with(helper) {
        modalityPreferences!!.apply {
            _ttsFlag.value = getBoolean(TTS)
            _srFlag.value = getBoolean(SR)
        }
    }

    fun fetchFlag(flag: MutableLiveData<String?>) = with(helper) {
        val hasRun = getBoolean(flag.toString())
    }

}
