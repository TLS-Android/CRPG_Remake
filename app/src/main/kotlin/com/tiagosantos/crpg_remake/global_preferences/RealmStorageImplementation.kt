package com.tiagosantos.crpg_remake.global_preferences
object RealmStorageImplementation : AppPreferencesStorage {

    internal const val REALM_EXTENSION = ".realm"
    override fun <T> getAppPreferences(transformation: (AppPreferencesRealm) -> T): T {
        TODO("Not yet implemented")
    }

    override suspend fun saveAppPreferences(appPreferencesRealm: AppPreferencesRealm) {
        TODO("Not yet implemented")
    }

    override suspend fun resetAppPreferences(appPreferencesRealm: AppPreferencesRealm) {
        TODO("Not yet implemented")
    }
}
