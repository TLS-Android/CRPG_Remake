package com.tiagosantos.crpg_remake.ui.meditation

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.FragmentMeditationMediaPlayerBinding
import com.plataforma.crpg.ui.MainActivity
import kotlinx.android.synthetic.main.custom_player_control_view.*
import kotlinx.android.synthetic.main.fragment_meditation_media_player.*
import net.gotev.speech.GoogleVoiceTypingDisabledException
import net.gotev.speech.Speech
import net.gotev.speech.SpeechDelegate
import net.gotev.speech.SpeechRecognitionNotAvailable


class MeditationMediaPlayerFragment : Fragment(){

    companion object {
        fun newInstance() = MeditationMediaPlayerFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMeditationMediaPlayerBinding.inflate(layoutInflater)
        return binding.root
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "REPRODUZIR ÁUDIO"
        val medViewModel = ViewModelProvider(activity as AppCompatActivity).get(MeditationViewModel::class.java)

        println(">Sel mood: " + medViewModel.selectedMood)
        medViewModel.getValue()
        text_selected_mood.text = medViewModel.selectedMood

        when(medViewModel.selectedMood){
            "RELAXADO" -> {
                mood_color.setBackgroundColor(Color.parseColor("#00BBF2"))
            }
            "FELIZ" -> {
                mood_color.setBackgroundColor(Color.parseColor("#87B700"))
            }
            "SONOLENTO" -> {
                mood_color.setBackgroundColor(Color.parseColor("#FBC02D"))
            }
            "CONFIANTE" -> {
                mood_color.setBackgroundColor(Color.parseColor("#57A8D8"))
            }
            "QUERIDO" -> {
                mood_color.setBackgroundColor(Color.parseColor("#AA00FF"))
            }
            "MENTE SÃ" -> {
                mood_color.setBackgroundColor(Color.parseColor("#57A8D8"))
            }
        }

        val uri: Uri = Uri.parse("android.resource://" + context?.packageName.toString()
                + "/raw/meditation_sound")
        val player = SimpleExoPlayer.Builder(requireContext()).build()

        player_view.player = player
        val mediaItem: MediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)

        player.prepare()
        player.play()

        button_return_meditation.setOnClickListener {
            player.stop()
            val fragment: Fragment = MeditationFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                player.stop()
                val fragment: Fragment = MeditationFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
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


    private fun startVoiceRecognition(){
        Speech.init(context)
        try {
            // you must have android.permission.RECORD_AUDIO granted at this point
            Speech.getInstance().startListening(object : SpeechDelegate {
                override fun onStartOfSpeech() {
                    Log.i("speech", "speech recognition is now active")
                }

                override fun onSpeechRmsChanged(value: Float) {
                    Log.d("speech", "rms is now: $value")
                }

                override fun onSpeechPartialResults(results: List<String>) {
                    val str = StringBuilder()
                    for (res in results) {
                        str.append(res).append(" ")
                    }
                    Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                }

                override fun onSpeechResult(result: String) {
                    performActionWithVoiceCommand(result)
                    Log.i("speech", "result: $result")
                    //println("on Speech Result")
                }
            })
        } catch (exc: SpeechRecognitionNotAvailable) {
            Log.e("speech", "Speech recognition is not available on this device!")
            // You can prompt the user if he wants to install Google App to have
            // speech recognition, and then you can simply call:
            //
            // SpeechUtil.redirectUserToGoogleAppOnPlayStore(this);
            //
            // to redirect the user to the Google App page on Play Store
        } catch (exc: GoogleVoiceTypingDisabledException) {
            Log.e("speech", "Google voice typing must be enabled!")
        }

    }

    private fun showBackButton() {
        if (activity is MainActivity) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}
//context?.resources?.getColor(R.color.material_blue_200)?.let { mood_color.setColorFilter(it) }
//
// getResources().openRawResource(R.raw.meditation_sound)
//        //val filePath = "raw/meditation_sound.mp3"
//
// player.setVideoSurface(null)
// player.clearVideoSurface()