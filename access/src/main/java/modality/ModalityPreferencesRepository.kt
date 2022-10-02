package modality

import android.content.Context
import android.content.SharedPreferences
import android.speech.tts.TextToSpeech
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tiagosantos.common.ui.utils.Constants.MODALITY
//import net.gotev.speech.Speech
//import net.gotev.speech.TextToSpeechCallback

class ModalityPreferencesRepository(
    val ctx: Context,
) {
    private var textToSpeech: TextToSpeech? = null

    fun requestMultiModalityOptions() {
        val sharedPreferences = ctx.getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        launchAlertDialog(
            "Lançar notificações para teste",
            "Para teste de usabilidade apenas",
            "test",
            editor
        )

        launchAlertDialog(
            "Permitir Sugestões de Áudio",
            "A aplicação possui uma voz virtual que poder dar-lhe indicações de como" +
                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
            "TTS",
            editor
        )

        launchAlertDialog(
            "Permitir Comandos de Voz",
            "A aplicação pode ser usada utilizando " +
                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
            "SR",
            editor
        )
    }

    private fun launchAlertDialog(
        title: String,
        message: String,
        putBoolean: String,
        editor: SharedPreferences.Editor
    ) {
        MaterialAlertDialogBuilder(ctx, android.R.style.Theme_Material_Dialog_Alert)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Permitir") { _, _ ->
                editor.putBoolean(putBoolean, true)
            }
            .setNegativeButton("Recusar") { _, _ ->
                editor.putBoolean(putBoolean, false)
            }.show()
    }


}
