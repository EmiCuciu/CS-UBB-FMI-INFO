package com.example.mafia.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mafia.data.datastore.AuthDataStore
import com.example.mafia.data.local.AppDatabase
import com.example.mafia.data.remote.RetrofitClient
import com.example.mafia.data.repository.CharacterRepository
import com.example.mafia.utils.NetworkConnectivityObserver
import com.example.mafia.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Worker for syncing character data in the background
 * Uses WorkManager to schedule periodic sync operations
 */
class SyncCharactersWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "sync_characters_work"
        private const val TAG = "SyncCharactersWorker"
    }

    private val notificationHelper = NotificationHelper(context)
    private val networkObserver = NetworkConnectivityObserver(context)
    private val authDataStore = AuthDataStore(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "===== STARTING CHARACTER SYNC WORK =====")

            // Check network connectivity
            if (!networkObserver.isConnected()) {
                Log.w(TAG, "No network connection - scheduling retry")
                return@withContext Result.retry()
            }
            Log.d(TAG, "✓ Network connection available")

            // Get token from DataStore
            val token = authDataStore.getToken()
            if (token == null) {
                Log.w(TAG, "No auth token found - user logged out, aborting sync")
                return@withContext Result.failure()
            }
            Log.d(TAG, "✓ Auth token found: ${token.take(20)}...")

            // Set token for API calls
            RetrofitClient.setToken(token)

            // Initialize repository
            val database = AppDatabase.getDatabase(applicationContext)
            val characterDao = database.characterDao()
            val characterApi = RetrofitClient.characterApi
            val repository = CharacterRepository(characterDao, characterApi, applicationContext)

            // Check for pending changes BEFORE syncing
            val pendingCount = characterDao.getPendingSyncCharacters().size
            Log.d(TAG, "Found $pendingCount pending changes in local database")

            if (pendingCount == 0) {
                Log.d(TAG, "No pending changes to sync - skipping sync")
                // Still refresh from server to get latest data
                val refreshResult = repository.refreshCharacters()
                if (refreshResult.isSuccess) {
                    Log.d(TAG, "===== SYNC WORK COMPLETED (NO CHANGES) =====")
                    return@withContext Result.success()
                } else {
                    Log.e(TAG, "Refresh failed: ${refreshResult.exceptionOrNull()?.message}")
                    return@withContext Result.retry()
                }
            }

            // Sync pending changes (CREATE, UPDATE, DELETE)
            Log.d(TAG, "Starting sync of $pendingCount pending changes...")
            val syncResult = repository.syncPendingChanges()

            if (syncResult.isSuccess) {
                val syncedCount = syncResult.getOrNull() ?: 0
                Log.d(TAG, "✓ Sync successful: $syncedCount items synced")

                // Show notification ONLY if items were actually synced
                if (syncedCount > 0) {
                    notificationHelper.showSyncSuccessNotification(syncedCount)
                    Log.d(TAG, "✓ Sync success notification shown")
                }

                // Refresh from server to get latest data
                Log.d(TAG, "Refreshing from server after sync...")
                val refreshResult = repository.refreshCharacters()

                if (refreshResult.isSuccess) {
                    Log.d(TAG, "✓ Refresh successful")
                    Log.d(TAG, "===== SYNC WORK COMPLETED SUCCESSFULLY =====")
                    return@withContext Result.success()
                } else {
                    Log.e(TAG, "Refresh after sync failed: ${refreshResult.exceptionOrNull()?.message}")
                    return@withContext Result.retry()
                }
            } else {
                val error = syncResult.exceptionOrNull()?.message ?: "Unknown error"
                Log.e(TAG, "✗ Sync failed: $error")
                // NEVER show failure notifications - just retry silently
                return@withContext Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "✗ ERROR during sync work: ${e.message}", e)
            // NEVER show failure notifications - just retry silently
            return@withContext Result.retry()
        }
    }
}

