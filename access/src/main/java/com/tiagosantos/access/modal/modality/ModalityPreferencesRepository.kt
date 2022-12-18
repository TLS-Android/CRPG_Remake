package com.tiagosantos.access.modal.modality

import android.content.Context
import android.content.SharedPreferences
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.common.ui.utils.Constants.PERMITIR
import com.tiagosantos.common.ui.utils.Constants.RECUSAR
import com.tiagosantos.common.ui.utils.Constants.SR
import com.tiagosantos.common.ui.utils.Constants.TTS

class ModalityPreferencesRepository(
    private val ctx: Context,
) {
    var alert = 0

    fun requestMultiModalityOptions() {
        println("requestMultiModalityOptions")

        val editor = ctx.getSharedPreferences(
            MODALITY,
            Context.MODE_PRIVATE
        ).edit()

        launchAlertDialog(
            "Permitir Sugestões de Áudio",
            "A aplicação possui uma voz virtual que poder dar-lhe indicações de como" +
                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
            TTS,
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
            .setPositiveButton(PERMITIR) { _, _ ->
                editor.putBoolean(putBoolean, true)
                if (alert == 0) {
                    launchAlertDialog(
                        "Permitir Comandos de Voz",
                        "A aplicação pode ser usada utilizando " +
                                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
                        SR,
                        editor
                    )
                }
                alert = 1
            }
            .setNegativeButton(RECUSAR) { _, _ ->
                editor.putBoolean(putBoolean, false)
                if (alert == 0) {
                    launchAlertDialog(
                        "Permitir Comandos de Voz",
                        "A aplicação pode ser usada utilizando " +
                                "utilizar a plataforma. Prima o botão \"Permitir\" para ativar esta funcionalidade.",
                        SR,
                        editor
                    )
                }
                alert = 1
            }.show()
    }
}
