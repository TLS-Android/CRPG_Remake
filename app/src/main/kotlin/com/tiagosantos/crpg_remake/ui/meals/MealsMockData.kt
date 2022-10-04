package com.tiagosantos.crpg_remake.ui.meals

//import com.google.firebase.database.DatabaseReference
import com.plataforma.crpg.model.Meal

class MealsMockData {

    //private lateinit var database: DatabaseReference
    val meal = Meal(
        "01042021", "bife", "atum", "massa", "salada"
    )

    val m = Meal(
        "01042021", "a",
        "b", "c", "d"
    )

    var retrievedMeal = Meal(
        "27052021", "Lasanha",
        "Sardinhas", "Massa", "Tofu"
    )

    val mealFilename = "meals.json"
    val fileContents = "Hello world!"
}
