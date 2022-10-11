package com.tiagosantos.access.modal.gotev

import android.app.Application
import android.os.Bundle
import android.speech.SpeechRecognizer.RESULTS_RECOGNITION
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.tiagosantos.common.ui.extension.observe
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable

class GotevViewModel(application: Application) : AndroidViewModel(application), Lifecyc {

    private var viewState: MutableLiveData<ViewState>? = null

    val isListening get() = viewState?.value?.isListening ?: false

    fun getViewState(): LiveData<ViewState> {
        if (viewState == null) {
            viewState = MutableLiveData()
            viewState?.value = initViewState()
        }
        return viewState as MutableLiveData<ViewState>
    }

    private fun notifyListening(isRecording: Boolean) {
        viewState?.value = viewState?.value?.copy(isListening = isRecording)
    }

    override fun observeLifecycleEvents() {
        observe(viewModel.errorMessage, observer = {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun updateResults(speechBundle: Bundle?) {
        val userSaid = speechBundle?.getStringArrayList(RESULTS_RECOGNITION)
        viewState?.value = viewState?.value?.copy(spokenText = userSaid?.get(0) ?: "")
    }

    private fun initViewState() = ViewState(spokenText = "", isListening = false, error = null)

    init {
        Speech.init(
            application.applicationContext,
            application.packageName
        )
    }

    data class ViewState(
        var spokenText: String?,
        val isListening: Boolean?,
        val error: String?
    )

    fun listen() {
        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i(
                        "speech",
                        "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d(
                        "speech",
                        "rms is now: $value")
                }

                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) {
                        str.append(res).append(" ")
                    }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    Log.i("speech", "result: $result")
                    viewState?.value?.spokenText = result
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
        Speech.getInstance().shutdown();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartView() {
        //do something on start view if it's needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopView() {

        println("hello world!")
        //do something on stop view if it's needed
    }

}


