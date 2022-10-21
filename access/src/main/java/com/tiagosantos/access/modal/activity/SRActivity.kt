package com.tiagosantos.access.modal.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tiagosantos.access.modal.settings.ActivitySettings

abstract class SRActivity(
    private val settings: ActivitySettings
) : AppCompatActivity(), BaseActivityInterface {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}