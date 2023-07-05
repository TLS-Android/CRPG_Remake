package com.tiagosantos.crpg_remake.ui

import androidx.annotation.StringRes
import com.tiagosantos.crpg_remake.R

val FeatureType.contextualHelp: Int
    @StringRes
    get() = when(this){
        FeatureType.MEDIA_PLAYER -> R.string.contextual_media_player
        FeatureType.LEMBRETES -> R.string.contextual_reminder
        FeatureType.MEDITACAO -> R.string.contextual_meditation
        FeatureType.REFEICOES -> R.string.contextual_media_player
        FeatureType.DATE_PICKER -> R.string.contextual_date_picker
        FeatureType.AGENDA -> R.string.contextual_agenda
        FeatureType.UNKNOWN -> R.string.s
    }


val FeatureType.isSpeaking: Boolean
    get() = when(this){
        FeatureType.MEDIA_PLAYER -> false
        FeatureType.LEMBRETES -> false
        FeatureType.MEDITACAO -> false
        FeatureType.REFEICOES -> false
        FeatureType.DATE_PICKER -> false
        FeatureType.AGENDA -> true
        FeatureType.UNKNOWN -> false
    }
