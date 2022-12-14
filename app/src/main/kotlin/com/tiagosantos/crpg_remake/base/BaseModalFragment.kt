package com.tiagosantos.crpg_remake.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.gotev.GotevViewModel
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.generalHelper

abstract class BaseModalFragment<B : ViewDataBinding>(
    @LayoutRes
    layoutId: Int,
    settings: FragmentSettings,
    /**
     * Member has the same visibility as one marked as private, but that it is also visible in subclasses.
     * **/
    protected val ttsSettings: TTSSettings,
    protected val srSettings: SRSettings,
) : BaseFragment<B>(
    layoutId = layoutId,
    settings = settings,
) {
    open val gossip: GossipViewModel by activityViewModels()
    private val gotev: GotevViewModel by activityViewModels()

    private val _flag = MutableLiveData<String?>()
    private val flag: LiveData<String?> = _flag

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    //abstract override fun onInitDataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gossip.setContextualHelp(ttsSettings.contextualHelp!!)
        fetchPreferences()
        listenToUser()
    }

    private fun fetchPreferences() {
        val modalityPreferences = this.requireActivity().getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean(TTS, false)
        val srFlag = modalityPreferences.getBoolean(SR, false)
        val hasRun = modalityPreferences.getBoolean(flag.toString(), false)

        setupModality(ttsFlag, srFlag, hasRun)
    }

    private fun listenToUser() =  gotev.speechResult.observe(viewLifecycleOwner){
        performActionWithVoiceCommand(it, srSettings.actionMap!!)
    }

    override fun onStop() {
        super.onStop()
        gotev.stop()
        gossip.shutUp()
    }

    override fun onPause() {
        super.onPause()
        gotev.stop()
        gossip.shutUp()
    }

    open fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String,Any>
    ) = generalHelper(command, actionMap)

    /**
     * SHOULD BE MOVED TO A VIEW MODEL; NOT PART OF THE FRAGMENT
     */
    open fun setupModality(
       ttsFlag: Boolean,
       srFlag: Boolean,
       hasRun: Boolean
    ){
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
