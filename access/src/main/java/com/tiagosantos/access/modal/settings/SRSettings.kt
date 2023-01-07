package com.tiagosantos.access.modal.settings

data class SRSettings(
    val isListening: Boolean? = false,
    val actionType: ActionType? = ActionType.GENERAL_VIEW,
    val commandList: List<String> = listOf(),
    val actionMap: Map<String, Any>? = mapOf(),
)

enum class ActionType {
    GENERAL_VIEW,
    DATE_PICKER,
    REMINDER,
}
