package com.tiagosantos.crpg_remake.ui.agenda.timeline.extentions

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.tiagosantos.crpg_remake.ui.agenda.timeline.TimelineApplication

fun dpToPx(dp: Float): Int {
    return dpToPx(dp, TimelineApplication.instance.resources)
}

private fun dpToPx(dp: Float, resources: Resources): Int {
    val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    return px.toInt()
}

fun getColorCompat(resId: Int) = ContextCompat.getColor(TimelineApplication.instance, resId)

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}
