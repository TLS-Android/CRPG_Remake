package com.tiagosantos.crpg_remake.base

import androidx.annotation.ColorRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

data class FragmentSettings(
    val appBarTitle: Any = 0,
    val appBarSubTitle: Any = 0,
    @ColorRes
    val appBarColor: Int = 0,
    val sharedPreferencesBooleanName: String? = EMPTY_STRING,
    val showBackButton: Boolean? = true,
    val hideBottomNavigationView: Boolean? = false
)
