package com.tiagosantos.crpg_remake.global_preferences

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import modality.ModalityPreferencesRepository

@SuppressLint("StaticFieldLeak")
class RepositoryManagerViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var _ttsFlag = MutableLiveData<Boolean>()
    private val ttsFlag
        get() = _ttsFlag

    private var appPreferencesRepository = AppPreferencesRepository(context)
    private var modalityPreferencesRepository = ModalityPreferencesRepository(context)

    fun initRepositories() {
        appPreferencesRepository.resetAppPreferences()
        modalityPreferencesRepository.requestMultiModalityOptions()

        ttsFlag.observe(this, Observer {
            if (it) println("true") else println("false")
        })
    }
}
