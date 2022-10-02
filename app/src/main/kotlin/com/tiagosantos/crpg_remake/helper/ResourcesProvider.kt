package com.tiagosantos.crpg_remake.helper

import androidx.annotation.StringRes

/**
 *  Provider for app resources (as example, from strings.xml)
 *
 * Created on Dec 15, 2020.
 *
 */
interface ResourcesProvider {
    fun getErrorMessage(throwable: Throwable?): String

    fun getStringMessage(@StringRes resId: Int): String

    fun getFormattedString(@StringRes resId: Int, vararg args: Any): String
}