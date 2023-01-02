package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName

data class MealChoice(
    var chosenMealDish: ChosenMealType? = ChosenMealType.MEAT,
    var mealType: MealType? = MealType.LUNCH,
) {
    override fun toString(): String {
        return "chosenMealType: ${this.chosenMealDish}, mealType: ${this.mealType} \\n "
    }
}

enum class MealType {
    @SerializedName("Lunch")
    LUNCH,
    @SerializedName("Dinner")
    DINNER,
}

enum class ChosenMealType {
    @SerializedName("Meat")
    MEAT,
    @SerializedName("Fish")
    FISH,
    @SerializedName("Veg")
    VEG,
    @SerializedName("Diet")
    DIET,
}

