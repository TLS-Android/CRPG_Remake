package com.tiagosantos.crpg_remake

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tiagosantos.access.modal.modality.ModalityPreferencesRepository
import com.tiagosantos.crpg_remake.databinding.SplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardingActivity : Activity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private lateinit var prefRepo: ModalityPreferencesRepository

    private lateinit var binding : SplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        //val splashScreen = installSplashScreen()






        prefRepo = ModalityPreferencesRepository(this).apply {
            requestMultiModalityOptions()
        }

        /*
        activityScope.launch {
            delay(3000)

            val intent = Intent(
                this@BoardingActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }
        */

    }
}
