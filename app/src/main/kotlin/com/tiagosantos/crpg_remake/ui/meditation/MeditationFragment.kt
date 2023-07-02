package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.button.MaterialButton
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.actionMap
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding
import com.tiagosantos.crpg_remake.ui.FeatureType

@FragmentWithArgs
class MeditationFragment : BaseModalFragment<FragmentMeditationBinding>() {

    @Arg override var layoutId = R.layout.fragment_meditation

    private val feature = FeatureType.MEDITACAO

    @delegate:Arg
    override val settings by lazy {
        FragmentSettings(
            appBarTitle = getString(R.string.title_meditacao),
            sharedPreferencesBooleanName = getString(R.string.meditationHasRun),
            showBackButton = true
        )
    }

    @delegate:Arg
    override val ttsSettings by lazy {
        TTSSettings(contextualHelp =  R.string.contextual_meditation)
    }

    @delegate:Arg
    override val srSettings by lazy {
        SRSettings(
            commandList = feature.actionMap,
            isListening = false,
        )
    }

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
