package com.tiagosantos.crpg_remake.ui.home

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.scope.Scope

class HomeViewModel : ViewModel(), DefaultLifecycleObserver {

    override val scope: Scope by getOrCreateScope()

    private val coroutinesScope = CoroutineScope(Dispatchers.IO)

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}