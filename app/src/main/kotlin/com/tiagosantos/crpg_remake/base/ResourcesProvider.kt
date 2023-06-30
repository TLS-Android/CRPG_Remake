package com.tiagosantos.crpg_remake.base

object ResourcesProvider {

    var list: List<String>? = listOf()
        get() {
            return when(null) {
                "REMINDER_FRAGMENT" -> listOf(
                    "Horas", "Dia", "Alerta", "Notas", "Lembrete", "Cancelar", "Guardar",
                    "Tomar Medicação", "Apanhar Transporte",
                    "Escolher Almoço", "O Meu Lembrete",
                    "Som","Vibrar","Ambos","Hoje","Sempre","Escolher Dias"
                )
                "MEALS_FRAGMENT" -> listOf(
                    "Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"
                )
                "DATE_PICKER_FRAGMENT" -> listOf(
                    "um", "dois", "três", "quatro", "cinco",
                    "seis", "sete", "oito", "nove", "dez"
                )
                "MEDITATION_FRAGMENT" -> listOf(
                    "RELAXADO", "FELIZ", "SONOLENTO", "CONFIANTE"
                )

                else -> { return null }
            }
        }

    fun getResources(fragment: String): List<String>? {
        return when(fragment) {
            "REMINDER_FRAGMENT" -> listOf(
                "Horas", "Dia", "Alerta", "Notas", "Lembrete", "Cancelar", "Guardar",
                "Tomar Medicação", "Apanhar Transporte",
                "Escolher Almoço", "O Meu Lembrete",
                "Som","Vibrar","Ambos","Hoje","Sempre","Escolher Dias"
            )
            "MEALS_FRAGMENT" -> listOf(
                "Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"
            )
            "DATE_PICKER_FRAGMENT" -> listOf(
                "um", "dois", "três", "quatro", "cinco",
                "seis", "sete", "oito", "nove", "dez"
            )
            "MEDITATION_FRAGMENT" -> listOf(
                "RELAXADO", "FELIZ", "SONOLENTO", "CONFIANTE"
            )

            else -> { return null }
        }
    }
}
