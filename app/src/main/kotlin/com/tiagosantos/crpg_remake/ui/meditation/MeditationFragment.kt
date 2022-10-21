package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding
import com.tiagosantos.crpg_remake.databinding.MealsFragmentBinding
import java.util.*

class MeditationFragment(ttsSettings: TTSFragmentSettings) :
    BaseModalFragment<FragmentMeditationBinding>(
    layoutId = R.layout.fragment_meditation,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
    ), ttsSettings
) {
    private lateinit var view: MealsFragmentBinding
    private var onResumeFlag = false
    private var hasInitSR = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val medViewModel:MeditationViewModel by viewModels()

        val feelingsMap = mapOf(
           button_mood_relaxed to "RELAXADO", button_mood_happy to "FELIZ",
            button_mood_sleepy to 3, button_mood_confident to)

        button_mood_relaxed.setOnClickListener{
            medViewModel.selectedMood = "RELAXADO"
            goToMeditationMediaPlayer()
        }

        button_mood_happy.setOnClickListener{
            medViewModel.selectedMood = "FELIZ"
            goToMeditationMediaPlayer()
        }

        button_mood_sleepy.setOnClickListener{
            medViewModel.selectedMood = "SONOLENTO"
            goToMeditationMediaPlayer()
        }

        button_mood_confident.setOnClickListener{
            medViewModel.selectedMood = "CONFIANTE"
            goToMeditationMediaPlayer()
        }
    }

    override fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {}

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String, Any>) {

        when {
            command.contains("Relaxado", true) ->
                button_mood_relaxed.performClick()
            command.contains("Feliz", true) ->
                button_mood_happy.performClick()
            command.contains("Com Sono", true) ->
                button_mood_sleepy.performClick()
            command.contains("Confiante", true) ->
                button_mood_confident.performClick()
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
}