package com.tiagosantos.crpg_remake.ui.meals

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import com.plataforma.crpg.BaseFragment
import com.plataforma.crpg.ui.agenda.AgendaFragment
import com.plataforma.crpg.ui.agenda.SharedViewModel
import com.plataforma.crpg.ui.meals.MealsViewModel
import kotlin.properties.Delegates

class MealsFragment : BaseFragment() {

    private var textToSpeech: TextToSpeech? = null
    private var flagMealChosen = false
    val buttonList = listOf(true, true, true)

    companion object {
        fun newInstance() = MealsFragment()
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences = this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean("mealsHasRun", true).apply()

        handler.removeCallbacksAndMessages(null)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        if (handler.hasMessages(0)) {
            handler.removeCallbacks(runnable)
            println("meditation SR shutdown")
        }

        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            println("shutdown TTS")
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "REFEIÇÕES"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val modalityPreferences = this.requireActivity().getSharedPreferences(
            MODALITY,
            Context.MODE_PRIVATE
        )

        val ttsFlag = modalityPreferences.getBoolean(TTS_FLAG, false)
        val srFlag = modalityPreferences.getBoolean(SR_FLAG, false)

        val binding = MealsFragmentBinding.inflate(layoutInflater)
        val view = binding.root

        showBackButton()
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event
                val fragment: Fragment = AgendaFragment()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
                fragmentManager.popBackStack()
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                onPause()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return view
        // return inflater.inflate(R.layout.meals_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mealsViewModel =
            ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)

        val modalityPreferences =
            this.requireActivity().getSharedPreferences("MODALITY", Context.MODE_PRIVATE)
        val ttsFlag = modalityPreferences.getBoolean("TTS", false)
        val srFlag = modalityPreferences.getBoolean("SR", false)

        val hasRun = modalityPreferences.getBoolean("mealsHasRun", false)

        defineModality(ttsFlag, srFlag, hasRun)

        /*
        text_opcao_carne.text = mealsViewModel.retrievedMeal.carne
        text_opcao_peixe.text = mealsViewModel.retrievedMeal.peixe
        text_opcao_dieta.text = mealsViewModel.retrievedMeal.dieta
        text_opcao_vegetariano.text = mealsViewModel.retrievedMeal.vegetariano
        */
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mealsViewModel = ViewModelProvider(activity as AppCompatActivity).get(MealsViewModel::class.java)
        ViewModelProvider(activity as AppCompatActivity).get(SharedViewModel::class.java)

        val cardCarne: MaterialCardView? = view?.findViewById(R.id.frame_opcao_carne)
        val cardPeixe: MaterialCardView? = view?.findViewById(R.id.frame_opcao_peixe)
        val cardDieta: MaterialCardView? = view?.findViewById(R.id.frame_opcao_dieta)
        val cardVeg: MaterialCardView? = view?.findViewById(R.id.frame_opcao_vegetariano)

        val isLunch = requireArguments().getBoolean("isLunch")

        cardCarne?.setOnClickListener {

            if (!cardCarne.isChecked) {
                mealsViewModel.selectedOption = 1
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardCarne.isChecked = !cardCarne.isChecked
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardPeixe?.setOnClickListener {

            if (!cardPeixe.isChecked) {
                mealsViewModel.selectedOption = 2
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardPeixe.isChecked = !cardPeixe.isChecked
            cardCarne?.isChecked = false
            cardDieta?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardDieta?.setOnClickListener {

            if (!cardDieta.isChecked) {
                mealsViewModel.selectedOption = 3
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardDieta.isChecked = !cardDieta.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardVeg?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        cardVeg?.setOnClickListener {

            if (!cardVeg.isChecked) {
                mealsViewModel.selectedOption = 4
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardVeg.isChecked = !cardVeg.isChecked
            cardCarne?.isChecked = false
            cardPeixe?.isChecked = false
            cardDieta?.isChecked = false
            flagMealChosen = !flagMealChosen
        }

        val mealSuccessView = view?.findViewById<View>(R.id.meal_choice_success)
        val nothingCheckedWarning = view?.findViewById<View>(R.id.aviso_nenhuma_refeicao_checked)

        button_confirm_meal.setOnClickListener {
            if (mealsViewModel.selectedOption != 0) {
                mealSuccessView?.visibility = View.VISIBLE
                mealSuccessView?.bringToFront()
                nothingCheckedWarning?.visibility = View.GONE
                mealsViewModel.updateMealChoiceOnLocalStorage(
                    mealsViewModel.selectedOption,
                    isLunch
                )
                button_ok.setOnClickListener {
                    mealSuccessView?.visibility = View.GONE
                    handler.removeCallbacksAndMessages(null)
                    if (handler.hasMessages(0)) {
                        handler.removeCallbacks(runnable)
                    }
                }
            } else {
                nothingCheckedWarning?.visibility = View.VISIBLE
            }
        }

        fun setChecks(selOption: Int, booleanList: List<Boolean>) {
            if (cardCarne?.isChecked == true) {
                mealsViewModel.selectedOption = selOption
            } else {
                mealsViewModel.selectedOption = 0
            }
            cardCarne?.isChecked = cardCarne?.isChecked == booleanList[0]
            cardPeixe?.isChecked = booleanList[1]
            cardDieta?.isChecked = booleanList[2]
            cardVeg?.isChecked = booleanList[3]
            flagMealChosen = !flagMealChosen
        }
    }

    override fun performActionWithVoiceCommand(command: String) {

        val commandToActionMap = mapOf(
            "Carne" to frame_opcao_carne.performClick(),
            "Peixe" to frame_opcao_peixe.performClick(),
            "Dieta" to frame_opcao_dieta.performClick(),
            "Vegetariano" to frame_opcao_vegetariano.performClick(),
        )

        val a = commandToActionMap.keys.find {
            command.contains(it) ||
                (command.contains(it) && command.contains("Prato"))
        }

        commandToActionMap[a]
    }

    private fun defineModality(ttsFlag: Boolean, srFlag: Boolean, hasRun: Boolean) {
        println("ttsFlag:  $ttsFlag")
        println("srFlag: $srFlag")
        println("hasRun: $hasRun")

        if (!hasRun) {
            when {
                ttsFlag && !srFlag -> { startTTS() }
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag -> { multimodalOption() }
            }
        }

        if (hasRun) {
            when {
                !ttsFlag && srFlag -> { startVoiceRecognition() }
                ttsFlag && srFlag -> { startVoiceRecognition() }
            }
        }
    }

    override fun startTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Linguagem não suportada!")
                }
                val speechStatus = textToSpeech!!.speak(
                    "Diga Carne, Peixe, Dieta ou Vegetariano " +
                        "para selecionar o seu prato e depois diga confirmar para selecionar a sua opção",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun multimodalOption() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                val speechListener = object : UtteranceProgressListener() {
                    @Override
                    override fun onStart(p0: String?) {
                        println("Iniciou TTS")
                    }

                    override fun onDone(p0: String?) {
                        println("Encerrou TTS")
                        if (activity != null && isAdded) {
                            startVoiceRecognition()
                        }
                    }

                    override fun onError(p0: String?) { TODO("Not yet implemented") }
                }

                textToSpeech?.setOnUtteranceProgressListener(speechListener)

                val speechStatus = textToSpeech!!.speak(
                    "Diga Carne, Peixe," +
                        " Dieta ou Vegetariano para selecionar o seu prato e depois diga confirmar " +
                        "para selecionar a sua opção",
                    TextToSpeech.QUEUE_FLUSH, null, "ID"
                )
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun startVoiceRecognition() {
        // MANTER WIFI SEMPRE LIGADO
        if (isAdded && isVisible && userVisibleHint) {
            runnable = Runnable {
                handler.sendEmptyMessage(0)
                Speech.init(requireActivity())
                // hasInitSR = true
                try {
                    Speech.getInstance().startListening(object : SpeechDelegate {
                        override fun onStartOfSpeech() {
                            Log.i("speech", "meal speech recognition is now active")
                        }

                        override fun onSpeechRmsChanged(value: Float) {
                            Log.d("speech", "rms is now: $value")
                        }

                        override fun onSpeechPartialResults(results: List<String>) {
                            val str = StringBuilder()
                            for (res in results) {
                                str.append(res).append(" ")
                            }
                            performActionWithVoiceCommand(results.toString())
                            Log.i("speech", "partial result: " + str.toString().trim { it <= ' ' })
                        }

                        override fun onSpeechResult(result: String) {
                            Log.d(TimelineView.TAG, "onSpeechResult: " + result.lowercase())
                            // Speech.getInstance().stopTextToSpeech()
                            val handler = Handler()
                            if (activity != null && isAdded) {
                                handler.postDelayed({
                                    try {
                                        if (isAdded && isVisible && userVisibleHint) {
                                            Speech.init(requireActivity())
                                            Speech.getInstance().startListening(this)
                                        }
                                    } catch (speechRecognitionNotAvailable: SpeechRecognitionNotAvailable) {
                                        speechRecognitionNotAvailable.printStackTrace()
                                    } catch (e: GoogleVoiceTypingDisabledException) {
                                        e.printStackTrace()
                                    }
                                }, 100)
                            }
                        }
                    })
                } catch (exc: SpeechRecognitionNotAvailable) {
                    Log.e("speech", "Speech recognition is not available on this device!")
                } catch (exc: GoogleVoiceTypingDisabledException) {
                    Log.e("speech", "Google voice typing must be enabled!")
                }
            }

            handler.post(runnable)
        }
    }
}
