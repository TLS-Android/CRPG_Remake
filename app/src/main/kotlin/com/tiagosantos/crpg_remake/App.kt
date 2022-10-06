package com.tiagosantos.crpg_remake

import android.app.Application
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
           // androidContext(this@App)
            modules(getModules())
        }
    }
}
