package com.tiagosantos.crpg_remake

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.core.context.startKoin

class App : Application() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        startKoin {
           // androidContext(this@App)
            modules(getModules())
        }
    }
}
