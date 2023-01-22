package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.*
import java.util.*

@SuppressLint("StaticFieldLeak")
@Suppress("UNUSED_PARAMETER")
class MealsViewModel(
    //application: Application,
    //private val resourcesProvider: ResourcesProvider,
    private val ctx: Context,
) : ViewModel(), DefaultLifecycleObserver {

    private var repo = MealsMockData()

    private val _listMessages = MutableLiveData<List<String>?>()
    private val listMessages: LiveData<List<String>?> = _listMessages

    private val _selectedOption = MutableLiveData<Int?>()
    var selectedOption: LiveData<Int?> = _selectedOption


    companion object {
        private const val EVENT_FILENAME = "event.json"
    }

    private fun observeMessagesList() {
     /*   viewModelScope.launch {
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
        }*/
    }

    fun updateMealChoiceOnLocalStorage(selectedOption: LiveData<Int?>, lunch: Boolean) { }

    fun updateSelectedOption(i: Int) { _selectedOption.value = i }

}
