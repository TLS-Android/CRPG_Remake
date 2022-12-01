package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

/**
If Android decides to recreate your Fragment later,
it's going to call the no-argument constructor of your fragment.
So overloading the constructor is not a solution.
With that being said, the way to pass stuff to your Fragment so that they are available after a
Fragment is recreated by Android is to pass a bundle to the setArguments method
 **/

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
        srSettings = SRSettings(
            isListening = false,
            actionMap = mapOf("ola" to "adeus")
        )
) {

    private val medViewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view.buttonReturnMeditation

        with(view){
            textSelectedMood.text = medViewModel.selectedMood
            medViewModel.setupPlayer(player,this)

            with(moodColor){
                when(medViewModel.selectedMood){
                    "RELAXADO" -> setBackgroundColor(this,"")
                    "FELIZ" -> setBackgroundColor(this,"#87B700")
                    "SONOLENTO" -> setBackgroundColor(this,"#FBC02D")
                    "CONFIANTE" -> setBackgroundColor(this,"#57A8D8")
                    "QUERIDO" -> setBackgroundColor(this,"#AA00FF")
                    "MENTE SÃ" -> setBackgroundColor(this,"#57A8D8")
                }
            }

            buttonReturnMeditation.setOnClickListener {
                goToFragment(MeditationFragment())
            }

        }
    }

    override fun performActionWithVoiceCommand(command: String, actionMap: Map<String,Any>) {
        with(view.custom) {
            when {
                command.contains("Tocar", true) -> exoPlay.performClick()
                command.contains("Parar", true) -> exoPause.performClick()
                command.contains("Passar à frente", true) -> exoFfwd.performClick()
                command.contains("Passar a trás", true) -> exoRew.performClick()
                command.contains("Regressar", true) ->
                    root.rootView.findViewById(R.id.button_return_meditation)
                else -> { //TODO }
                }
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

    private fun setBackgroundColor(img: ImageView, str: String) =
        img.setBackgroundColor(android.graphics.Color.parseColor(str))

}