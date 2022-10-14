package com.tiagosantos.access.modal

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.access.R
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS

abstract class BaseModalFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
) : BaseFragment<B>(
    layoutId = layoutId,
    settings = settings,
) {

    private val _flag = MutableLiveData<String?>()
    val flag: LiveData<String?> = _flag

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
        val ttsFlag = modalityPreferences.getBoolean(TTS, false)
        val srFlag = modalityPreferences.getBoolean(SR, false)
        val hasRun = modalityPreferences.getBoolean(flag.toString(), false)

        defineModality(ttsFlag, srFlag, hasRun)
    }

    open fun performActionWithVoiceCommand(command: String) {}

    private fun defineModality(
       ttsFlag: Boolean,
       srFlag: Boolean,
       hasRun: Boolean)
    { if (!hasRun) {
            when {
                ttsFlag && !srFlag -> println("ola")
                !ttsFlag && srFlag -> println("ola")
                ttsFlag && srFlag -> println("ola")
            }
        } else {
            when {
                !ttsFlag && srFlag -> println("ola")
                ttsFlag && srFlag -> println("ola")
            }
        }
    }

    override fun observeLifecycleEvents() {
        observe(viewModel.errorMessage, observer = {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
    }


}
