package com.tiagosantos.crpg_remake.database

class MealsRepository {
/*
    fun convertMealsToJSON() {
        MealsViewModel.gson.toJson(repo.m)
        val file = File(context.filesDir, repo.mealFilename)

        val fos: FileOutputStream = context.openFileOutput(repo.mealFilename, Context.MODE_PRIVATE)
        fos.write(repo.fileContents.toByteArray())

        context.openFileInput(repo.mealFilename).bufferedReader().useLines { lines ->
            lines.fold(Constants.EMPTY_STRING) { some, text ->
                "$some\n$text"
            }
        }
    }

    fun fetchMealChoiceOnLocalStorage(): String {
        val isLunch = CustomDateUtils.getIsLunchOrDinner()
        val currentDate = CustomDateUtils.getCurrentDay()

        val dish: String = when (verifyMealChoiceOnLocalStorage(isLunch)) {
            1 -> repo.mockMeal.carne
            2 -> repo.mockMeal.peixe
            3 -> repo.mockMeal.dieta
            4 -> repo.mockMeal.vegetariano
            else -> Constants.EMPTY_STRING
        }

        return dish
    }

    fun updateMealChoiceOnLocalStorage(selectedOption: Int, isLunch: Boolean) {
        val fullFilename = "$FILES_DIR/${MealsViewModel.EVENT_FILENAME}}"

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = MealsViewModel.gson.fromJson(FileReader(fullFilename), type)

        when (isLunch) {
            true -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "ALMOÇO"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = true

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = repo.mockMeal.carne
                    2 -> eventsList[idx].chosen_meal = repo.mockMeal.peixe
                    3 -> eventsList[idx].chosen_meal = repo.mockMeal.dieta
                    4 -> eventsList[idx].chosen_meal = repo.mockMeal.vegetariano
                }
            }

            false -> {
                val idx = eventsList.indexOfFirst {
                    it.title == "JANTAR"
                }

                eventsList[idx].meal_int = selectedOption
                eventsList[idx].isLunch = false

                when (selectedOption) {
                    1 -> eventsList[idx].chosen_meal = repo.mockMeal.carne
                    2 -> eventsList[idx].chosen_meal = repo.mockMeal.peixe
                    3 -> eventsList[idx].chosen_meal = repo.mockMeal.dieta
                    4 -> eventsList[idx].chosen_meal = repo.mockMeal.vegetariano
                }
            }
        }

        val newMealJSON = MealsViewModel.gson.toJson(eventsList)
        File(fullFilename).writeText(newMealJSON)
        fetchMealChoiceOnLocalStorage()
    }

    private fun verifyMealChoiceOnLocalStorage(isLunch: Boolean): Int {
        val fullFilename = "$FILES_DIR/${MealsViewModel.EVENT_FILENAME}}"

        val type: Type = object : TypeToken<ArrayList<Event>>() {}.type
        val eventsList: ArrayList<Event> = MealsViewModel.gson.fromJson(FileReader(fullFilename), type)

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
        val json = MealsViewModel.gson.toJson(repo.m)
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

    */

}