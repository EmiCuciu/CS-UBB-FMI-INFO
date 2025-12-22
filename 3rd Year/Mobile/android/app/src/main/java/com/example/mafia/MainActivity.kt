package com.example.mafia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.example.mafia.databinding.ActivityMainBinding
import com.example.mafia.worker.SyncCharactersWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupToolbar()
        setupNavigation()
        setupPeriodicSync()  // Initialize WorkManager
    }

    private fun setupWindowInsets() {
        // Handle system bars properly
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup toolbar with navigation controller
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    /**
     * Setup periodic background sync with WorkManager
     * Syncs pending changes every 15 minutes when network is available
     *
     * LAB REQUIREMENT: Background Tasks - WorkManager (3p)
     */
    private fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)  // Wait for WiFi/Mobile Data
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncCharactersWorker>(
            15, TimeUnit.MINUTES  // Repeat every 15 minutes
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10, TimeUnit.SECONDS  // Retry with exponential backoff on failure
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "character_sync_periodic",
            ExistingPeriodicWorkPolicy.KEEP,  // Don't create duplicate if already exists
            syncRequest
        )
    }
}