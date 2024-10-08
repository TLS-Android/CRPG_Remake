package com.tiagosantos.access.modal

import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe

abstract class BaseSRFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSSettings,
    private val srSettings: SRSettings,
) : BaseModalFragment<B>(
    layoutId = layoutId,
    settings = settings,
    ttsSettings = TTSSettings(
        "hey",
        isMuted = false,
    ),
    srSettings = SRSettings(
        isListening = true
    )
) {

    abstract override fun onInitDataBinding()

    override fun onStop() {
        val activity = requireActivity()
        super.onStop()
    }

    override fun observeLifecycleEvents() {
        observe(viewModel.errorMessage, observer = {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
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

}
