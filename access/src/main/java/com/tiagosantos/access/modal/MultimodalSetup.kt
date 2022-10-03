package com.tiagosantos.access.modal

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import com.tiagosantos.common.ui.utils.Constants.myLocale

class MultimodalSetup(
    context: Context,
    textToSpeech: TextToSpeech
) {

    open fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                var speechStatus = textToSpeech!!.speak(
                    "Diga a paragem que pretende para selecionar",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    open fun multimodalOption() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if (activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) {
                        TODO("Not yet implemented")
                    }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                var speechStatus = textToSpeech!!.speak(
                    "Diga a paragem que pretende para selecionar",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
}