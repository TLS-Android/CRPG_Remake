package com.tiagosantos.common.ui.model

data class Meal(
    val data: String? = "data",
    val carne: String? = "carne",
    val peixe: String? = "peixe",
    val dieta: String? = "dieta",
    val vegetariano: String? = "vegetariano"
) {
    override fun toString(): String {
        return "data: ${this.data},carne: ${this.carne}, peixe: ${this.peixe}," +
                " dieta: ${this.dieta}, vegetariano: ${this.vegetariano}"
    }
}
