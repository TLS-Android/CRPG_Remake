package com.tiagosantos.crpg_remake.helper

import android.content.res.Resources
import android.content.res.loader.ResourcesProvider
import android.os.Build
import androidx.annotation.RequiresApi
import com.tiagosantos.common.ui.utils.NetworkConnectionThrowable
import com.tiagosantos.crpg_remake.R

/**
 * Provider for app resources (as example, from strings.xml)
 *
 * Created on Dec 15, 2020.
 *
 */
@RequiresApi(Build.VERSION_CODES.R)
class ImplResourcesProvider(
    private val resources: Resources
) : ResourcesProvider {

    override fun getErrorMessage(throwable: Throwable?): String =
        when (throwable) {

            is NetworkConnectionThrowable ->
                resources.getString(R.string.message_trouble_internet_connection)

            else -> throwable?.localizedMessage ?: ""
        }


    override fun getStringMessage(resId: Int): String =
        resources.getString(resId)

    override fun getFormattedString(resId: Int, vararg args: Any): String =
        resources.getString(resId, *args)
}