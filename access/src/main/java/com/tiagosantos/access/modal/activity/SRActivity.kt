package com.tiagosantos.access.modal.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tiagosantos.access.modal.settings.ActivitySettings

abstract class SRActivity(
    private val settings: ActivitySettings
) : AppCompatActivity(), BaseActivityInterface {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun finish() {
        super.finish()
    }

}