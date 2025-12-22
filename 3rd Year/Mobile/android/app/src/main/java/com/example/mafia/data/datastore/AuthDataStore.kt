package com.example.mafia.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create DataStore instance
private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

/**
 * DataStore implementation for storing authentication data
 * This is a modern replacement for SharedPreferences with better performance and type safety
 */
class AuthDataStore(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    /**
     * Get the stored token as a Flow
     * This allows observing token changes reactively
     */
    val tokenFlow: Flow<String?> = context.authDataStore.data
        .map { preferences: Preferences ->
            preferences[TOKEN_KEY]
        }

    /**
     * Get the stored username as a Flow
     */
    val usernameFlow: Flow<String?> = context.authDataStore.data
        .map { preferences ->
            preferences[USERNAME_KEY]
        }

    /**
     * Save authentication token
     */
    suspend fun saveToken(token: String) {
        context.authDataStore.edit { preferences: MutablePreferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    /**
     * Save username
     */
    suspend fun saveUsername(username: String) {
        context.authDataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    /**
     * Clear all authentication data
     */
    suspend fun clearAuth() {
        context.authDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(USERNAME_KEY)
        }
    }

    /**
     * Get token synchronously (for compatibility with existing code)
     */
    suspend fun getToken(): String? {
        var token: String? = null
        context.authDataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.collect {
            token = it
        }
        return token
    }

    /**
     * Get username synchronously
     */
    suspend fun getUsername(): String? {
        var username: String? = null
        context.authDataStore.data.map { preferences ->
            preferences[USERNAME_KEY]
        }.collect {
            username = it
        }
        return username
    }
}
