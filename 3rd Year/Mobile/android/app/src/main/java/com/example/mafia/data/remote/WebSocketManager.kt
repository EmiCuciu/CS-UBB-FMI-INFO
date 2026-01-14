package com.example.mafia.data.remote

import android.os.Build
import android.util.Log
import com.example.mafia.data.model.Character
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketManager private constructor() {
    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private val listeners = mutableListOf<WebSocketListener>()

    companion object {
        private const val TAG = "WebSocketManager"
        private const val HOST_IP = "10.99.138.25"  // Your laptop's hotspot IP
        private const val EMULATOR_IP = "10.0.2.2"   // Special IP for Android Emulator
        private const val PORT = "3000"

        private fun getWebSocketUrl(): String {
            val host = if (isEmulator()) EMULATOR_IP else HOST_IP
            return "ws://$host:$PORT"
        }

        private fun isEmulator(): Boolean {
            return (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || "google_sdk" == Build.PRODUCT)
        }

        @Volatile
        private var instance: WebSocketManager? = null

        fun getInstance(): WebSocketManager {
            return instance ?: synchronized(this) {
                instance ?: WebSocketManager().also { instance = it }
            }
        }
    }

    interface WebSocketListener {
        fun onCharacterCreated(character: Character)
        fun onCharacterUpdated(character: Character)
        fun onCharacterDeleted(characterId: String)
        fun onConnectionError(error: String)
    }

    fun addListener(listener: WebSocketListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: WebSocketListener) {
        listeners.remove(listener)
    }

    fun connect(token: String) {
        if (webSocket != null) {
            Log.d(TAG, "WebSocket already connected")
            return
        }

        val wsUrl = getWebSocketUrl()
        Log.d(TAG, "Connecting to WebSocket: $wsUrl")

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(wsUrl)
            .build()

        webSocket = client.newWebSocket(request, object : okhttp3.WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket connected")
                // Authenticate with token
                val authMessage = JsonObject().apply {
                    addProperty("type", "auth")
                    addProperty("token", token)
                }
                webSocket.send(gson.toJson(authMessage))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "WebSocket message received: $text")
                try {
                    val jsonObject = gson.fromJson(text, JsonObject::class.java)
                    val type = jsonObject.get("type")?.asString

                    when (type) {
                        "auth" -> {
                            val status = jsonObject.get("status")?.asString
                            if (status == "success") {
                                Log.d(TAG, "WebSocket authenticated successfully")
                            } else {
                                val message = jsonObject.get("message")?.asString ?: "Authentication failed"
                                notifyError(message)
                            }
                        }
                        "character_created" -> {
                            val character = gson.fromJson(
                                jsonObject.getAsJsonObject("character"),
                                Character::class.java
                            )
                            notifyCharacterCreated(character)
                        }
                        "character_updated" -> {
                            val character = gson.fromJson(
                                jsonObject.getAsJsonObject("character"),
                                Character::class.java
                            )
                            notifyCharacterUpdated(character)
                        }
                        "character_deleted" -> {
                            val characterId = jsonObject.get("characterId")?.asString
                            if (characterId != null) {
                                notifyCharacterDeleted(characterId)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing WebSocket message", e)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $reason")
                webSocket.close(1000, null)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closed: $reason")
                this@WebSocketManager.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket error", t)
                this@WebSocketManager.webSocket = null
                notifyError(t.message ?: "Connection failed")
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "Disconnecting")
        webSocket = null
        listeners.clear()
    }

    private fun notifyCharacterCreated(character: Character) {
        listeners.forEach { it.onCharacterCreated(character) }
    }

    private fun notifyCharacterUpdated(character: Character) {
        listeners.forEach { it.onCharacterUpdated(character) }
    }

    private fun notifyCharacterDeleted(characterId: String) {
        listeners.forEach { it.onCharacterDeleted(characterId) }
    }

    private fun notifyError(error: String) {
        listeners.forEach { it.onConnectionError(error) }
    }
}

