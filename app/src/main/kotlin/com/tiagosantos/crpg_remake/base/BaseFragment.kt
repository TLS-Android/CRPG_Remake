@file:Suppress("DEPRECATION")

package com.tiagosantos.crpg_remake.base

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hannesdorfmann.fragmentargs.annotation.Arg
import com.tiagosantos.common.ui.extension.getColorCompatible
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.Constants.FEATURE_TYPE_KEY
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.crpg_remake.MainActivity
import com.tiagosantos.crpg_remake.ui.FeatureType
import com.tiagosantos.crpg_remake.ui.layoutId

abstract class BaseFragment<B : ViewDataBinding, T : FeatureType>: Fragment() {

    protected val feature: T by lazy {
        arguments?.getSerializable(FEATURE_TYPE_KEY) as T
    }

    protected abstract val settings: FragmentSettings
    @LayoutRes @Arg protected open var layoutId: Int = feature.layoutId

    open lateinit var viewBinding: B

    private var _viewB: B? = null
    val viewB get() = _viewB!!

    lateinit var fragment: Fragment

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navController = findNavController()
            navController.popBackStack()
        }
    }

    protected fun navigateToDestination(destinationId: Int) {
        val navController = findNavController()
        navController.navigate(destinationId)
    }

    private fun showBackButton() {
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(
            settings.showBackButton ?: false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewB = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewB.lifecycleOwner = viewLifecycleOwner
        showBackButton()
        return viewB.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyResources()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onStop() {
        val activity = requireActivity()

        //hide keyboard if it was shown
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

        inputManager?.let {
            val view = activity.currentFocus
            it.hideSoftInputFromWindow(view?.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
        }

        super.onStop()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Menu items are doubling after fragment has been re-created. Need to execute clear()
        menu.clear()
        //if (settings.optionsMenuId != 0) inflater.inflate(settings.optionsMenuId, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences =
            this.requireActivity().getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean(settings.sharedPreferencesBooleanName, true).apply()
        }
    }

    private fun setLightOrDarkStatusBarContent(@ColorRes statusBarColor: Int, view: View) {

        val rgb: Int = requireContext().getColorCompatible(statusBarColor) // 0xAARRGGBB

        val red: Int = rgb.shr(16) and 0xff
        val green: Int = rgb.shr(8) and 0xff
        val blue: Int = rgb.shr(0) and 0xff

        val lum: Double = 0.2126 * red + 0.7152 * green + 0.0722 * blue // per ITU-R BT.709

        //if lum greater than 128, the status bar color is light, so the content should be dark and vise versa
        val isLight = lum > 128 //0..255 : 0 - darkest, 255 - lightest

        view.windowInsetsController?.setSystemBarsAppearance(
            if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0, // value
            if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0 // mask
        )
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireActivity(),
            statusBarColor
        )
    }

    private fun applyResources() {
        (requireActivity() is MainActivityInterface).apply {
            if (settings.hideBottomNavigationView == true) {
                (activity as MainActivity).hideNavBar()
            } else {
                (activity as MainActivity).showNavBar()
            }
        }

        (requireActivity() is BaseActivityInterface).apply {
            if (!this) {
                settings.apply {
                    throw UnsupportedOperationException(
                        when {
                            appBarColor != 0 ->
                                "Your activity should extends from 'BaseActivity' to set status bar color"

                            appBarTitle as Int != 0 || (appBarTitle as String).isEmpty() ->
                                "Your activity should extends from 'BaseActivity' to set AppBar title"

                            appBarSubTitle as Int != 0 || (appBarSubTitle as String).isEmpty() ->
                                "Your activity should extends from 'BaseActivity' to set AppBar sub-title"

                            else -> "Your activity should extends from 'BaseActivity' to set AppBar sub-title"
                        })
                }
            }

            (requireActivity() as BaseActivityInterface).apply {
                setAppBarTitle(
                    when (settings.appBarTitle) {
                        is Int -> if (settings.appBarTitle != 0) settings.appBarTitle.toString() else EMPTY_STRING
                        is String -> settings.appBarTitle.toString().ifEmpty { EMPTY_STRING }
                        else -> EMPTY_STRING
                    }
                )

                //apply sub-title
                setAppBarSubTitle(
                    when (settings.appBarSubTitle) {
                        is Int -> if (settings.appBarSubTitle != 0) settings.appBarSubTitle.toString() else EMPTY_STRING
                        is String -> settings.appBarSubTitle.toString().ifEmpty { EMPTY_STRING }
                        else -> EMPTY_STRING
                    }
                )

                //apply color to appbar
                if (settings.appBarColor != 0)
                    setAppBarColor(settings.appBarColor)

            }
        }
    }
}
