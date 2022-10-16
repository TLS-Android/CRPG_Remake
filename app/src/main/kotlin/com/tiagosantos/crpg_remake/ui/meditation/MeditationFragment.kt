package com.tiagosantos.crpg_remake.ui.meditation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.tiagosantos.access.modal.BaseModalFragment
import com.tiagosantos.common.ui.base.FragmentSettings
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.FragmentMeditationBinding
import net.gotev.speech.Speech
import java.util.*
import kotlin.properties.Delegates

abstract class MeditationFragment : BaseModalFragment<FragmentMeditationBinding>(
    layoutId = R.layout.meals_fragment,
    FragmentSettings(
        appBarTitle = R.string.title_dashboard,
        sharedPreferencesBooleanName = R.string.mealsHasRun.toString(),
    )
) {

    private var onResumeFlag = false
    private var hasInitSR = false

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable by Delegates.notNull()

    private lateinit var speech: Speech

    companion object {
        fun newInstance() = MeditationFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentMeditationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        onResumeFlag = true
    }

    fun exp() {
        val l = listOf(1, 2, 3)
        l.forEachIndexed { index, _ -> println(index) }

        Person("Alice", 20, "Amsterdam").let {
            println(it)
            it.moveTo("London")
            it.incrementAge()
            println(it)
        }

        val p = Pair(1, 2)
        val (first, _) = p
        println(p)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val medViewModel:MeditationViewModel by viewModels()

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

        button_mood_loved.setOnClickListener{
            medViewModel.selectedMood = "QUERIDO"
            goToMeditationMediaPlayer()
        }

        button_mood_mindful.setOnClickListener{
            medViewModel.selectedMood = "MENTE SÃ"
            goToMeditationMediaPlayer()
        }

    }

    override fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {}

    private fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                        || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
                        "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun performActionWithVoiceCommand(command: String) {
        when {
            command.contains("Relaxado", true) -> button_mood_relaxed.performClick()
            command.contains("Feliz", true) -> button_mood_happy.performClick()
            command.contains("Com Sono", true) -> button_mood_sleepy.performClick()
            command.contains("Confiante", true) -> button_mood_confident.performClick()
            command.contains("Querido", true) -> button_mood_loved.performClick()
            command.contains("Mente Sã", true) -> button_mood_mindful.performClick()
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
}

//  val speechStatus = textToSpeech!!.speak("Selecione uma das opções ou diga o estado" +
//                            "em voz alta", TextToSpeech.QUEUE_FLUSH, null, "ID")