package com.example.inventar.data.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response as OkHttpResponse

class WebSocketManager(private val listener: WebSocketEventListener) {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    interface WebSocketEventListener {
        fun onProductListChanged()
        fun onConnected()
        fun onDisconnected()
    }

    fun connect() {
        // Pentru EMULATOR Android folosește: 10.0.2.2
        // Pentru DEVICE FIZIC folosește IP-ul calculatorului din aceeași rețea
        // Pentru USB TETHERING folosește IP-ul calculatorului din subnet-ul tethering

        val request = Request.Builder()
            // .url("ws://10.0.2.2:3000") // Android emulator localhost
            // .url("ws://10.99.138.25:3000") // Wi-Fi - pentru device fizic
            .url("ws://10.231.147.116:3000") // Ethernet 2 / USB Tethering - pentru device fizic
            // .url("ws://10.231.147.206:3000") // Gateway Ethernet 2 - pentru USB tethering phone
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: OkHttpResponse) {
                Log.d("WebSocket", "Connected")
                listener.onConnected()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")
                if (text.contains("PRODUCT_LIST_CHANGED")) {
                    listener.onProductListChanged()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing: $code $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closed: $code $reason")
                listener.onDisconnected()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: OkHttpResponse?) {
                Log.e("WebSocket", "Error: ${t.message}")
                listener.onDisconnected()
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
    }
}

