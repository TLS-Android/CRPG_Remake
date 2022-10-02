package com.tiagosantos.crpg_remake

import android.content.res.loader.ResourcesProvider
import com.tiagosantos.crpg_remake.helper.ImplResourcesProvider
import com.tiagosantos.crpg_remake.helper.ResourcesProvider
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

