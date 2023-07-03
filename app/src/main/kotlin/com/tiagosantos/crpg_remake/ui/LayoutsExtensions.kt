package com.tiagosantos.crpg_remake.ui

import androidx.annotation.LayoutRes
import com.tiagosantos.crpg_remake.R

val FeatureType.layoutId: Int
    @LayoutRes
    get() = when(this){
        FeatureType.MEDIA_PLAYER -> R.layout.fragment_meditation_media_player
        FeatureType.LEMBRETES -> R.layout.reminder_fragment
        FeatureType.MEDITACAO -> R.layout.fragment_meditation
        FeatureType.REFEICOES -> R.layout.meals_fragment
        FeatureType.DATE_PICKER -> R.layout.fragment_date_picker
        FeatureType.AGENDA -> R.layout.fragment_agenda
        FeatureType.UNKNOWN -> R.layout.meals_fragment
    }
