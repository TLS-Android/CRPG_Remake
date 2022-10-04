package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tiagosantos.common.ui.utils.GeneralUtils.LOG_TAG_DEBUG
import com.tiagosantos.crpg_remake.helper.ResourcesProvider
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

@SuppressLint("StaticFieldLeak")
class MealsViewModel(
    application: Application,
    private val resourcesProvider: ResourcesProvider,
) : ViewModel(), DefaultLifecycleObserver {

    private var repo = MealsMockData()
    private val context = application.applicationContext

    private val _listMessages = MutableLiveData<List<String>?>()
    val listMessages: LiveData<List<String>?> = _listMessages

    private val _selectedOption = MutableLiveData<Int?>()
    var selectedOption: LiveData<Int?> = _selectedOption

    private val FILES_DIR = context?.filesDir.toString()

    companion object {
        private const val EVENT_FILENAME = "event.json"
        val gson = Gson()
    }

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


    fun convertMealsToJSON() {
        gson.toJson(repo.m)
        val file = File(context.filesDir, repo.mealFilename)

        val fos: FileOutputStream = context.openFileOutput(repo.mealFilename, Context.MODE_PRIVATE)
        fos.write(repo.fileContents.toByteArray())

        context.openFileInput(repo.mealFilename).bufferedReader().useLines { lines ->
            lines.fold(EMPTY_STRING) { some, text ->
                "$some\n$text"
            }
        }
    }

    private fun fetchMealChoiceOnLocalStorage(): String {
        val isLunch = CustomDateUtils.getIsLunchOrDinner()
        val currentDate = CustomDateUtils.getCurrentDay()

        val dish: String = when (verifyMealChoiceOnLocalStorage(isLunch)) {
            1 -> repo.retrievedMeal.carne
            2 -> repo.retrievedMeal.peixe
            3 -> repo.retrievedMeal.dieta
            4 -> repo.retrievedMeal.vegetariano
            else -> EMPTY_STRING
        }

        return dish
    }

    fun updateMealChoiceOnLocalStorage(selectedOption: Int, isLunch: Boolean) {
        val fullFilename = "$FILES_DIR/$EVENT_FILENAME}"

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        when (isLunch) {
            true -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "ALMOÇO"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = true

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = repo.retrievedMeal.carne
                    2 -> eventsList[idx].chosen_meal = repo.retrievedMeal.peixe
                    3 -> eventsList[idx].chosen_meal = repo.retrievedMeal.dieta
                    4 -> eventsList[idx].chosen_meal = repo.retrievedMeal.vegetariano
                }
            }

            false -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "JANTAR"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = false

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = repo.retrievedMeal.carne
                    2 -> eventsList[idx].chosen_meal = repo.retrievedMeal.peixe
                    3 -> eventsList[idx].chosen_meal = repo.retrievedMeal.dieta
                    4 -> eventsList[idx].chosen_meal = repo.retrievedMeal.vegetariano
                }
            }
        }

        val newMealJSON = gson.toJson(eventsList)
        File(fullFilename).writeText(newMealJSON)
        fetchMealChoiceOnLocalStorage()
    }

    private fun verifyMealChoiceOnLocalStorage(isLunch: Boolean): Int {
        val fullFilename = "$FILES_DIR/$EVENT_FILENAME}"

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = gson.fromJson(FileReader(fullFilename), type)

        when (isLunch) {
            true -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "ALMOÇO"
                }
                return if (eventsList[idx].meal_int < 1 || eventsList[idx].meal_int > 4) 0
                else eventsList[idx].meal_int
            }

            false -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "JANTAR"
                }

                return if (eventsList[idx].meal_int < 1 || eventsList[idx].meal_int > 4) 0
                else eventsList[idx].meal_int
            }
        }
    }

    fun testJSON() {
        val json = gson.toJson(repo.m)
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val prettyJson: String = gsonPretty.toJson(json)

        val file = File(context.filesDir, repo.mealFilename)
        file.writeText(prettyJson)
    }

    fun getMealsFromJSON() {
        val file = File(context.filesDir, repo.mealFilename)

        context.openFileInput(repo.mealFilename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }

}
