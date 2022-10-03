package com.tiagosantos.access.modal

import android.content.Context
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tiagosantos.common.ui.utils.Constants.myLocale

open class MultimodalSetup(
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

    open fun startVoiceRecognition() {
            // KEEP WIFI ALWAYS ON
            if (isAdded && isVisible) {
                runnable = Runnable {
                    handler.sendEmptyMessage(0)
                    Speech.init(requireActivity())
                    try {
                        Speech.getInstance().startListening(object : SpeechDelegate {
                            override fun onStartOfSpeech() {
                                Log.i(
                                    "speech", "transports speech" +
                                            " recognition is now active"
                                )
                            }

                            override fun onSpeechRmsChanged(value: Float) {}

                            override fun onSpeechPartialResults(results: List<String>) {
                                val str = StringBuilder()
                                for (res in results) {
                                    str.append(res).append(" ")
                                }
                                performActionWithVoiceCommand(results.toString())
                                Log.i(
                                    "speech",
                                    "partial result: " + str.toString().trim {
                                        it <= ' '
                                    }
                                )
                            }

                            override fun onSpeechResult(result: String) {
                                val handler = Handler()
                                if (activity != null && isAdded) {
                                    handler.postDelayed({
                                        try {
                                            if (isAdded && isVisible) {

                                                Speech.init(requireActivity())
                                                Speech.getInstance().startListening(this)
                                            }
                                        } catch (
                                            speechRecognitionNotAvailable: SpeechRecognitionNotAvailable
                                        ) {
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

        fun manageBackButton(fragment: Fragment) {
            // Handle the back button event
            val onBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                    fragmentManager.popBackStack()
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                onBackPressedCallback
            )
        }

    }

}