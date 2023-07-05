package com.tiagosantos.crpg_remake.ui

import androidx.annotation.StringRes
import com.tiagosantos.crpg_remake.R

val FeatureType.appBarTitleRes: Int
    @StringRes
    get() = when(this){
        FeatureType.MEDIA_PLAYER -> R.string.title_media
        FeatureType.LEMBRETES -> R.string.title_reminders
        FeatureType.MEDITACAO -> R.string.title_meditacao
        FeatureType.REFEICOES -> R.string.title_refeicao
        FeatureType.DATE_PICKER -> R.string.escolher_data
        FeatureType.AGENDA -> R.string.title_agenda
        FeatureType.UNKNOWN -> R.string.s
    }


val FeatureType.sharedPrefsBooleanNameRes: Int
    @StringRes
    get() = when(this){
        FeatureType.MEDIA_PLAYER -> R.string.mediaHasRun
        FeatureType.LEMBRETES -> R.string.remindersHasRun
        FeatureType.MEDITACAO -> R.string.meditationHasRun
        FeatureType.REFEICOES -> R.string.mealsHasRun
        FeatureType.DATE_PICKER -> R.string.datePickerHasRun
        FeatureType.AGENDA -> R.string.agendaHasRun
        FeatureType.UNKNOWN -> R.string.s
    }