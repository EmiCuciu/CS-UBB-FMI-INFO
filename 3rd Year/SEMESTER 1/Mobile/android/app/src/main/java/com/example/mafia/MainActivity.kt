package com.example.mafia

import android.os.Bundle
import android.util.Log
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
        Log.d(TAG, "===== SETTING UP WORKMANAGER PERIODIC SYNC =====")

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

        Log.d(TAG, "WorkManager sync request created:")
        Log.d(TAG, "  - Work ID: ${syncRequest.id}")
        Log.d(TAG, "  - Periodic interval: 15 minutes")
        Log.d(TAG, "  - Network constraint: CONNECTED")
        Log.d(TAG, "  - Backoff policy: EXPONENTIAL (10s)")

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncCharactersWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  // Don't create duplicate if already exists
            syncRequest
        )

        Log.d(TAG, "✓ WorkManager periodic work enqueued: ${SyncCharactersWorker.WORK_NAME}")

        // Query and log current work status
        WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(SyncCharactersWorker.WORK_NAME)
            .observeForever { workInfos ->
                Log.d(TAG, "===== WORKMANAGER STATUS UPDATE =====")
                workInfos?.forEachIndexed { index, workInfo ->
                    Log.d(TAG, "Work [$index]:")
                    Log.d(TAG, "  - ID: ${workInfo.id}")
                    Log.d(TAG, "  - State: ${workInfo.state}")
                    Log.d(TAG, "  - Run attempt: ${workInfo.runAttemptCount}")
                    Log.d(TAG, "  - Tags: ${workInfo.tags}")

                    // Log output data
                    val pending = workInfo.outputData.getInt("pendingCount", -1)
                    val synced = workInfo.outputData.getInt("syncedCount", -1)
                    val error = workInfo.outputData.getString("lastError") ?: ""

                    if (pending >= 0) {
                        Log.d(TAG, "  - Output: pending=$pending, synced=$synced, error='$error'")
                    }
                }
            }

        Log.d(TAG, "===== WORKMANAGER SETUP COMPLETE =====")
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}