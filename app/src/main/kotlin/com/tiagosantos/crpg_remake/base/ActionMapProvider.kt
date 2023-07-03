package com.tiagosantos.crpg_remake.base

import com.tiagosantos.crpg_remake.ui.FeatureType

val FeatureType.actionMap: List<String>
    get() {
        return when (this) {
            FeatureType.AGENDA -> listOf(
                "Horas", "Dia", "Alerta", "Notas", "Lembrete", "Cancelar", "Guardar",
                "Tomar Medicação", "Apanhar Transporte",
                "Escolher Almoço", "O Meu Lembrete",
                "Som", "Vibrar", "Ambos", "Hoje", "Sempre", "Escolher Dias"
            )

            FeatureType.LEMBRETES -> listOf(
                "Carne", "Peixe", "Dieta", "Vegetariano", "Guardar"
            )

            FeatureType.REFEICOES -> listOf(
                "um", "dois", "três", "quatro", "cinco",
                "seis", "sete", "oito", "nove", "dez"
            )

            FeatureType.MEDITACAO -> listOf(
                "RELAXADO", "FELIZ", "SONOLENTO", "CONFIANTE"
            )

            FeatureType.MEDIA_PLAYER -> listOf(
                "Tocar", "Parar", "Passar à frente", "Passar a trás", "Regressar"
            )

            FeatureType.DATE_PICKER -> listOf(
                "um", "dois", "três", "quatro", "cinco",
                "seis", "sete", "oito", "nove", "dez"
            )

            FeatureType.UNKNOWN -> listOf()
        }
}
