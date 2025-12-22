package com.example.mafia.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Detects shake gestures using the accelerometer sensor
 *
 * LAB REQUIREMENT: Sensors (3p)
 * Uses Android accelerometer sensor to detect shake gestures
 */
class ShakeDetector(
    private val onShake: () -> Unit
) : SensorEventListener {

    private var accelerationLast = SensorManager.GRAVITY_EARTH
    private var acceleration = SensorManager.GRAVITY_EARTH
    private val shakeThreshold = 15f // Sensitivity threshold

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Calculate acceleration magnitude
            val accelerationCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta = accelerationCurrent - accelerationLast
            acceleration = acceleration * 0.9f + delta

            // Detect shake
            if (acceleration > shakeThreshold) {
                onShake()
            }

            accelerationLast = accelerationCurrent
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation
    }
}

