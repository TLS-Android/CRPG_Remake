package com.tiagosantos.crpg_remake.navigation

import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.tiagosantos.crpg_remake.R

fun Fragment.launch(navigation: Navigation) {
    val commonNavOptionsBuilder = NavOptions.Builder()

    /*
    val navResId = when (navigation.to) {
        NavigateTo.MEALS -> R.id.actionNavToMeals
        NavigateTo.PROFILE_SETTINGS -> R.id.actionNavToProfileSettings
        else -> 0
    }

    if (navResId > 0)
        findNavController()
            .navigate(
                navResId,
                navigation.extras,
                commonNavOptionsBuilder.build()
            )
    else
        when (navigation.to) {
            NavigateTo.BACK ->
                requireActivity().onBackPressed()
            else -> {}
        }


     */

}