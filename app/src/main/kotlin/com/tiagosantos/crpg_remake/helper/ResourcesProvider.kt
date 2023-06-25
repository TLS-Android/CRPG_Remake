package com.tiagosantos.crpg_remake.helper

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.StringRes
import com.tiagosantos.common.ui.model.AlarmFrequency
import com.tiagosantos.common.ui.model.AlarmType

interface ResourcesProvider {
    fun getErrorMessage(throwable: Throwable?): String

    fun getStringMessage(@StringRes resId: Int): String

    fun getFormattedString(@StringRes resId: Int, vararg args: Any): String

    val states: Array<IntArray>
        get() = arrayOf(
            intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(-android.R.attr.state_enabled), // disabled
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_pressed)  // pressed
        )

    val colors: IntArray
        get() = intArrayOf(
            Color.BLACK,
            Color.RED,
            Color.GREEN,
            Color.BLUE
        )

    val alarmTypeStates: HashMap<Int, AlarmType>
        get() = hashMapOf(
            1 to AlarmType.SOM, 2 to AlarmType.VIBRAR, 3 to AlarmType.AMBOS
        )

    val alarmFreqStates: HashMap<Pair<Int, AlarmFrequency>
        get() = hashMapOf(
            1 to AlarmFrequency.HOJE, 2 to AlarmFrequency.TODOS_OS_DIAS, 3 to AlarmFrequency.PERSONALIZADO
        )

    val colorList get() = ColorStateList(states, colors)
}