package com.example.mafia.worker

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.mafia.data.datastore.AuthDataStore
import com.example.mafia.data.local.AppDatabase
import com.example.mafia.data.remote.RetrofitClient
import com.example.mafia.data.repository.CharacterRepository
import com.example.mafia.utils.NetworkConnectivityObserver
import com.example.mafia.utils.NotificationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

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

        private const val OUTPUT_PENDING = "pendingCount"
        private const val OUTPUT_SYNCED = "syncedCount"
        private const val OUTPUT_ERROR = "lastError"

        private const val RETRY_WORK_NAME = "sync_retry_delayed"

        fun enqueueDelayedRetry(context: Context, delaySeconds: Long = 5) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val req = OneTimeWorkRequestBuilder<SyncCharactersWorker>()
                .setConstraints(constraints)
                .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .addTag("sync_retry")
                .build()

            Log.d(TAG, "🔄 Enqueuing retry work with ${delaySeconds}s delay")
            WorkManager.getInstance(context)
                .enqueueUniqueWork(RETRY_WORK_NAME, ExistingWorkPolicy.REPLACE, req)
        }

        /**
         * Trigger immediate sync when network becomes available
         */
        fun triggerImmediateSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val req = OneTimeWorkRequestBuilder<SyncCharactersWorker>()
                .setConstraints(constraints)
                .addTag("immediate_sync")
                .build()

            Log.d(TAG, "🚀 Triggering IMMEDIATE sync")
            WorkManager.getInstance(context)
                .enqueueUniqueWork("immediate_sync", ExistingWorkPolicy.REPLACE, req)
        }

        private fun isLikelyNetworkException(t: Throwable): Boolean {
            return t is IOException || t is SocketTimeoutException || t is ConnectException
        }
    }

    private val notificationHelper = NotificationHelper(context)
    private val networkObserver = NetworkConnectivityObserver(context)
    private val authDataStore = AuthDataStore(context)

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "===== STARTING CHARACTER SYNC WORK =====")
            Log.d(TAG, "Work id=${id}, attempt=$runAttemptCount")

            // WorkManager constraints already ensure CONNECTED.
            // Avoid being overly strict here: VALIDATED can be false briefly after reconnect.
            val status = networkObserver.getCurrentNetworkStatus()
            Log.d(TAG, "Network status: isConnected=${status.isConnected}, hasInternet=${status.hasInternet}, type=${status.networkType}")
            if (!status.isConnected) {
                Log.w(TAG, "⚠️ No active network - scheduling retry in 5 seconds")
                enqueueDelayedRetry(applicationContext, delaySeconds = 5)
                return@withContext Result.retry()
            }
            Log.d(TAG, "✓ Network available (CONNECTED)")

            // Get token from DataStore
            val token = authDataStore.getToken()
            if (token.isNullOrBlank()) {
                Log.w(TAG, "No auth token found - aborting sync (user logged out?)")
                return@withContext Result.failure(
                    workDataOf(
                        OUTPUT_PENDING to -1,
                        OUTPUT_SYNCED to 0,
                        OUTPUT_ERROR to "No auth token"
                    )
                )
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
            val pending = characterDao.getPendingSyncCharacters()
            Log.d(TAG, "Found ${pending.size} pending changes in local database")

            if (pending.isEmpty()) {
                Log.d(TAG, "No pending changes to sync - refreshing from server")
                val refreshResult = repository.refreshCharacters()
                return@withContext if (refreshResult.isSuccess) {
                    Log.d(TAG, "===== SYNC WORK COMPLETED (NO CHANGES) =====")
                    Result.success(
                        workDataOf(
                            OUTPUT_PENDING to 0,
                            OUTPUT_SYNCED to 0,
                            OUTPUT_ERROR to ""
                        )
                    )
                } else {
                    val msg = refreshResult.exceptionOrNull()?.message ?: "Refresh failed"
                    Log.e(TAG, "Refresh failed: $msg")
                    enqueueDelayedRetry(applicationContext, delaySeconds = 20)
                    Result.retry()
                }
            }

            // Sync pending changes (CREATE, UPDATE, DELETE)
            Log.d(TAG, "📤 Starting sync of ${pending.size} pending changes...")
            pending.forEach { char ->
                Log.d(TAG, "  - ${char.name} (${char.id}): ${char.syncAction} [pendingSync=${char.pendingSync}]")
            }

            val syncResult = repository.syncPendingChanges()

            if (syncResult.isSuccess) {
                val syncedCount = syncResult.getOrNull() ?: 0
                Log.d(TAG, "✅ Sync successful: $syncedCount items synced")

                // Show notification ONLY if items were actually synced
                if (syncedCount > 0) {
                    notificationHelper.showSyncSuccessNotification(syncedCount)
                    Log.d(TAG, "🔔 Sync success notification shown")
                }

                // Refresh from server to get latest data
                Log.d(TAG, "🔄 Refreshing from server after sync...")
                val refreshResult = repository.refreshCharacters()

                return@withContext if (refreshResult.isSuccess) {
                    Log.d(TAG, "✅ Refresh successful")
                    Log.d(TAG, "===== SYNC WORK COMPLETED SUCCESSFULLY =====")
                    Result.success(
                        workDataOf(
                            OUTPUT_PENDING to pending.size,
                            OUTPUT_SYNCED to syncedCount,
                            OUTPUT_ERROR to ""
                        )
                    )
                } else {
                    val msg = refreshResult.exceptionOrNull()?.message ?: "Refresh after sync failed"
                    Log.e(TAG, "❌ Refresh after sync failed: $msg")
                    enqueueDelayedRetry(applicationContext, delaySeconds = 5)
                    Result.retry()
                }
            } else {
                val error = syncResult.exceptionOrNull()?.message ?: "Unknown error"
                Log.e(TAG, "✗ Sync failed: $error")
                enqueueDelayedRetry(applicationContext, delaySeconds = 20)
                return@withContext Result.retry()
            }
        } catch (e: Exception) {
            Log.e(TAG, "✗ ERROR during sync work: ${e.message}", e)
            if (isLikelyNetworkException(e)) {
                enqueueDelayedRetry(applicationContext, delaySeconds = 20)
            }
            return@withContext Result.retry()
        }
    }
}
