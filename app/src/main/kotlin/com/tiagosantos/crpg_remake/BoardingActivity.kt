package com.tiagosantos.crpg_remake

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiagosantos.access.modal.modality.ModalityPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardingActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private lateinit var prefRepo: ModalityPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefRepo = ModalityPreferencesRepository(applicationContext).apply {
            requestMultiModalityOptions()
        }

        setContentView(R.layout.splash_screen)
        activityScope.launch {
            delay(3000)

            val intent = Intent(
                this@BoardingActivity,
                BoardingActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }
}
