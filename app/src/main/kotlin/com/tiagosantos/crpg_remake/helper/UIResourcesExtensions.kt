@file:Suppress("DEPRECATION")

package com.tiagosantos.crpg_remake.helper

import androidx.annotation.DrawableRes
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.ui.meditation.Mood

val Mood.iconId: Int?
    @DrawableRes
    get() = when(this){
        Mood.RELAXADO -> R.color.md_blue_100
        Mood.FELIZ -> R.color.md_blue_100
        Mood.SONOLENTO -> R.color.md_blue_100
        Mood.CONFIANTE -> R.color.md_blue_100
        Mood.VALORIZADO -> R.color.md_blue_100
        Mood.MENTE_SA -> R.color.md_blue_100
    }
