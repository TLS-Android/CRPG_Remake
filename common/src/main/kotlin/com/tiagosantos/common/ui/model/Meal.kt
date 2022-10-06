package com.tiagosantos.common.ui.model

data class Meal(
    val data: String,
    val carne: String,
    val peixe: String,
    val dieta: String,
    val vegetariano: String
) {
    override fun toString(): String {
        return "data: ${this.data},carne: ${this.carne}, peixe: ${this.peixe}," +
                " dieta: ${this.dieta}, vegetariano: ${this.vegetariano}"
    }
}
