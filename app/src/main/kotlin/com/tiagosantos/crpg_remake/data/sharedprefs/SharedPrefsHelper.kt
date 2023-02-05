package com.tiagosantos.crpg_remake.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.tiagosantos.common.ui.utils.Constants.MODALITY

class SharedPrefsHelper(
    ctx: Context
) {
    private val PREFS_NAME = MODALITY
    private var sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPref = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun put(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    fun put(key: String, value: Boolean) {
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    fun getString(key: String): String? {
        return sharedPref.getString(key, null)
    }

    fun clear() {
        editor.clear().apply()
    }

}