package com.tiagosantos.access.modal

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.settings.SRFragmentSettings
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

abstract class BaseTTSFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
    private val srSettings: SRFragmentSettings,
) : BaseModalFragment<B>(
    layoutId = layoutId,
    settings = settings,
    ttsSettings = TTSFragmentSettings(
        "Indique qual das opcoes pretende para o seu almoco",
        isMuted = false
    ),
    srSettings = SRFragmentSettings(
        isListening = true
    )
) {

    private val gossipVM: GossipViewModel by viewModels()
    abstract override fun onInitDataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gossipVM.setContextualHelp(ttsSettings.contextualHelp ?: EMPTY_STRING)
        gossipVM.talk()
    }

    override fun observeLifecycleEvents() {
        observe(gossipVM.contextualHelp, observer = {
            Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onStop() {
        super.onStop()
        gossipVM.gossip.stop()
    }
}
