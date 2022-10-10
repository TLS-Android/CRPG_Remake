package com.tiagosantos.access.modal.gossip

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.utils.Constants
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import java.util.*

abstract class BaseGossipFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
) : BaseModalFragment<B>(
    layoutId = layoutId,
    settings = settings,
    ttsSettings = TTSFragmentSettings(
        EMPTY_STRING
    )
) {

    lateinit var gossip: Gossip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    abstract override fun onInitDataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gossip = Gossip(requireActivity().applicationContext)
        gossip.setLanguage(Constants.myLocale)
        talk()
    }

    open fun talk() = gossip.talk(ttsSettings.contextualHelp.toString())

    override fun onStop() {
        super.onStop()
        gossip.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        gossip.shutdown()
    }

    override fun performActionWithVoiceCommand(command: String) {}


}
