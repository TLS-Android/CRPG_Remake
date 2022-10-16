package com.tiagosantos.crpg_remake.ui.meditation

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationMediaPlayerBinding

@SuppressLint("StaticFieldLeak")
class MeditationViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    var selectedMood = EMPTY_STRING

    fun setupPlayer(player: ExoPlayer, view: FragmentMeditationMediaPlayerBinding) {
        val uri: Uri = Uri.parse("android.resource://" + context?.packageName.toString()
                + "/raw/meditation_sound")

        player_view.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)

        player.prepare()
        player.play()
    }

}