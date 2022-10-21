package com.tiagosantos.access.modal.gossip

import android.content.Context
import android.content.res.loader.ResourcesProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.tiagosantos.common.ui.base.BaseViewModel
import com.tiagosantos.common.ui.utils.Constants
import java.lang.ref.WeakReference

class GossipViewModel(
    private val resourcesProvider: ResourcesProvider,
    private val context: WeakReference<Context>
) : BaseViewModel() {

    private val _contextualHelp = MutableLiveData<String>()
    val contextualHelp: LiveData<String> = _contextualHelp

    var gossip: Gossip = Gossip(context.get()!!)

    init {
        gossip.setLanguage(Constants.myLocale)
        talk()
        gossip.isMuted
    }

    fun talk() { if (gossip.isMuted) gossip.talk(contextualHelp.toString()) }

    fun setContextualHelp(help: String) {
        _contextualHelp.value = help
        talk()
    }

    override fun onCleared() {
        super.onCleared()
        gossip.stop()
        gossip.shutdown()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStartView() {
        //do something on start view if it's needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStopView() {
        //do something on stop view if it's needed
    }
}


