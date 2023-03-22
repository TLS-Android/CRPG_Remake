package com.tiagosantos.access.modal.gossip

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.tiagosantos.common.ui.utils.Constants

class GossipViewModel(
    application: Application,
) : AndroidViewModel(application), DefaultLifecycleObserver {

    private val _contextualHelp = MutableLiveData<String>()
    val contextualHelp: LiveData<String> = _contextualHelp

    @SuppressLint("StaticFieldLeak")
    val context: Context? = application.applicationContext

    private var gossip: Gossip = Gossip(context!!)

    init {
        gossip.run { setLanguage(Constants.myLocale); isMuted }
    }

    fun talk() { if (gossip.isMuted) gossip.talk(contextualHelp.toString()) }
    fun shutUp() { if (!gossip.isMuted) gossip.stop() }

    fun setContextualHelp(help: String) { _contextualHelp.value = help; talk() }

    override fun onCleared() {
        super.onCleared()
        gossip.run { stop(); shutdown() }
    }

    fun onStartView(owner: LifecycleOwner) {
        super.onCreate(owner)

        //do something on start view if it's needed
    }

    fun onStopView(owner: LifecycleOwner) {
        super.onCreate(owner)
        //do something on stop view if it's needed
    }
}
