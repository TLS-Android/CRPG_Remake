package com.tiagosantos.crpg_remake.base

import android.content.Context
import android.os.Build
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tiagosantos.common.ui.extension.getColorCompatible
import com.tiagosantos.common.ui.utils.Constants.EMPTY_STRING
import com.tiagosantos.common.ui.utils.Constants.MODALITY
import com.tiagosantos.crpg_remake.MainActivity
import com.tiagosantos.crpg_remake.R
import com.tiagosantos.crpg_remake.databinding.ReminderFragmentBinding

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int,
    protected val settings: FragmentSettings,
) : Fragment() {

    open lateinit var viewBinding: B

    private var _view: B? = null
    val view get() = _view!!

    val fragment: Fragment
        get() {
            TODO()
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction =
                fragmentManager.beginTransaction()
            fragmentTransaction.replace(androidx.navigation.fragment.R.id.nav_host_fragment_container, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun showBackButton() {
        if (activity is MainActivity && settings.showBackButton == true) {
            (activity as MainActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    protected fun goToFragment(destination: Fragment){
            val fragment: Fragment = destination
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
            fragmentManager.popBackStack()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
    }

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    abstract fun onInitDataBinding()

    abstract fun observeLifecycleEvents()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _view = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        showBackButton()
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
        observeLifecycleEvents()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Menu items are doubling after fragment has been re-created. Need to execute clear()
        menu.clear()
        //if (settings.optionsMenuId != 0) inflater.inflate(settings.optionsMenuId, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            requireActivity().onBackPressed()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        val sharedPreferences =
            this.requireActivity().getSharedPreferences(MODALITY, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply { putBoolean(settings.sharedPreferencesBooleanName, true).apply() }
    }

    private fun setLightOrDarkStatusBarContent(@ColorRes statusBarColor: Int, view: View) {

        val rgb: Int = requireContext().getColorCompatible(statusBarColor) // 0xAARRGGBB

        val red: Int = rgb.shr(16) and 0xff
        val green: Int = rgb.shr(8) and 0xff
        val blue: Int = rgb.shr(0) and 0xff

        val lum: Double = 0.2126 * red + 0.7152 * green + 0.0722 * blue // per ITU-R BT.709

        //if lum greater than 128, the status bar color is light, so the content should be dark and vise versa
        val isLight = lum > 128 //0..255 : 0 - darkest, 255 - lightest

        //For API 30+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.windowInsetsController?.setSystemBarsAppearance(
                if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0, // value
                if (isLight) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0 // mask
            )
        }
        //for API 23+
        else
            if (isLight) {
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
            }
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), statusBarColor)
    }

    private fun applyResources() {
        (requireActivity() is BaseActivityInterface).apply {
            if (!this) {
                if (settings.appBarColor != 0)
                    throw UnsupportedOperationException("Your activity should extends from 'BaseActivity' to set AppBar color")
                if (settings.appBarTitle as Int != 0 || (settings.appBarTitle as String).isEmpty())
                    throw UnsupportedOperationException("Your activity should extends from 'BaseActivity' to set AppBar title")
                if (settings.appBarSubTitle as Int != 0 || (settings.appBarSubTitle as String).isEmpty())
                    throw UnsupportedOperationException("Your activity should extends from 'BaseActivity' to set AppBar sub-title")
                }

            (requireActivity() as BaseActivityInterface).apply {
                setAppBarTitle(
                    when (settings.appBarTitle) {
                        is Int -> if (settings.appBarTitle != 0) resources.getString(settings.appBarTitle) else EMPTY_STRING
                        is String -> settings.appBarTitle.ifEmpty { EMPTY_STRING }
                        else -> EMPTY_STRING
                    }
                )

                //apply sub-title
                setAppBarSubTitle(
                    when (settings.appBarSubTitle) {
                        is Int -> if (settings.appBarSubTitle != 0) resources.getString(settings.appBarSubTitle) else EMPTY_STRING
                        is String -> settings.appBarSubTitle.ifEmpty { EMPTY_STRING }
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
