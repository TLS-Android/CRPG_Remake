package com.tiagosantos.access.modal

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import kotlin.properties.Delegates

abstract class BaseTTSFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
) : BaseMultimodalFragment<B>(
    layoutId = layoutId,
    settings = settings,
    ttsSettings = TTSFragmentSettings(
        "hey"
    )
) {

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract override fun onInitDataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        val activity = requireActivity()
        super.onStop()
    }

    override fun observeLifecycleEvents() {
        observe(viewModel.errorMessage, observer = {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun performActionWithVoiceCommand(command: String) {}

}
