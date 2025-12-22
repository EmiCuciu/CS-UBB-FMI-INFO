package com.example.mafia.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Network connectivity monitor using callbackFlow
 * Provides reactive network status updates
 */
class NetworkConnectivityObserver(private val context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        private const val TAG = "NetworkConnectivity"
    }

    /**
     * Data class representing network status
     */
    data class NetworkStatus(
        val isConnected: Boolean,
        val hasInternet: Boolean,
        val networkType: NetworkType
    )

    enum class NetworkType {
        WIFI,
        CELLULAR,
        ETHERNET,
        NONE
    }

    /**
     * Observe network connectivity changes as a Flow
     * Uses callbackFlow to convert callback-based API to Flow
     */
    fun observeNetworkStatus(): Flow<NetworkStatus> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            private val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                Log.d(TAG, "Network available: $network")
                networks.add(network)
                trySend(getCurrentNetworkStatus())
            }

            override fun onLost(network: Network) {
                Log.d(TAG, "Network lost: $network")
                networks.remove(network)
                trySend(getCurrentNetworkStatus())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                Log.d(TAG, "Network capabilities changed: $network")
                trySend(getCurrentNetworkStatus())
            }

            override fun onUnavailable() {
                Log.d(TAG, "Network unavailable")
                trySend(
                    NetworkStatus(
                        isConnected = false,
                        hasInternet = false,
                        networkType = NetworkType.NONE
                    )
                )
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Send initial status
        trySend(getCurrentNetworkStatus())

        // Wait for flow collection to be cancelled
        awaitClose {
            Log.d(TAG, "Unregistering network callback")
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged() // Only emit when status actually changes

    /**
     * Get current network status synchronously
     */
    fun getCurrentNetworkStatus(): NetworkStatus {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return if (networkCapabilities != null) {
            val hasInternet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            val networkType = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
                else -> NetworkType.NONE
            }

            NetworkStatus(
                isConnected = true,
                hasInternet = hasInternet,
                networkType = networkType
            )
        } else {
            NetworkStatus(
                isConnected = false,
                hasInternet = false,
                networkType = NetworkType.NONE
            )
        }
    }

    /**
     * Simple check if device is currently connected to internet
     */
    fun isConnected(): Boolean {
        return getCurrentNetworkStatus().hasInternet
    }
}

