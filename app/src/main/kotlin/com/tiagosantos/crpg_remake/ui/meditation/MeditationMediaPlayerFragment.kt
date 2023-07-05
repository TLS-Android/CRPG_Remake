package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.ExoPlayer
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs
import com.tiagosantos.access.modal.settings.SRSettings
import com.tiagosantos.access.modal.settings.TTSSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.base.BaseModalFragment
import com.tiagosantos.crpg_remake.base.FragmentSettings
import com.tiagosantos.crpg_remake.base.actionMap
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding
import com.tiagosantos.crpg_remake.helper.colorId

/**
If Android decides to recreate your Fragment later,
it's going to call the no-argument constructor of your fragment.
So overloading the constructor is not a solution.
With that being said, the way to pass stuff to your Fragment so that they are available after a
Fragment is recreated by Android is to pass a bundle to the setArguments method
 **/
@FragmentWithArgs
class MeditationMediaPlayerFragment : BaseModalFragment<FragmentMeditationMediaPlayerBinding>() {

    private val viewModel: MeditationViewModel by viewModels()
    private lateinit var player : ExoPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewB.custom.apply {
            actionList = mutableListOf(
                exoPlay, exoPause, exoFfwd, exoRew, viewB.buttonReturnMeditation
            )
        }

        player = ExoPlayer.Builder(requireContext()).build()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun setupUI() {
        with(viewB) {
            textSelectedMood.text = viewModel.selectedMood
            viewModel.setupPlayer(player, this)
            //setBackgroundColor(viewB)

            val mood = Mood.RELAXADO
            moodColor.apply { setBackgroundColor(resources.getColor(mood.colorId)) }

            buttonReturnMeditation.setOnClickListener {
                goToFragment(MeditationFragment())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        //player.stop()
    }

}
