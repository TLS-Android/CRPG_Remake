package com.tiagosantos.access.modal.gotev

import android.app.Application
import android.content.res.loader.ResourcesProvider
import android.util.Log
import androidx.lifecycle.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable

class GotevViewModel(
    private val resourcesProvider: ResourcesProvider,
    application: Application,
) : AndroidViewModel(application) {

    init {
        Speech.init(
            application.applicationContext,
            application.packageName
        )
    }

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


