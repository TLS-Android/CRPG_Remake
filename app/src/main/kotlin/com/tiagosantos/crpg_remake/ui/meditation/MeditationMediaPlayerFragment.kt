package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

/**
If Android decides to recreate your Fragment later,
it's going to call the no-argument constructor of your fragment.
So overloading the constructor is not a solution.
With that being said, the way to pass stuff to your Fragment so that they are available after a
Fragment is recreated by Android is to pass a bundle to the setArguments method
 **/
@FragmentWithArgs
class MeditationMediaPlayerFragment : BaseModalFragment<FragmentMeditationMediaPlayerBinding>() {

    @Arg override var layoutId = R.layout.fragment_meditation_media_player

    @Arg
    override var settings = FragmentSettings(
    appBarTitle = R.string.title_media,
    sharedPreferencesBooleanName = R.string.meditationHasRun.toString(),
    showBackButton = true
    )

    @Arg
    override var ttsSettings = TTSSettings(contextualHelp =  R.string.contextual_media_player)

    @Arg
    override var srSettings = SRSettings(
        commandList = listOf("Tocar", "Parar", "Passar à frente", "Passar a trás", "Regressar"),
        isListening = false,
    )

    private val viewModel: MeditationViewModel by viewModels()
    private val player = ExoPlayer.Builder(requireContext()).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewB.custom.apply {
            actionList = mutableListOf(exoPlay, exoPause, exoFfwd, exoRew, viewB.buttonReturnMeditation)
        }
        super.onCreate(savedInstanceState)
    }

    override fun setupUI() {
        with(viewB) {
            textSelectedMood.text = viewModel.selectedMood
            viewModel.setupPlayer(player, this)
            setBackgroundColor(viewB)
            buttonReturnMeditation.setOnClickListener {
                goToFragment(MeditationFragment())
            }
        }
    }

    private fun setBackgroundColor(binding: FragmentMeditationMediaPlayerBinding) {
        with(binding.moodColor) {
            when (viewModel.selectedMood) {
                "RELAXADO" -> setBackgroundColor(this, "")
                "FELIZ" -> setBackgroundColor(this, "#87B700")
                "SONOLENTO" -> setBackgroundColor(this, "#FBC02D")
                "CONFIANTE" -> setBackgroundColor(this, "#57A8D8")
                "QUERIDO" -> setBackgroundColor(this, "#AA00FF")
                "MENTE SÃ" -> setBackgroundColor(this, "#57A8D8")
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

    private fun setBackgroundColor(img: ImageView, str: String) =
        img.setBackgroundColor(android.graphics.Color.parseColor(str))

}
