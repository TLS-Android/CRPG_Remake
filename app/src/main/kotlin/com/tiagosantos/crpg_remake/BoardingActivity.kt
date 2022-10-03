package com.tiagosantos.crpg_remake

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.plataforma.crpg.utils.Constants

class BoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestMultiModalityOptions()
        setContentView(R.layout.splash_screen)
        val handler = Handler()
        handler.postDelayed(Runnable {
            val mInHome = Intent(this@Splash_Screen_Activity, InvoiceASAPTabActivity::class.java)
            this@Splash_Screen_Activity.startActivity(mInHome)
            this@Splash_Screen_Activity.finish()
        }, 3000)
    }

    private fun requestMultiModalityOptions() {
        val sharedPreferences = this.applicationContext.getSharedPreferences(Constants.MODALITY, Context.MODE_PRIVATE)
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
