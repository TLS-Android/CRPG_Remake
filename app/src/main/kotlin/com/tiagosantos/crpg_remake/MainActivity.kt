package com.tiagosantos.crpg_remake

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.gotev.GotevViewModel
import com.tiagosantos.crpg_remake.base.ActivitySettings
import com.tiagosantos.crpg_remake.base.BaseActivity
import com.tiagosantos.crpg_remake.base.BaseActivityInterface
import com.tiagosantos.crpg_remake.base.MainActivityInterface
import com.tiagosantos.crpg_remake.databinding.ActivityMainBinding
import com.tiagosantos.crpg_remake.global_preferences.AppPreferencesRepository

/**
Android framework will try to instantiate an object of your activity.
And it calls the default constructor (without parameters) when it does that.
It fails when your class does not have a constructor without parameters.

So as there is no way you can pass your parameters to default constructor,
you cannot employ it to work for you.
*/
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

    /**
    override fun initToolbar() {
        TODO("Not yet implemented")
    }

    override fun initViews(layoutView: View) {
        //layoutView.
    }
    **/

    private fun setupParams() {
        this.layoutId = R.layout.activity_main
        this.settings = ActivitySettings(
            isAdjustFontScaleToNormal = true,
            windowFlags = listOf(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        )
        this.appPreferences = AppPreferencesRepository(
            applicationContext
        )
    }

    /** The modules() function in startKoin load the given list of modules  **/
    override fun onCreate(savedInstanceState: Bundle?) {
        setupParams()
        val gotev by viewModels<GotevViewModel>()
        val gossip by viewModels<GossipViewModel>()

        println("MainActivity")

        super.onCreate(savedInstanceState)

        //startKoin {
            //modules(appModule)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //val navView: BottomNavigationView = binding.navView
            navView = binding.navView
            //val navController = findNavController(R.id.nav_host_fragment_activity_main)

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
            val navController = navHostFragment.navController

            val appBarConfiguration = AppBarConfiguration(
                setOf(R.id.navigation_agenda)
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        //}
    }

    override fun showNavBar() { navView.visibility = VISIBLE }
    override fun hideNavBar() { navView.visibility = INVISIBLE }

}
