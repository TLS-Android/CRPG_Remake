package com.tiagosantos.crpg_remake.ui.meditation

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING

@SuppressLint("StaticFieldLeak")
class MeditationViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    var selectedMood = EMPTY_STRING
    
    fun getValue(){
        println(">Selected mood no VM:$selectedMood")
    }

}