package com.tiagosantos.access.modal

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.viewModels
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

abstract class BaseTTSFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int,
    private val settings: FragmentSettings,
    private val ttsSettings: TTSFragmentSettings,
) : BaseModalFragment<B>(
    layoutId = layoutId,
    settings = settings,
    ttsSettings = TTSFragmentSettings(
        "Indique qual das opcoes pretende para o seu almoco"
    )
) {

    private val gossipVM: GossipViewModel by viewModels()

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

    override fun onInitDataBinding() {
        //viewBinding.model = viewModel
        lifecycle.addObserver(gossipVM)
    }

    override fun onStop() {
        super.onStop()
        gossipVM.gossip.stop()
    }

    override fun performActionWithVoiceCommand(command: String) {}

}
