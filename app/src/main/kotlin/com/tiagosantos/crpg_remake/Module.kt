package com.tiagosantos.crpg_remake

import android.content.res.loader.ResourcesProvider
import com.mobiledevpro.app.helper.ImplResourcesProvider
import com.mobiledevpro.app.helper.ResourcesProvider
import com.mobiledevpro.database.di.coreDatabaseModule
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


/**
 * App Koin module
 *
 */
fun getModules() = listOf(
    presentationCommonModule,
)

val presentationCommonModule = module {
    single<ResourcesProvider> {
        ImplResourcesProvider(androidApplication().resources)
    }
}

