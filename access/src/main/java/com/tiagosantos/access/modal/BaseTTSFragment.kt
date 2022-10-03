package com.tiagosantos.access.modal

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.SR_FLAG
import com.tiagosantos.common.ui.utils.Constants.TTS_FLAG
import kotlin.properties.Delegates

abstract class BaseTTSFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
) : BaseFragment<B>(
    layoutId = layoutId,
    settings = settings,
) {

    private var handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()
    private var textToSpeech: TextToSpeech? = null

    override lateinit var viewBinding: B

    private val _flag = MutableLiveData<String?>()
    val flag: LiveData<String?> = _flag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    abstract override fun onInitDataBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modalityPreferences =
            this.requireActivity().getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean(TTS_FLAG, false)
        val srFlag = modalityPreferences.getBoolean(SR_FLAG, false)
        val hasRun = modalityPreferences.getBoolean(flag, false)

        defineModality(ttsFlag, srFlag, hasRun)


    }

    open fun performActionWithVoiceCommand(command: String) {}

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {
        if (!hasRun) {
            when {
                ttsFlag && !srFlag -> {
                    startTTS()
                }
                !ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
                ttsFlag && srFlag -> {
                    multimodalOption()
                }
            }
        }

        if (hasRun) {
            when {
                !ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
                ttsFlag && srFlag -> {
                    startVoiceRecognition()
                }
            }
        }
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

}
