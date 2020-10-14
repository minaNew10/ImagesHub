package com.example.imageshub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.bumptech.glide.manager.SupportRequestManagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //in case of using fragment controller view we can't call find nav controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        val appBarConfig = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController,appBarConfig)
    }

    override fun onSupportNavigateUp(): Boolean {
        //the first will navigate up in our nab graph and will return true
        //to indicate that everything is ok if it returns false then it well recall the default implementation
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}