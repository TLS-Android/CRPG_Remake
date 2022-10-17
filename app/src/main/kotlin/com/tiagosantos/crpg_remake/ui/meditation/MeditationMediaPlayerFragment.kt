package com.tiagosantos.crpg_remake.ui.meditation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.modality.TTSPreferences
import com.tiagosantos.access.modal.settings.TTSFragmentSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

class MeditationMediaPlayerFragment(ttsSettings: TTSFragmentSettings) :
    BaseModalFragment<FragmentMeditationMediaPlayerBinding>(
        layoutId = R.layout.fragment_meditation,
        FragmentSettings(
            appBarTitle = R.string.title_dashboard,
            sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
        ),
        ttsSettings = TTSFragmentSettings(
            "ola",
            "Meditacao"
        )
) {

    private lateinit var view: FragmentMeditationMediaPlayerBinding

    private val medViewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View { return view.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"
        text_selected_mood.text = medViewModel.selectedMood
        medViewModel.setupPlayer(player,view)

        with(mood_color){
            when(medViewModel.selectedMood){
                "RELAXADO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#00BBF2"))
                "FELIZ" -> this.setBackgroundColor(android.graphics.Color.parseColor("#87B700"))
                "SONOLENTO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#FBC02D"))
                "CONFIANTE" -> this.setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
                "QUERIDO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#AA00FF"))
                "MENTE SÃ" -> this.setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
            }
        }

        button_return_meditation.setOnClickListener {
            val fragment: Fragment = MeditationFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        showBackButton()
    }

    override fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Tocar", true) -> exo_play?.performClick()
            command.contains("Parar", true) -> exo_pause?.performClick()
            command.contains("Passar à frente", true) -> exo_ffwd?.performClick()
            command.contains("Passar a trás", true) -> exo_rew?.performClick()
            command.contains("Regressar", true) -> button_return_meditation?.performClick()
        }
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onInitDataBinding() {
        TODO("Not yet implemented")
    }

    fun experiment(){
        val numbers = mutableListOf("one", "two", "three")
        val countEndsWithE = numbers.run {
            add("four")
            add("five")
            count { it.endsWith("e") }
        }
        println("There are $countEndsWithE elements that end with e.")
    }

}