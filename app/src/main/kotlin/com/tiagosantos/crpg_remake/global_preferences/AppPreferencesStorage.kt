package com.tiagosantos.crpg_remake.global_preferences

interface AppPreferencesStorage {
    fun <T> getAppPreferences(transformation: (AppPreferencesRealm) -> T): T
    suspend fun saveAppPreferences(appPreferencesRealm: AppPreferencesRealm)
    suspend fun resetAppPreferences(appPreferencesRealm: AppPreferencesRealm)
}