package com.tiagosantos.access.modal.gossip

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.loader.ResourcesProvider
import androidx.lifecycle.*
import com.tiagosantos.common.ui.utils.Constants
import java.lang.ref.WeakReference

class GossipViewModel(
    //private val resourcesProvider: ResourcesProvider,
    //context: WeakReference<Context>,
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    private val _contextualHelp = MutableLiveData<String>()
    val contextualHelp: LiveData<String> = _contextualHelp

    @SuppressLint("StaticFieldLeak")
    val context = application.applicationContext

    var gossip: Gossip = Gossip(context)

    init {
        gossip.run { setLanguage(Constants.myLocale); isMuted }
        talk()
    }

    fun talk() { if (gossip.isMuted) gossip.talk(contextualHelp.toString()) }
    fun shutUp() { if (!gossip.isMuted) gossip.stop() }

    fun setContextualHelp(help: String) { _contextualHelp.value = help }

    override fun onCleared() {
        super.onCleared()
        gossip.run { stop(); shutdown() }
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
