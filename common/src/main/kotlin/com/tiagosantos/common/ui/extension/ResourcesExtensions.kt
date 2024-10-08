@file:Suppress("DEPRECATION")

package com.tiagosantos.common.ui.extension

import android.app.Activity
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import kotlin.math.roundToInt


/**
 * Get drawable  (compatible)
 *
 * @param drawableResId Resource Drawable ID
 * @return Drawable
 */
fun Context.getDrawableCompatible(@DrawableRes drawableResId: Int): Drawable? {
    if (drawableResId == 0)
        throw RuntimeException("Drawable Resource ID can not be 0")

    return ResourcesCompat.getDrawable(
        resources,
        drawableResId,
        theme
    )
}

fun Context.getThemeDrawable(@AttrRes drawableResId: Int): Drawable? {
    if (drawableResId == 0)
        throw RuntimeException("Drawable Resource ID can not be 0")

    val value = TypedValue()
    theme.resolveAttribute(drawableResId, value, true)

    return ResourcesCompat.getDrawable(
        resources,
        value.resourceId,
        theme)
}

/**
 * Get color (compatible)
 *
 * @param id Resource Color ID
 * @return Color
 */
@ColorInt
fun Context.getColorCompatible(@ColorRes id: Int): Int {
    if (id == 0)
        throw RuntimeException("Color Resource ID can not be 0")

    return ContextCompat.getColor(this, id)
}

@ColorInt
fun Context.getThemeColorCompatible(@AttrRes id: Int): Int {
    if (id == 0)
        throw RuntimeException("Color Resource ID can not be 0")

    val value = TypedValue()
    theme.resolveAttribute(id, value, true)
    return value.data
}

/**
 * Get VectorDrawable compatible with API < 23
 *
 * @param resId Drawable res id
 * @return Drawable
 */
fun Context.getVectorDrawable(@DrawableRes resId: Int): Drawable? {
    if (resId == 0)
        throw RuntimeException("Drawable Resource ID can not be 0")

    val d = VectorDrawableCompat.create(
        resources,
        resId,
        null) ?: return null
    return DrawableCompat.wrap(d)
}

/**
 * Get Animated VectorDrawable compatible with API < 23
 *
 * @param resId Drawable res id
 * @return Drawable
 */
fun Context.getAnimatedVectorDrawable(@DrawableRes resId: Int): Drawable? {
    if (resId == 0)
        throw RuntimeException("Drawable Resource ID can not be 0")

    val d = AnimatedVectorDrawableCompat.create(
        this,
        resId
    ) ?: return null
    return DrawableCompat.wrap(d)
}

/**
 * Convert Drawable to Bitmap (including Vector Drawable)
 *
 * @param context    Context
 * @param drawableId Drawable Res id
 * @return Bitmap
 */
@Throws(RuntimeException::class)
fun Context.getBitmapFromDrawable(@DrawableRes drawableId: Int): Bitmap? {
    if (drawableId == 0)
        throw RuntimeException("Drawable Resource ID can not be 0")

    val drawable: Drawable?
    val d: VectorDrawableCompat? =
        try {
            VectorDrawableCompat.create(
                resources,
                drawableId,
                null)
        } catch (e: NotFoundException) {
            null
        }

    drawable = if (d != null)
        DrawableCompat.wrap(d)
    else
        ContextCompat.getDrawable(this, drawableId)

    return when (drawable) {
        is BitmapDrawable -> drawable.bitmap
        is VectorDrawableCompat, is LayerDrawable -> {
            val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
        else -> throw IllegalArgumentException("Unsupported drawable type")
    }

}

/**
 * Convert DP to PX
 *
 * @param context   Context
 * @param valueInDp DP
 * @return PX
 */
fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

/**
 * Convert PX to DP
 *
 * @param context   Context
 * @param valueInPx PX
 * @return DP
 */
fun Context.pxToDp(valueInPx: Int): Int {
    val displayMetrics = resources.displayMetrics
    return (valueInPx / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

/**
 * Change Status Bar background color (API21+)
 *
 * @param activity   Activity
 * @param colorResId Color Resource Id
 */
fun Activity.applyStatusBarColor(@ColorRes colorResId: Int) {
    window.apply {
        //this.statusBarColor = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        this.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        this.statusBarColor = getColorCompatible(colorResId)
    }
}

/**
 * Get Display size
 *
 * @param activity Activity
 * @return {width, height}
 */
fun Activity.getDisplaySize(): IntArray {
    val displaySize = Point()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        baseContext.display?.getRealSize(displaySize)
    else
        windowManager.defaultDisplay.getSize(displaySize)

    val displayWidth = displaySize.x
    val displayHeight = displaySize.y
    return intArrayOf(displayWidth, displayHeight)
}


