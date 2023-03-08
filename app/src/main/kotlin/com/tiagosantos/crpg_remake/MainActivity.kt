package com.tiagosantos.crpg_remake

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tiagosantos.common.ui.extension.hide
import com.tiagosantos.common.ui.extension.show
import com.tiagosantos.crpg_remake.base.ActivitySettings
import com.tiagosantos.crpg_remake.base.BaseActivity
import com.tiagosantos.crpg_remake.base.MainActivityInterface
import com.tiagosantos.crpg_remake.data.sharedprefs.SharedPrefsViewModel
import com.tiagosantos.crpg_remake.databinding.ActivityMainBinding

/**
Android framework will try to instantiate an object of your activity.
And it calls the default constructor (without parameters) when it does that.
It fails when your class does not have a constructor without parameters.

So as there is no way you can pass your parameters to default constructor,
you cannot employ it to work for you.
*/
@Suppress("DEPRECATION")
class MainActivity : MainActivityInterface, BaseActivity(
    layoutId = R.layout.activity_main,
    ActivitySettings(
        isAdjustFontScaleToNormal = true,
        windowFlags = listOf(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    ),
) {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView

    val shared by viewModels<SharedPrefsViewModel>()

    private fun setupParams() {
        this.layoutId = R.layout.activity_main
        this.settings = ActivitySettings(
            isAdjustFontScaleToNormal = true,
            windowFlags = listOf(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        )
    }

    /** The modules() function in startKoin load the given list of modules  **/
    @Suppress("UNUSED_PARAMETER")
    override fun onCreate(savedInstanceState: Bundle?) {
        setupParams()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_agenda)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun finish() {
        super.finish()
        shared.resetAppPreferences()
    }

    override fun showNavBar() { navView.show() }
    override fun hideNavBar() { navView.hide() }

}
