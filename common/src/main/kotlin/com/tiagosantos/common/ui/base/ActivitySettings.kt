package com.tiagosantos.common.ui.base

import androidx.annotation.AnimRes

data class ActivitySettings(
    @AnimRes
    val openEnterAnimation: Int = 0,
    @AnimRes
    val openExitAnimation: Int = 0,
    @AnimRes
    val closeEnterAnimation: Int = 0,
    @AnimRes
    val closeExitAnimation: Int = 0,
    val isAdjustFontScaleToNormal: Boolean = false,
    val windowFlags: List<Int> = emptyList()
)