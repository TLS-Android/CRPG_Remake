package com.tiagosantos.crpg_remake.helper

import androidx.annotation.ColorRes
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.ui.meditation.Mood

val Mood.colorId: Int
    @ColorRes
    get() = when(this){
        Mood.RELAXADO -> R.color.md_blue_100
        Mood.FELIZ -> R.color.md_orange_100
        Mood.SONOLENTO -> R.color.white
        Mood.CONFIANTE -> R.color.black
        Mood.VALORIZADO -> R.color.colorGrey100
        Mood.MENTE_SA -> R.color.md_green_100
    }
