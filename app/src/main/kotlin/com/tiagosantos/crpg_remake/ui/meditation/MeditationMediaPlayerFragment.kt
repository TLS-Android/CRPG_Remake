package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

class MeditationMediaPlayerFragment : BaseModalFragment<FragmentMeditationMediaPlayerBinding>(
        layoutId = R.layout.fragment_meditation_media_player,
        settings = FragmentSettings(
            appBarTitle = R.string.title_media,
            sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
            showBackButton = true
        ),
        ttsSettings = TTSSettings(
           contextualHelp =  "Indique qual o seu estado de espirito atual",
        ),
        srSettings = SRSettings(actionMap = mapOf("ola" to "adeus"))
) {
    private var _view: FragmentMeditationMediaPlayerBinding? = null
    private val view get() = _view!!

    private val medViewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _view = FragmentMeditationMediaPlayerBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(_view!!){
            textSelectedMood.text = medViewModel.selectedMood
            medViewModel.setupPlayer(player,this)

        with(moodColor){
            when(medViewModel.selectedMood){
                "RELAXADO" -> setBackgroundColor(android.graphics.Color.parseColor("#00BBF2"))
                "FELIZ" -> setBackgroundColor(android.graphics.Color.parseColor("#87B700"))
                "SONOLENTO" -> setBackgroundColor(android.graphics.Color.parseColor("#FBC02D"))
                "CONFIANTE" -> setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
                "QUERIDO" -> setBackgroundColor(android.graphics.Color.parseColor("#AA00FF"))
                "MENTE SÃ" -> setBackgroundColor(android.graphics.Color.parseColor("#57A8D8"))
            }
        }

        buttonReturnMeditation.setOnClickListener {
            val fragment: Fragment = MeditationFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        gotev.speechResult.observe(viewLifecycleOwner){
            updateColorWithVoiceCommand(it)
        }
        }
    }

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String,Any>){
       with(view.custom){
           when {
               command.contains("Tocar", true) -> exoPlay.performClick()
               command.contains("Parar", true) -> exoPause.performClick()
               command.contains("Passar à frente", true) -> exoFfwd.performClick()
               command.contains("Passar a trás", true) -> exoRew.performClick()
               command.contains("Regressar", true) ->
                   root.rootView.findViewById(R.id.button_return_meditation)
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

    override fun observeLifecycleEvents() {
        TODO("Not yet implemented")
    }

}