package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.plataforma.crpg.model.Event
import com.plataforma.crpg.utils.Constants.EMPTY_STRING
import com.plataforma.crpg.utils.CustomDateUtils
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.lang.reflect.Type
import java.util.*

@SuppressLint("StaticFieldLeak")
class MealsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var repo = MealsRepository()
    private val context = getApplication<Application>().applicationContext
    var selectedOption = 0

    private val FILES_DIR = context?.filesDir.toString()

    companion object {
        private const val EVENT_FILENAME = "event.json"
        val gson = Gson()
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

    fun fetchMealChoiceOnLocalStorage(): String {
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
}
