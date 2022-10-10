package com.tiagosantos.common.ui.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private var disposable: CompositeDisposable = CompositeDisposable()

    val subscriptions: CompositeDisposable
        get() {
            if (disposable.isDisposed) disposable = CompositeDisposable()
            return disposable
        }

    fun clearSubscriptions() {
        disposable.dispose()
    }

    override fun onCleared() {
        disposable.dispose()
    }


}