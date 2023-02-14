@file:Suppress("MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate")

package com.tiagosantos.crpg_remake.data.sharedprefs

/**
 * So when should you use static import? Very sparingly!
 * Only use it when you'd otherwise be tempted to declare local copies of constants,
 * or to abuse inheritance (the Constant Interface Antipattern). ...
 * If you overuse the static import feature, it can make your program unreadable and unmaintainable,
 * polluting its namespace with all the static members you import.
 * Readers of your code (including you, a few months after you wrote it)
 * will not know which class a static member comes from.
 * Importing all of the static members from a class can be particularly harmful to readability;
 * if you need only one or two members, import them individually.
 **/
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

    fun setTextToSpeechFlag(value: Boolean) { _ttsFlag.value = value }
    fun setSpeechRecognitionFlag(value: Boolean) { _srFlag.value = value }
}
