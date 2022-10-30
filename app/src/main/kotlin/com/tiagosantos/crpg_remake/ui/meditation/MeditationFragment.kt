package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding

class MeditationFragment(ttsSettings: TTSSettings, srSettings: SRSettings) :
    BaseModalFragment<FragmentMeditationBinding>(
    layoutId = R.layout.fragment_meditation,
    settings  = FragmentSettings(
        appBarTitle = R.string.title_meditacao,
        sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
        showBackButton = true
    ),
    ttsSettings = ttsSettings,
    srSettings = srSettings
) {
    private lateinit var view: FragmentMeditationBinding
    private var onResumeFlag = false

    val feelingsMap = mapOf(
        view.buttonMoodRelaxed to "RELAXADO", view.buttonMoodHappy to "FELIZ",
        view.buttonMoodSleepy to "SONOLENTO", view.buttonMoodConfident to "CONFIANTE")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val medViewModel:MeditationViewModel by viewModels()

        with(view){
            buttonMoodRelaxed.setOnClickListener{
                medViewModel.selectedMood = "RELAXADO"
            }.also { goToMeditationMediaPlayer() }

            buttonMoodHappy.setOnClickListener{
                medViewModel.selectedMood = "FELIZ"
            }.also { goToMeditationMediaPlayer() }

            buttonMoodSleepy.setOnClickListener{
                medViewModel.selectedMood = "SONOLENTO"
            }.also { goToMeditationMediaPlayer() }

            buttonMoodConfident.setOnClickListener{
                medViewModel.selectedMood = "CONFIANTE"
            }.also { goToMeditationMediaPlayer() }
            return root
        }
    }

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String, Any>) {
        with(view){
            when {
                command.contains("Relaxado", true) ->
                    buttonMoodRelaxed.performClick()
                command.contains("Feliz", true) ->
                    buttonMoodHappy.performClick()
                command.contains("Com Sono", true) ->
                    buttonMoodSleepy.performClick()
                command.contains("Confiante", true) ->
                    buttonMoodConfident.performClick()
                else -> { }
            }
        }
    }

    private fun goToMeditationMediaPlayer(){
        val fragment: Fragment = MeditationMediaPlayerFragment()
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        fragmentManager.popBackStack()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

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