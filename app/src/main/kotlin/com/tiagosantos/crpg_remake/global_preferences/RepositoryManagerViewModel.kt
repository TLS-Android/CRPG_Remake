package com.tiagosantos.crpg_remake.global_preferences

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.access.modal.modality.ModalityPreferencesRequestManager

@SuppressLint("StaticFieldLeak")
class RepositoryManagerViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    private val _ttsFlag = MutableLiveData<Boolean>()
    val ttsFlag get() = _ttsFlag

    lateinit var editor: SharedPreferences.Editor

    private var appPreferencesRepository = AppPreferencesRepository(context)
    private var modalityPreferencesRepository = ModalityPreferencesRequestManager(context, editor)

    /** ViewModel shouldn't hold any reference related to View (Activity, Context etc)
     * due to increased difficulty to test. **/
    override fun onCleared() {
        super.onCleared()
        _ttsFlag.value = false
    }

    fun initRepositories() {
        appPreferencesRepository.resetAppPreferences()
        modalityPreferencesRepository.requestMultiModalityOptions()
    }
}
