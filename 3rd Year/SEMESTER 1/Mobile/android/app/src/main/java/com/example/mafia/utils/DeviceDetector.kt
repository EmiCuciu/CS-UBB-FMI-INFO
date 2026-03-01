package com.example.mafia.utils

import android.os.Build
import android.util.Log

/**
 * Utility to check if running on emulator or real device
 */
object DeviceDetector {
    private const val TAG = "DeviceDetector"

    fun isEmulator(): Boolean {
        val result = (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
        
        Log.d(TAG, "Device detection:")
        Log.d(TAG, "  FINGERPRINT: ${Build.FINGERPRINT}")
        Log.d(TAG, "  MODEL: ${Build.MODEL}")
        Log.d(TAG, "  MANUFACTURER: ${Build.MANUFACTURER}")
        Log.d(TAG, "  BRAND: ${Build.BRAND}")
        Log.d(TAG, "  DEVICE: ${Build.DEVICE}")
        Log.d(TAG, "  PRODUCT: ${Build.PRODUCT}")
        Log.d(TAG, "  Is Emulator: $result")
        
        return result
    }

    fun getNetworkConfig(): String {
        return if (isEmulator()) {
            "Emulator detected - Using 10.0.2.2 (host loopback)"
        } else {
            "Real device detected - Using 10.99.138.25 (hotspot IP)"
        }
    }
}
