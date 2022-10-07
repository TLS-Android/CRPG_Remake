package com.tiagosantos.crpg_remake.helper

import androidx.annotation.StringRes

interface ResourcesProvider {
    fun getErrorMessage(throwable: Throwable?): String

    fun getStringMessage(@StringRes resId: Int): String

    fun getFormattedString(@StringRes resId: Int, vararg args: Any): String
}