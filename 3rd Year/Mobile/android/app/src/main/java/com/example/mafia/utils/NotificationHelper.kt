package com.example.mafia.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mafia.MainActivity

/**
 * Helper class for managing notifications
 */
class NotificationHelper(private val context: Context) {

    companion object {
        private const val SYNC_CHANNEL_ID = "sync_channel"
        private const val SYNC_CHANNEL_NAME = "Data Sync"
        private const val SYNC_CHANNEL_DESCRIPTION = "Notifications for data synchronization"

        private const val UPDATE_CHANNEL_ID = "update_channel"
        private const val UPDATE_CHANNEL_NAME = "Character Updates"
        private const val UPDATE_CHANNEL_DESCRIPTION = "Notifications for character updates"

        private const val NETWORK_CHANNEL_ID = "network_channel"
        private const val NETWORK_CHANNEL_NAME = "Network Status"
        private const val NETWORK_CHANNEL_DESCRIPTION = "Notifications for network connectivity changes"

        const val SYNC_NOTIFICATION_ID = 1001
        const val UPDATE_NOTIFICATION_ID = 1002
        const val ERROR_NOTIFICATION_ID = 1003
        const val NETWORK_NOTIFICATION_ID = 1004

        private const val TAG = "NotificationHelper"
    }

    init {
        createNotificationChannels()
    }

    /**
     * Check if notification permission is granted (Android 13+)
     */
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Create notification channels (required for Android O and above)
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val syncChannel = NotificationChannel(
                SYNC_CHANNEL_ID,
                SYNC_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = SYNC_CHANNEL_DESCRIPTION
            }

            val updateChannel = NotificationChannel(
                UPDATE_CHANNEL_ID,
                UPDATE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = UPDATE_CHANNEL_DESCRIPTION
            }

            val networkChannel = NotificationChannel(
                NETWORK_CHANNEL_ID,
                NETWORK_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = NETWORK_CHANNEL_DESCRIPTION
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(syncChannel)
            notificationManager.createNotificationChannel(updateChannel)
            notificationManager.createNotificationChannel(networkChannel)
        }
    }

    /**
     * Show notification for sync start
     */
    fun showSyncStartNotification() {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .setContentTitle("Syncing Data")
            .setContentText("Synchronizing characters with server...")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        NotificationManagerCompat.from(context).notify(SYNC_NOTIFICATION_ID, notification)
    }

    /**
     * Show notification for sync success
     */
    fun showSyncSuccessNotification(itemCount: Int) {
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_sync_noanim)
            .setContentTitle("Sync Complete")
            .setContentText("Successfully synced $itemCount character(s)")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(SYNC_NOTIFICATION_ID, notification)
    }

    /**
     * Show notification for sync failure
     */
    fun showSyncFailureNotification(error: String) {
        if (!hasNotificationPermission()) {
            Log.d(TAG, "Notification permission not granted, skipping sync failure notification")
            return
        }

        Log.d(TAG, "Showing sync failure notification: $error")

        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle("Sync Failed")
            .setContentText(if (error.contains("cancel", ignoreCase = true))
                "Job was cancelled - will retry when online"
            else
                error)
            .setPriority(NotificationCompat.PRIORITY_LOW)  // Changed to LOW so it's less intrusive
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(ERROR_NOTIFICATION_ID, notification)
            Log.d(TAG, "Sync failure notification posted")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to show sync failure notification", e)
        }
    }

    /**
     * Show notification for character update
     */
    fun showCharacterUpdateNotification(characterName: String, action: String) {
        if (!hasNotificationPermission()) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, UPDATE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Character $action")
            .setContentText("$characterName was $action")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(context).notify(UPDATE_NOTIFICATION_ID, notification)
    }

    /**
     * Show notification for network status change
     */
    fun showNetworkStatusNotification(isConnected: Boolean) {
        if (!hasNotificationPermission()) {
            Log.d(TAG, "Notification permission not granted, skipping network status notification")
            return
        }

        Log.d(TAG, "Showing network status notification: isConnected=$isConnected")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NETWORK_CHANNEL_ID)
            .setSmallIcon(if (isConnected) android.R.drawable.stat_sys_upload_done else android.R.drawable.stat_sys_warning)
            .setContentTitle(if (isConnected) "Network Connected" else "Network Disconnected")
            .setContentText(if (isConnected) "Data sync will resume" else "Working in offline mode")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(false)  // Always alert even if updating same notification
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NETWORK_NOTIFICATION_ID, notification)
            Log.d(TAG, "Network notification posted successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to show network notification", e)
        }
    }

    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }

    /**
     * Cancel specific notification
     */
    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}

