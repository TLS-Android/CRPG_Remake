package com.tiagosantos.access.modal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.gotev.GotevViewModel
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.base.BaseFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.generalHelper

abstract class BaseModalFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSSettings,
    private val srSettings: SRSettings,
) : BaseFragment<B>(
    layoutId = layoutId,
    settings = settings,
) {
    val gossip: GossipViewModel by activityViewModels()
    val gotev: GotevViewModel by activityViewModels()

    private val _flag = MutableLiveData<String?>()
    private val flag: LiveData<String?> = _flag

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

        setupModality(ttsFlag, srFlag, hasRun)
    }

    open fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String,Any>
    ) = generalHelper(command, actionMap)


    open fun setupModality(
       ttsFlag: Boolean,
       srFlag: Boolean,
       hasRun: Boolean)
    {
        if (!hasRun) {
            when {
                ttsFlag && !srFlag -> gossip.talk()
                !ttsFlag && srFlag -> gotev.listen()
                ttsFlag && srFlag -> { gossip.talk(); gotev.listen() }
            }
        } else {
            when { !ttsFlag && srFlag || ttsFlag && srFlag -> gotev.listen() }
        }
    }
}
