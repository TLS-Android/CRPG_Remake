package com.tiagosantos.crpg_remake.ui.meditation

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
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.common.ui.extension.observe
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

class MeditationMediaPlayerFragment : BaseModalFragment<FragmentMeditationMediaPlayerBinding>(
        layoutId = R.layout.fragment_meditation_media_player,
        FragmentSettings(
            appBarTitle = R.string.title_media,
            sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
            showBackButton = true
        ),
        ttsSettings = TTSSettings("Indique qual o seu estado de espirito atual"),
        srSettings = SRSettings()
) {
    private lateinit var view: FragmentMeditationMediaPlayerBinding

    private val medViewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    val actionMap = mapOf("ola" to "adeus")

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"
        view.textSelectedMood.text = medViewModel.selectedMood
        medViewModel.setupPlayer(player,view)

        with(view.moodColor){
            when(medViewModel.selectedMood){
                "RELAXADO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#00BBF2"))
                "FELIZ" -> this.setBackgroundColor(android.graphics.Color.parseColor("#87B700"))
                "SONOLENTO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#FBC02D"))
                "CONFIANTE" -> this.setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
                "QUERIDO" -> this.setBackgroundColor(android.graphics.Color.parseColor("#AA00FF"))
                "MENTE SÃ" -> this.setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
            }
        }

        view.buttonReturnMeditation.setOnClickListener {
            val fragment: Fragment = MeditationFragment(TTSSettings())
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gotev.speechResult.observe(viewLifecycleOwner){
            performActionWithVoiceCommand(it, actionMap)
        }
    }

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String,Any>){
       with(view){
           when {
               command.contains("Tocar", true) -> exo_play?.performClick()
               command.contains("Parar", true) -> exo_pause?.performClick()
               command.contains("Passar à frente", true) -> exo_ffwd?.performClick()
               command.contains("Passar a trás", true) -> exo_rew?.performClick()
               command.contains("Regressar", true) -> buttonReturnMeditation.performClick()
               else -> { print("ola") }
           }
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

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}