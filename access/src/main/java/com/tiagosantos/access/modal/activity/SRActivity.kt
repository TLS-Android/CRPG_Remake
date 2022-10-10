package com.tiagosantos.access.modal.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.tiagosantos.access.modal.settings.ActivitySettings
import com.tiagosantos.common.ui.extension.applyStatusBarColor
import com.tiagosantos.common.ui.extension.dpToPx
import com.tiagosantos.crpg_remake.R

abstract class SRActivity(
    private val settings: ActivitySettings
) : AppCompatActivity(), BaseActivityInterface {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (settings.isAdjustFontScaleToNormal)
            adjustFontScaleToNormal(resources.configuration)

    }

    override fun finish() {
        super.finish()
    }

    override fun setAppBarSubTitle(subTitleString: String) {
        actionBar?.apply {
            val view: View? = findViewById(R.id.appbar)

            view?.apply {
                if (!TextUtils.isEmpty(subTitleString))
                //set appbar min height
                    view.minimumHeight = dpToPx(72f).toInt()
                else
                //remove appbar min height
                    view.minimumHeight = 0
            }

            subtitle = subTitleString
        }
    }

    override fun setStatusBarColor(@ColorRes colorResId: Int) {
        applyStatusBarColor(colorResId)
    }




}