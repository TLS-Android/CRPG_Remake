package com.tiagosantos.crpg_remake.ui.meditation

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tiagosantos.crpg_remake.R

class MeditationMediaPlayerFragment : Fragment(){

    private val medViewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMeditationMediaPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"
        medViewModel.getValue()
        text_selected_mood.text = medViewModel.selectedMood

        setupPlayer(player)

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
            player.stop()
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



    fun performActionWithVoiceCommand(command: String){
        when {
            command.contains("Tocar", true) -> exo_play?.performClick()
            command.contains("Parar", true) -> exo_pause?.performClick()
            command.contains("Passar à frente", true) -> exo_ffwd?.performClick()
            command.contains("Passar a trás", true) -> exo_rew?.performClick()
            command.contains("Regressar", true) -> button_return_meditation?.performClick()
        }
    }

}