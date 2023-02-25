package com.tiagosantos.common.ui.model

import com.google.gson.annotations.SerializedName

data class MealEvent(
    override val title: String?,
    override val info: String?,
    override val type: EventType = EventType.MEAL,
    override val timestampData: TimestampData?,
    var chosenMealDish: ChosenMealDish? = ChosenMealDish.MEAT,
    var mealType: MealType? = MealType.LUNCH,
): Event() {
    override fun toString(): String {
        return "chosenMealType: ${this.chosenMealDish}, mealType: ${this.mealType}"
    }
}

enum class MealType {
    @SerializedName("Lunch")
    LUNCH,
    @SerializedName("Dinner")
    DINNER,
}

enum class ChosenMealDish {
    @SerializedName("Meat")
    MEAT,
    @SerializedName("Fish")
    FISH,
    @SerializedName("Veg")
    VEG,
    @SerializedName("Diet")
    DIET,
}

