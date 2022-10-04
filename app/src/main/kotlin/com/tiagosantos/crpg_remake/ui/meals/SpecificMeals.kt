package com.tiagosantos.crpg_remake.ui.meals

import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast

class SpecificMeals {}
/*
    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {
        println("ttsFlag:  $ttsFlag, srFlag: $srFlag, hasRun: $hasRun")

        if (!hasRun) {
            when {
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag -> { multimodalOption() }
            }
        }

        if (hasRun) {
            when {
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag -> { startVoiceRecognition() }
            }
        }
    }

    override fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak(
                    "Diga Carne, Peixe, Dieta ou Vegetariano " +
                            "para selecionar o seu prato e depois diga confirmar para selecionar a sua opção",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun multimodalOption() {
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

                    override fun onError(p0: String?) { TODO("Not yet implemented") }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak(
                    "Diga Carne, Peixe," +
                            " Dieta ou Vegetariano para selecionar o seu prato e depois diga confirmar " +
                            "para selecionar a sua opção",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun startVoiceRecognition() {
        // MANTER WIFI SEMPRE LIGADO
        if (isAdded && isVisible && userVisibleHint) {
            runnable = Runnable {
                handler.sendEmptyMessage(0)
                Speech.init(requireActivity())
                // hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "meal speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {
                            Log.d("speech", "rms is now: $value")
                        }

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString())
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(TimelineView.TAG, "onSpeechResult: " + result.lowercase())
                            // Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible && userVisibleHint) {
                                            Speech.init(requireActivity())
                                            Speech.getInstance().startListening(this)
                                        }
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)
                            }
                        }
                    })
                } catch (exc: SpeechRecognitionNotAvailable) {
                    Log.e("speech", "Speech recognition is not available on this device!")
                } catch (exc: GoogleVoiceTypingDisabledException) {
                    Log.e("speech", "Google voice typing must be enabled!")
                }
            }

            handler.post(runnable)
        }
    }



 */