@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

package com.tiagosantos.crpg_remake.data.sharedprefs

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.tiagosantos.common.ui.utils.Constants
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS
import java.util.*
import java.util.Calendar.*


class SharedPrefsViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext
    val modalityPreferences = context?.getSharedPreferences(Constants.MODALITY, Context.MODE_PRIVATE)
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

    fun fetchModalityPreferences(flag: MutableLiveData<String?>) = with(helper) {
        modalityPreferences!!.apply {
            val ttsFlag = getBoolean(TTS)
            val srFlag = getBoolean(SR)
            val hasRun = getBoolean(flag.toString())
        }
    }

}
