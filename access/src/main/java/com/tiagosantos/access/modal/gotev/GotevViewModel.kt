package com.tiagosantos.access.modal.gotev

import android.app.Application
import android.os.Bundle
import android.speech.SpeechRecognizer.RESULTS_RECOGNITION
import android.util.Log
import androidx.lifecycle.*
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable

class GotevViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    private var viewState: MutableLiveData<ViewState>? = null
    private val isListening get() = viewState?.value?.isListening ?: false

    private val _speechResult = MutableLiveData<String>()
    val speechResult: LiveData<String> = _speechResult

    fun getViewState(): LiveData<ViewState> {
        if (viewState == null) {
            viewState = MutableLiveData()
            viewState?.value = initViewState()
        }
        return viewState as MutableLiveData<ViewState>
    }

    init { Speech.init(application.applicationContext, application.packageName) }

    private fun notifyListening(
        isRecording: Boolean
    ) { viewState?.value = viewState?.value?.copy(isListening = isRecording) }

    private fun updateResults(speechBundle: Bundle?) {
        val userSaid = speechBundle?.getStringArrayList(RESULTS_RECOGNITION)
        viewState?.value =
            viewState?.value?.copy(spokenText = userSaid?.get(0) ?: EMPTY_STRING)
    }

    private fun initViewState() = ViewState(
        spokenText = EMPTY_STRING,
        isListening = false,
        error = null
    )

    fun listen() {
        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    notifyListening(isRecording = true)
                    Log.i("speech", "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) { Log.d("speech", "rms is now: $value") }

                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) { str.append(res).append(" ") }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' }) }

                override fun onSpeechResult(result: String) {
                    Log.i("speech", "result: $result")
                    viewState?.value?.spokenText = result
                    notifyListening(isRecording = false)
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Speech.getInstance().shutdown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartView() { //do something on start view if it's needed }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopView() {
        //do something on stop view if it's needed
    }

    data class ViewState(
        var spokenText: String?,
        val isListening: Boolean?,
        val error: String?
    )
}
