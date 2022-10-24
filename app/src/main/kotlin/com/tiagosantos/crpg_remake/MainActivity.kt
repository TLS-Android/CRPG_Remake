package com.tiagosantos.crpg_remake

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.tiagosantos.access.modal.gossip.GossipViewModel
import com.tiagosantos.access.modal.gotev.GotevViewModel
import com.tiagosantos.common.ui.base.ActivitySettings
import com.tiagosantos.common.ui.base.BaseActivity
import com.tiagosantos.common.ui.model.appModule
import com.tiagosantos.crpg_remake.databinding.ActivityMainBinding
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : BaseActivity(
    layoutId = R.layout.activity_main,
    ActivitySettings(
        isAdjustFontScaleToNormal = true,
        windowFlags = listOf(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    )
) {
    private lateinit var binding: ActivityMainBinding

    override fun initToolbar() {
        TODO("Not yet implemented")
    }

    override fun initViews(layoutView: View) {
        TODO("Not yet implemented")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gotev by viewModels<GotevViewModel>()
        val gossip by viewModels<GossipViewModel>()

        startKoin {
            modules(appModule) //The modules() function in startKoin load the given list of modules
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}