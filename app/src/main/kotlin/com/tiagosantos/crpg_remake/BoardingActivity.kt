package com.tiagosantos.crpg_remake

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Call installSplashScreen in the starting activity before calling super.onCreate().
 * Removed theme from manifest because it was causing issues
 * **/
class BoardingActivity : Activity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)
    private var isRequestingSettings = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { isRequestingSettings }

        activityScope.launch {
            delay(1000)
            val intent = Intent(
                this@BoardingActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }

    }
}
