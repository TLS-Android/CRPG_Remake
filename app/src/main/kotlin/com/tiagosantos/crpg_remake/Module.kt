package com.tiagosantos.crpg_remake

import android.os.Build
import androidx.annotation.RequiresApi
import com.tiagosantos.crpg_remake.helper.ImplResourcesProvider
import com.tiagosantos.crpg_remake.helper.ResourcesProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


/**
 * App Koin module
 *
 */
@RequiresApi(Build.VERSION_CODES.R)
fun getModules() = listOf(
    presentationCommonModule,
)

@RequiresApi(Build.VERSION_CODES.R)
val presentationCommonModule = module {
    single<ResourcesProvider> {
        ImplResourcesProvider(androidApplication().resources)
    }
}

