package com.plataforma.crpg.ui.meals

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
class MealDataViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repo = MealsRepository()

    fun testJSON() {
        val gson = Gson()
        val json = gson.toJson(repo.m)

        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val prettyJson: String = gsonPretty.toJson(json)

        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        file.writeText(prettyJson)
    }

    fun getMealsFromJSON() {
        val filename = "meals.json"
        val file = File(context.filesDir, filename)

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }

    fun convertMealsToJSON() {
        val gson = Gson()
        val json = gson.toJson(repo.m)
        val filename = "meals.json"
        val fileContents = "Hello world!"

        val file = File(context.filesDir, filename)

        val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
        fos.write(fileContents.toByteArray())

        context.openFileInput(filename).bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
    }
}
