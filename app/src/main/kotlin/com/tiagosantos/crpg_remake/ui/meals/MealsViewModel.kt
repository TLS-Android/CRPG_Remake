package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import androidx.lifecycle.*
import java.util.*

@SuppressLint("StaticFieldLeak")
@Suppress("UNUSED_PARAMETER")
class MealsViewModel : ViewModel(), DefaultLifecycleObserver {

    private var repo = MealsMockData()

    private val _selectedOption = MutableLiveData<Int?>()
    val selectedOption: LiveData<Int?> = _selectedOption

    companion object {
        private const val EVENT_FILENAME = "meal.json"
    }

    fun updateMealChoiceOnLocalStorage(selectedOption: LiveData<Int?>, lunch: Boolean) { }

    fun updateSelectedOption(i: Int) { _selectedOption.value = i }

}
