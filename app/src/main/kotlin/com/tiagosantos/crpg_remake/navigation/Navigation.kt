package com.tiagosantos.crpg_remake.navigation

import android.os.Bundle


data class Navigation(val to: NavigateTo) {
    var extras: Bundle? = null
}

enum class NavigateTo {
    MEALS,
    PROFILE_SETTINGS,
    BACK
}
