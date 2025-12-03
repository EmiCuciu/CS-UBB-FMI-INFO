package com.example.mafia.data.repository

import android.content.Context
import android.util.Log
import com.example.mafia.data.model.LoginRequest
import com.example.mafia.data.model.User
import com.example.mafia.data.remote.AuthApi
import com.example.mafia.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val context: Context,
    private val authApi: AuthApi
) {

    private val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
        RetrofitClient.setToken(token)
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
        RetrofitClient.setToken(null)
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    suspend fun login(username: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    saveToken(loginResponse.token)
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
                    saveToken(loginResponse.token)
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

    fun logout() {
        clearToken()
    }
}

