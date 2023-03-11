package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding

@FragmentWithArgs
class MeditationFragment : BaseModalFragment<FragmentMeditationBinding>(
    layoutId = R.layout.fragment_meditation,
    settings = FragmentSettings(
        appBarTitle = R.string.title_meditacao,
        sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
        showBackButton = true
    ),

) {
    @Arg
    override var ttsSettings = TTSSettings(contextualHelp =  R.string.contextual_meditation)

    @Arg
    override var srSettings = SRSettings(
    commandList = listOf("RELAXADO", "FELIZ", "SONOLENTO", "CONFIANTE"),
    isListening = false,
    )

    private val viewModel: MeditationViewModel by viewModels()

    /** This property is only valid between onCreateView and onDestroyView. **/
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val feelingsMap = with(viewB){
        mapOf("RELAXADO" to buttonMoodRelaxed , "FELIZ" to buttonMoodHappy,
        "SONOLENTO" to buttonMoodSleepy, "CONFIANTE" to buttonMoodConfident)
    }

    companion object {
        fun newInstance() = MeditationFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewB.apply {
            actionList = mutableListOf(buttonMoodRelaxed, buttonMoodHappy,
                buttonMoodSleepy, buttonMoodConfident)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun setupUI() {
        for((mood, value) in feelingsMap) {
            value.setOnClickListener { viewModel.selectedMood = mood }.also {
                goToFragment(MeditationMediaPlayerFragment()) }
        }
    }
}
