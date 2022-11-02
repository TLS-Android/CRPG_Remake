package com.tiagosantos.access.modal.settings

data class SRSettings(
    val isListening: Boolean? = false,
    val actionMap: Map<String, Any> = mapOf()
)


