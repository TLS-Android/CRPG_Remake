package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding

@FragmentWithArgs
class MeditationFragment : BaseModalFragment<FragmentMeditationBinding>() {

    private val viewModel: MeditationViewModel by viewModels()

    /** This property is only valid between onCreateView and onDestroyView. **/
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private lateinit var feelingsMap : Map<String, MaterialButton>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewB){
            feelingsMap =  mapOf("RELAXADO" to buttonMoodRelaxed , "FELIZ" to buttonMoodHappy,
                "SONOLENTO" to buttonMoodSleepy, "CONFIANTE" to buttonMoodConfident)
            actionList = mutableListOf(buttonMoodRelaxed, buttonMoodHappy,
                buttonMoodSleepy, buttonMoodConfident)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupUI() {
        for((mood, value) in feelingsMap) {
            value.setOnClickListener { viewModel.selectedMood = mood }.also {
                goToFragment(MeditationMediaPlayerFragment()) }
        }
    }
}
