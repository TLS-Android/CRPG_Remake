package com.tiagosantos.crpg_remake.base

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.tiagosantos.common.ui.extension.applyStatusBarColor
import com.tiagosantos.common.ui.extension.dpToPx
import com.tiagosantos.common.ui.extension.getColorCompatible
import com.tiagosantos.crpg_remake.R

@Suppress("DEPRECATION")
abstract class BaseActivity(
    @LayoutRes
    protected var layoutId: Int ?= null,
    protected var settings: ActivitySettings ?= null,
) : AppCompatActivity(), BaseActivityInterface {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private var actionBar: ActionBar? = null

    //abstract fun initToolbar()
    //abstract fun initViews(layoutView: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (settings!!.isAdjustFontScaleToNormal)
            adjustFontScaleToNormal(resources.configuration)

        //window feature flags
        settings!!.windowFlags.forEach {
            window.addFlags(it)
        }

        super.onCreate(savedInstanceState)

        //set start activity animation
        if (settings!!.openEnterAnimation != 0 || settings!!.openExitAnimation != 0)
            overridePendingTransition(settings!!.openEnterAnimation, settings!!.openExitAnimation)

        setContentView(layoutId!!)
        //initToolbar()
        actionBar = supportActionBar
        //setSupportActionBar(findViewById(R.id.appbar))

        //populate view
        //val layoutView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        //initViews(layoutView)

        //applyWindowInsets(findViewById(android.R.id.content))
    }

    override fun finish() {
        super.finish()
        //finish activity animation
        if (settings!!.closeEnterAnimation != 0 || settings!!.closeExitAnimation != 0)
            overridePendingTransition(settings!!.closeEnterAnimation, settings!!.closeExitAnimation)
        //appPreferences!!.resetAppPreferences()


    }

    override fun setAppBarTitle(titleString: String) {
        actionBar?.title = titleString
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

    override fun setAppBarColor(@ColorRes colorResId: Int) {
        actionBar?.apply {
            setBackgroundDrawable(
                ColorDrawable(
                    getColorCompatible(colorResId)
                )
            )
        }
    }

    override fun setAppBarTitleColor(@ColorRes colorResId: Int) {
        actionBar?.also {
            findViewById<Toolbar>(R.id.toolbar)?.apply {
                this.setTitleTextColor(
                    getColorCompatible(colorResId)
                )
            }
        }
    }

    override fun setAppWindowBackground(@DrawableRes backgroundResId: Int) {
        window?.setBackgroundDrawableResource(backgroundResId)
    }

    override fun setHomeAsUpIndicatorIcon(@DrawableRes drawable: Int) {
        actionBar?.apply {
            if (drawable != 0) {
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(drawable)
            } else
                setDisplayHomeAsUpEnabled(false)
        }
    }

    /**
     * Use Window Insets to apply system paddings to this activity
     */
    private fun applyWindowInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->

            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply the insets as a margin to the view. Here the system is setting
            // only the bottom, left, and right dimensions, but apply whichever insets are
            // appropriate to your layout. You can also update the view padding
            // if that's more appropriate.
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
                topMargin = insets.top
            }

            // Return CONSUMED if you don't want want the window insets to keep being
            // passed down to descendant views.
            WindowInsetsCompat.CONSUMED
        }
    }

    /**
     * This method helps to ignore font scale value from device settings.
     *
     * NOTE: User has ability to change font scale in the device settings,
     * in this case font will be scaled in the app
     *
     * @param configuration
     */
    @SuppressWarnings("deprecation")
    private fun adjustFontScaleToNormal(configuration: Configuration) {
        val metrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            baseContext.display?.getRealMetrics(metrics)
        else
            windowManager.defaultDisplay.getMetrics(metrics)

        val wm = getSystemService(WINDOW_SERVICE) as WindowManager?
        wm ?: return


        var densityDpiStable = DisplayMetrics.DENSITY_DEVICE_STABLE //480

        //Device may has different screen resolution modes.
        //As example, Samsung S8: 422 in FHD+, 562 in WQHD+
        var xDpi = resources.displayMetrics.xdpi.toInt()

        //round it to bigger value
        if (xDpi > DisplayMetrics.DENSITY_XXHIGH) {
            xDpi = DisplayMetrics.DENSITY_XXXHIGH //640
        } else if (xDpi > DisplayMetrics.DENSITY_XHIGH) {
            xDpi = DisplayMetrics.DENSITY_XXHIGH //480
        } else if (xDpi > DisplayMetrics.DENSITY_HIGH) {
            xDpi = DisplayMetrics.DENSITY_XHIGH //320
        }
        if (xDpi > 0) densityDpiStable = xDpi
        val densityDpiDefault = DisplayMetrics.DENSITY_DEFAULT
        val densityDefault = densityDpiStable.toFloat() / densityDpiDefault

        //ignore system zoom setting, set zoom by default
        //NOTE: the smaller densityDpiStable the zoom is smaller
        configuration.densityDpi = densityDpiStable

        //Set font scale by default
        configuration.fontScale = 1.00.toFloat()
        //ignore system scaling for fonts, set scale factor by default
        metrics.density = densityDefault
        metrics.scaledDensity = configuration.fontScale * densityDefault
        baseContext.resources.updateConfiguration(configuration, metrics)
    }
}