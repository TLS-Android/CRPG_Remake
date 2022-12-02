package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding

class MeditationFragment : BaseModalFragment<FragmentMeditationBinding>(
    layoutId = R.layout.fragment_meditation,
    settings = FragmentSettings(
        appBarTitle = R.string.title_meditacao,
        sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
        showBackButton = true
    ),
        ttsSettings = TTSSettings(
            contextualHelp =  "Indique qual o seu estado de espirito atual",
        ),
        srSettings = SRSettings(
            isListening = false,
        )
) {

    private val medViewModel: MeditationViewModel by viewModels()

    /** This property is only valid between onCreateView and onDestroyView. **/
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private var onResumeFlag = false

    val feelingsMap = mapOf(
        "RELAXADO" to viewB.buttonMoodRelaxed ,  "FELIZ" to viewB.buttonMoodHappy,
        "SONOLENTO" to viewB.buttonMoodSleepy, "CONFIANTE" to viewB.buttonMoodConfident)

    companion object {
        fun newInstance() = MeditationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
    }

    private fun setupButtons() {
        for((mood, value) in feelingsMap) {
            value.setOnClickListener { medViewModel.selectedMood = mood }.also {
                goToFragment(MeditationMediaPlayerFragment()) }
        }
    }

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String, Any>) =
            feelingsMap.getOrDefault(command) { println("do nothing") }

    override fun onPause() {
        super.onPause()
        onResumeFlag = true
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }
}