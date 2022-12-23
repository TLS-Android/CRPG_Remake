package com.tiagosantos.crpg_remake

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tiagosantos.access.modal.modality.ModalityPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardingActivity : Activity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private lateinit var prefRepo: ModalityPreferencesRepository

    private var isRequestingSettings = true

    override fun onCreate(savedInstanceState: Bundle?) {
        /** Call installSplashScreen in the starting activity before calling super.onCreate().
         * Removed theme from manifest because it was causing issues
         * **/
        //val splashScreen = installSplashScreen()
        //splashScreen.setKeepOnScreenCondition { isRequestingSettings }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        prefRepo = ModalityPreferencesRepository(this).apply {
            requestMultiModalityOptions()
        }.also { isRequestingSettings = false }

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
