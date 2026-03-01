package com.example.mafia.data.repository

import android.content.Context
import android.util.Log
import com.example.mafia.data.datastore.AuthDataStore
import com.example.mafia.data.model.LoginRequest
import com.example.mafia.data.model.User
import com.example.mafia.data.remote.AuthApi
import com.example.mafia.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class AuthRepository(
    private val context: Context,
    private val authApi: AuthApi
) {

    private val authDataStore = AuthDataStore(context)

    /**
     * Observe token changes as Flow
     */
    val tokenFlow: Flow<String?> = authDataStore.tokenFlow

    /**
     * Observe username changes as Flow
     */
    val usernameFlow: Flow<String?> = authDataStore.usernameFlow

    suspend fun saveToken(token: String, username: String) {
        authDataStore.saveToken(token)
        authDataStore.saveUsername(username)
        RetrofitClient.setToken(token)
    }

    suspend fun getToken(): String? {
        return authDataStore.tokenFlow.firstOrNull()
    }

    suspend fun clearToken() {
        authDataStore.clearAuth()
        RetrofitClient.setToken(null)
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    suspend fun login(username: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    saveToken(loginResponse.token, username)
                    Result.success(loginResponse.token)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login error", e)
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.register(User(username, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    saveToken(loginResponse.token, username)
                    Result.success(loginResponse.token)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Registration error", e)
            Result.failure(e)
        }
    }

    suspend fun logout() {
        clearToken()
    }
}



