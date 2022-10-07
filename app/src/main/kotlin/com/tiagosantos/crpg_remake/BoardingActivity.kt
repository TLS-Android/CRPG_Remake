package com.tiagosantos.crpg_remake

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tiagosantos.common.ui.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardingActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestMultiModalityOptions()

        setContentView(R.layout.splash_screen)
        activityScope.launch {
            delay(3000)

            val intent = Intent(
                this@BoardingActivity,
                BoardingActivity::class.java
            )
            startActivity(intent)
            finish()
        }
    }

    private fun requestMultiModalityOptions() {
        val sharedPreferences =
            this.applicationContext.getSharedPreferences(
                Constants.MODALITY,
                Context.MODE_PRIVATE
            )
        val editor = sharedPreferences.edit()

        launchAlertDialog(
            getString(R.string.lancar_notificacoes_teste),
            "Para teste de usabilidade apenas",
            "test",
            editor
        )

        launchAlertDialog(
            getString(R.string.permitir_sugestoes_audio),
            "A aplicação possui uma voz virtual que poder dar-lhe indicações de como" +
                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
            "TTS",
            editor
        )

        launchAlertDialog(
            getString(R.string.permitir_comandos_voz),
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
        MaterialAlertDialogBuilder(this.applicationContext, android.R.style.Theme_Material_Dialog_Alert)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(getString(R.string.permitir)) { _, _ ->
                editor.putBoolean(putBoolean, true)
            }
            .setNegativeButton(getString(R.string.recusar)) { _, _ ->
                editor.putBoolean(putBoolean, false)
            }.show()
    }
}
