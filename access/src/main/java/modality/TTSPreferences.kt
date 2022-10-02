package modality

class TTSPreferences {
/*
    private fun onSpeakClick() {
        Speech.getInstance().say(
            "Hello",
            object : TextToSpeechCallback {
                override fun onStart() {
                    Toast.makeText(
                        ctx,
                        "TTS onStart",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCompleted() {
                    Toast.makeText(
                        ctx,
                        "TTS onCompleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError() {
                    Toast.makeText(
                        ctx,
                        "TTS onError",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    fun ttsDatePickerHint() {
        textToSpeech = TextToSpeech(ctx) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = textToSpeech!!.setLanguage(Constants.myLocale)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA ||
                    ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "The Language is not supported!")
                } else {
                    Log.i("TTS", "Language Supported.")
                }
                Log.i("TTS", "Initialization success.")

                if (textToSpeech!!.isSpeaking) {
                    _ttsFlag = true
                }

                if (!textToSpeech!!.isSpeaking) {
                    _ttsFlag = false
                }

                val speechStatus = textToSpeech!!.speak(
                    "Por favor selecione um dia movendo os" +
                            "quadrados amarelos para a esquerda e direita e premindo aquele que pretender selecionar",
                    TextToSpeech.QUEUE_FLUSH, null
                )
            } else {
                Toast.makeText(ctx, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }


 */
}