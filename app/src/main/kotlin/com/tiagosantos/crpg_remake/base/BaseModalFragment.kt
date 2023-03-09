package com.tiagosantos.crpg_remake.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hannesdorfmann.fragmentargs.FragmentArgs
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.gotev.GotevViewModel
import com.tiagosantos.access.modal.settings.ActionType.REMINDER
import com.tiagosantos.access.modal.settings.ActionType.GENERAL_VIEW
import com.tiagosantos.access.modal.settings.ActionType.DATE_PICKER
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.generalHelper
import com.tiagosantos.common.ui.utils.VoiceCommandsProcessingHelper.numberList
import com.tiagosantos.crpg_remake.data.sharedprefs.SharedPrefsViewModel

abstract class BaseModalFragment<B : ViewDataBinding>(
    @LayoutRes
    layoutId: Int,
    settings: FragmentSettings,
) : BaseFragment<B>(
    layoutId = layoutId,
    settings = settings,
) {

    /**
     * Member has the same visibility as one marked as private, but that it is also visible in subclasses.
     * **/
    @Arg
    protected open lateinit var ttsSettings: TTSSettings

    @Arg
    protected open lateinit var srSettings: SRSettings

    private val prefHelper: SharedPrefsViewModel by activityViewModels()
    private val gossip: GossipViewModel by activityViewModels()
    private val gotev: GotevViewModel by activityViewModels()

    protected var actionList = mutableListOf<Any>()
    private var actionMap = mapOf<String,Any>()

    private val _flag = MutableLiveData<String?>()
    val flag: LiveData<String?> = _flag

    protected var literalValue: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FragmentArgs.inject(this)
        prefHelper.fetchModalityPreferences()
        val hasRun = prefHelper.fetchFlag(_flag)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        //gossip.setContextualHelp(ttsSettings.contextualHelp!!)
        //handleVoiceToActionController()
    }

    abstract fun setupUI()

    private fun listenToUser() = gotev.speechResult.observe(viewLifecycleOwner){
        performActionWithVoiceCommand(it, actionMap)
    }

    override fun onStop() {
        super.onStop()
        //gotev.stop()
        //gossip.shutUp()
    }

    override fun onPause() {
        super.onPause()
        //gotev.stop()
        //gossip.shutUp()
    }

    /**
    isinstance caters for inheritance (an instance of a derived class is an instance of a base class, too),
    while checking for equality of type does not
    (it demands identity of types and rejects instancees of subtypes, AKA subclasses).

    our A class is a subtype of X and Y, if we apply the is operator on the A instance
    and the two supertypes, we’ll get true as well
     **/
    private fun createActionMap() {
        actionMap = when(srSettings.actionType) {
            GENERAL_VIEW, REMINDER  -> {
                actionList.replaceAll { if (it is View) it.performClick() }
                srSettings.commandList.zip(actionList).toMap()
            }
            DATE_PICKER ->  srSettings.commandList.zip(numberList).toMap()
            else -> actionMap
        }
    }

    open fun handleVoiceToActionController() {
        when(srSettings.actionType) {
            GENERAL_VIEW, REMINDER -> { createActionMap(); listenToUser() }
            DATE_PICKER -> { listenToUser() }
            else -> {}
        }
    }

    open fun performActionWithVoiceCommand(
        command: String,
        actionMap: Map<String,Any>
    ) = generalHelper(command, actionMap)

}
