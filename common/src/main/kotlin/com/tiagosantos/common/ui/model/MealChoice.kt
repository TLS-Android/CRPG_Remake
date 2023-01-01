package com.tiagosantos.common.ui.model

data class MealChoice(
    var chosen_meal: String? = "",
    var isLunch: Boolean? = true,
    var meal_int: Int? = 1
) {
    override fun toString(): String {
        return "chosen_meal: ${this.chosen_meal}, isLunch: ${this.isLunch}," +
                "meal_Int: ${this.meal_int} \\n "
    }
}
