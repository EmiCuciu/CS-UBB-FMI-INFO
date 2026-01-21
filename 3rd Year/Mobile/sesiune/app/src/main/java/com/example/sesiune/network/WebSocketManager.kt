package com.example.sesiune.network

import android.util.Log
import com.example.sesiune.data.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*

class WebSocketManager {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val TAG = "WebSocketManager"

    fun connectAndReceiveProducts(): Flow<Result<List<Product>>> = callbackFlow {
        val request = Request.Builder()
            .url("ws://10.231.147.18:3000")
            .build()

        Log.d(TAG, "Connecting to WebSocket: ws://10.231.147.18:3000")

        val webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket opened successfully")
                // Serverul trimite automat lista la conexiune
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "Received message: ${text.take(200)}...")
                try {
                    val type = object : TypeToken<List<Product>>() {}.type
                    val products: List<Product> = gson.fromJson(text, type)
                    Log.d(TAG, "Parsed ${products.size} products successfully")
                    trySend(Result.success(products))
                    webSocket.close(1000, "Products received")
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing products: ${e.message}", e)
                    trySend(Result.failure(e))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket connection failed: ${t.message}", t)
                Log.e(TAG, "Response: ${response?.code} - ${response?.message}")
                trySend(Result.failure(t))
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closing: $code - $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket closed: $code - $reason")
            }
        })

        awaitClose {
            Log.d(TAG, "Flow closed, closing WebSocket")
            webSocket.close(1000, "Closing")
        }
    }
}
