package com.tiagosantos.common.ui.model

class MessagesViewModel {
/*
    private val _listMessages = MutableLiveData<List<String>?>()
    private val listMessages: LiveData<List<String>?> = _listMessages

    private fun observeMessagesList() {
        viewModelScope.launch {
            interactor.getMessagesList()
                .collect {
                    it.onSuccess { messagesList ->
                        Log.d(LOG_TAG_DEBUG, "observeMessagesList: onSuccess: $messagesList ")
                    }.onFailure {
                        Log.e(
                            LOG_TAG_DEBUG,
                            "observeMessagesList: onFailure: ${it.localizedMessage} "
                        )
                        Log.e(
                            LOG_TAG_DEBUG,
                            "observeMessagesList: onFailure: Thread ${Thread.currentThread().name} "
                        )
                    }

                    fun isLoadingAnimationVisible(): LiveData<Boolean> {
                        val isVisible = MediatorLiveData<Boolean>()

                        val b: () -> Boolean = {
                            listMessages.value?.isEmpty() ?: true
                        }

                        isVisible.value = b()

                        isVisible.addSource(listMessages) {
                            isVisible.value = b()
                        }

                        return isVisible
                    }
                }
        }
    }

 */
}