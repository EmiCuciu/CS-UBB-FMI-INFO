package com.example.mafia.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mafia.data.datastore.AuthDataStore
import com.example.mafia.data.local.CharacterDao
import com.example.mafia.data.model.Character
import com.example.mafia.data.remote.CharacterApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val characterApi: CharacterApi,
    private val context: Context
) {

    companion object {
        private const val TAG = "CharacterRepository"
    }

    private val authDataStore = AuthDataStore(context)

    val allCharacters: LiveData<List<Character>> = characterDao.getAllCharacters()

    /**
     * Refresh characters from server (for pull-to-refresh)
     */
    suspend fun refreshCharacters(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = characterApi.getAllCharacters()
            if (response.isSuccessful) {
                response.body()?.let { characters ->
                    // Get pending changes FIRST
                    val pending = characterDao.getPendingSyncCharacters()
                    val pendingIds = pending.map { it.id }.toSet()
                    Log.d(TAG, "Found ${pending.size} pending items: $pendingIds")

                    // Delete ONLY synced items (not pending)
                    characterDao.deleteSyncedCharacters()

                    // Insert server data, but SKIP items with pending changes
                    val serverCharacters = characters
                        .filter { !pendingIds.contains(it.id) }  // Skip pending
                        .map { it.copy(pendingSync = false, syncAction = "NONE") }

                    characterDao.insertAll(serverCharacters)
                    Log.d(TAG, "Refreshed ${serverCharacters.size} synced items from server")

                    // Pending items stay untouched with their local changes
                    Log.d(TAG, "Preserved ${pending.size} pending items with local changes")

                    Result.success(Unit)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error refreshing characters", e)
            Result.failure(e)
        }
    }

    /**
     * Create character - Try server first, fallback to offline
     */
    suspend fun createCharacter(character: Character): Result<Character> = withContext(Dispatchers.IO) {
        try {
            // Try to create on server immediately
            val response = characterApi.createCharacter(character)

            if (response.isSuccessful && response.body() != null) {
                // SUCCESS - Save server version locally
                val serverCharacter = response.body()!!.copy(pendingSync = false, syncAction = "NONE")
                characterDao.insert(serverCharacter)
                Log.d(TAG, "Character created on server: ${serverCharacter.id}")
                Result.success(serverCharacter)
            } else {
                // Server error - save offline
                throw Exception("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            // Network error or server down - OFFLINE MODE
            Log.d(TAG, "Server unavailable, saving offline: ${e.message}")

            // Get userId from DataStore
            val userId = authDataStore.usernameFlow.first() ?: ""
            Log.d(TAG, "Using userId from DataStore: $userId")

            val localId = "temp_${UUID.randomUUID()}"
            val localCharacter = character.copy(
                id = localId,
                userId = userId,  // ✅ FIX: Set userId from DataStore
                pendingSync = true,
                syncAction = "CREATE"
            )

            characterDao.insert(localCharacter)
            Log.d(TAG, "Character saved offline with id=$localId, userId=$userId")
            Log.d(TAG, "✅ ROOM DATABASE STATE:")
            Log.d(TAG, "   id=${localCharacter.id}")
            Log.d(TAG, "   name=${localCharacter.name}")
            Log.d(TAG, "   balance=${localCharacter.balance}")
            Log.d(TAG, "   userId=${localCharacter.userId}")
            Log.d(TAG, "   pendingSync=${localCharacter.pendingSync}")
            Log.d(TAG, "   syncAction=${localCharacter.syncAction}")

            Result.success(localCharacter)
        }
    }

    /**
     * Update character - Try server first, fallback to offline
     */
    suspend fun updateCharacter(character: Character): Result<Character> = withContext(Dispatchers.IO) {
        try {
            // Don't sync temp IDs to server
            if (character.id.startsWith("temp_")) {
                // Just update locally
                val updated = character.copy(pendingSync = true, syncAction = "CREATE")
                characterDao.update(updated)
                Log.d(TAG, "Temp character updated locally: ${character.id}")
                return@withContext Result.success(updated)
            }

            // Try to update on server immediately
            val response = characterApi.updateCharacter(character.id, character)

            if (response.isSuccessful && response.body() != null) {
                // SUCCESS - Save server version locally
                val serverCharacter = response.body()!!.copy(pendingSync = false, syncAction = "NONE")
                characterDao.update(serverCharacter)
                Log.d(TAG, "Character updated on server: ${serverCharacter.id}")
                Result.success(serverCharacter)
            } else {
                throw Exception("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            // Network error - OFFLINE MODE
            Log.d(TAG, "Server unavailable, saving update offline: ${e.message}")

            // Get userId from DataStore
            val userId = authDataStore.usernameFlow.first() ?: character.userId

            val localCharacter = character.copy(
                userId = userId,  // ✅ FIX: Ensure userId is set
                pendingSync = true,
                syncAction = "UPDATE"
            )

            characterDao.update(localCharacter)
            Log.d(TAG, "Character update saved offline: ${character.id}, userId=$userId")
            Log.d(TAG, "✅ ROOM DATABASE STATE:")
            Log.d(TAG, "   id=${localCharacter.id}")
            Log.d(TAG, "   name=${localCharacter.name}")
            Log.d(TAG, "   balance=${localCharacter.balance}")
            Log.d(TAG, "   userId=${localCharacter.userId}")
            Log.d(TAG, "   pendingSync=${localCharacter.pendingSync}")
            Log.d(TAG, "   syncAction=${localCharacter.syncAction}")

            Result.success(localCharacter)
        }
    }

    /**
     * Delete character - Try server first, fallback to offline
     */
    suspend fun deleteCharacter(character: Character): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Temp IDs were never on server - just delete locally
            if (character.id.startsWith("temp_")) {
                characterDao.delete(character)
                Log.d(TAG, "Temp character deleted locally: ${character.id}")
                return@withContext Result.success(Unit)
            }

            // Try to delete on server immediately
            val response = characterApi.deleteCharacter(character.id)

            if (response.isSuccessful) {
                // SUCCESS - Delete locally too
                characterDao.delete(character)
                Log.d(TAG, "Character deleted on server: ${character.id}")
                Result.success(Unit)
            } else {
                throw Exception("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            // Network error - OFFLINE MODE
            Log.d(TAG, "Server unavailable, marking for deletion: ${e.message}")

            val deletedCharacter = character.copy(
                pendingSync = true,
                syncAction = "DELETE"
            )

            characterDao.update(deletedCharacter)
            Log.d(TAG, "Character marked for deletion offline: ${character.id}")
            Log.d(TAG, "✅ ROOM DATABASE STATE:")
            Log.d(TAG, "   id=${deletedCharacter.id}")
            Log.d(TAG, "   name=${deletedCharacter.name}")
            Log.d(TAG, "   pendingSync=${deletedCharacter.pendingSync}")
            Log.d(TAG, "   syncAction=${deletedCharacter.syncAction}")

            Result.success(Unit)
        }
    }

    /**
     * Sync all pending changes to server
     * Called by WorkManager when network is available
     */
    suspend fun syncPendingChanges(): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val pendingCharacters = characterDao.getPendingSyncCharacters()
            Log.d(TAG, "📤 Syncing ${pendingCharacters.size} pending characters")

            if (pendingCharacters.isNotEmpty()) {
                Log.d(TAG, "   Pending items breakdown:")
                pendingCharacters.forEach { char ->
                    Log.d(TAG, "   - ${char.syncAction}: ${char.name} (${char.id})")
                }
            }

            var syncedCount = 0

            for (character in pendingCharacters) {
                when (character.syncAction) {
                    "CREATE" -> {
                        val result = syncCreate(character)
                        if (result.isSuccess) syncedCount++
                    }
                    "UPDATE" -> {
                        val result = syncUpdate(character)
                        if (result.isSuccess) syncedCount++
                    }
                    "DELETE" -> {
                        val result = syncDelete(character)
                        if (result.isSuccess) {
                            syncedCount++
                            Log.d(TAG, "   ✅ DELETE sync successful for ${character.id}")
                        } else {
                            Log.e(TAG, "   ❌ DELETE sync failed for ${character.id}")
                        }
                    }
                }
            }

            Log.d(TAG, "✅ Synced $syncedCount out of ${pendingCharacters.size} characters")
            Result.success(syncedCount)
        } catch (e: Exception) {
            Log.e(TAG, "❌ EXCEPTION syncing pending changes: ${e.message}", e)
            Result.failure(e)
        }
    }

    private suspend fun syncCreate(character: Character): Result<Character> {
        return try {
            // Create clean character for server (no sync fields)
            val serverCharacter = character.copy(
                id = "",  // Let server generate ID
                pendingSync = false,
                syncAction = "NONE"
            )

            val response = characterApi.createCharacter(serverCharacter)
            if (response.isSuccessful) {
                response.body()?.let { newCharacter ->
                    // Delete temp local version
                    characterDao.deleteById(character.id)

                    // Insert server version (with real ID)
                    characterDao.insert(newCharacter.copy(pendingSync = false, syncAction = "NONE"))

                    Log.d(TAG, "Synced CREATE: ${character.id} -> ${newCharacter.id}")
                    Result.success(newCharacter)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Log.e(TAG, "Failed to sync CREATE: ${response.code()}")
                Result.failure(Exception("Sync failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing CREATE", e)
            Result.failure(e)
        }
    }

    private suspend fun syncUpdate(character: Character): Result<Character> {
        return try {
            val serverCharacter = character.copy(pendingSync = false, syncAction = "NONE")

            val response = characterApi.updateCharacter(character.id, serverCharacter)
            if (response.isSuccessful) {
                response.body()?.let { updatedCharacter ->
                    // Mark as synced
                    characterDao.markSynced(character.id)

                    Log.d(TAG, "Synced UPDATE: ${character.id}")
                    Result.success(updatedCharacter)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Log.e(TAG, "Failed to sync UPDATE: ${response.code()}")
                Result.failure(Exception("Sync failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing UPDATE", e)
            Result.failure(e)
        }
    }

    private suspend fun syncDelete(character: Character): Result<Unit> {
        return try {
            Log.d(TAG, "🗑️ Attempting DELETE sync for character: ${character.id} (${character.name})")

            val response = characterApi.deleteCharacter(character.id)

            Log.d(TAG, "   DELETE API Response: ${response.code()} ${response.message()}")

            if (response.isSuccessful) {
                // Delete from local database
                characterDao.deleteById(character.id)

                Log.d(TAG, "✅ Synced DELETE: ${character.id} - Deleted from server & local DB")
                Result.success(Unit)
            } else {
                Log.e(TAG, "❌ Failed to sync DELETE: ${response.code()} - ${response.message()}")
                Log.e(TAG, "   Response body: ${response.errorBody()?.string()}")
                Result.failure(Exception("Sync failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ EXCEPTION syncing DELETE for ${character.id}: ${e.message}", e)
            Result.failure(e)
        }
    }


    suspend fun clearAllLocalData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            characterDao.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing local data", e)
            Result.failure(e)
        }
    }

    /**
     * Insert/update character from WebSocket
     * Always mark as synced (not pending)
     */
    suspend fun insertFromWebSocket(character: Character) = withContext(Dispatchers.IO) {
        try {
            val syncedCharacter = character.copy(
                pendingSync = false,
                syncAction = "NONE"
            )
            characterDao.insert(syncedCharacter)
            Log.d(TAG, "Inserted from WebSocket: ${character.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting from WebSocket", e)
        }
    }

    /**
     * Delete character from WebSocket
     * Only delete if not pending sync locally
     */
    suspend fun deleteFromWebSocket(characterId: String) = withContext(Dispatchers.IO) {
        try {
            val local = characterDao.getCharacterById(characterId)
            if (local == null || !local.pendingSync) {
                // Safe to delete - not pending locally
                characterDao.deleteById(characterId)
                Log.d(TAG, "Deleted from WebSocket: $characterId")
            } else {
                Log.d(TAG, "Skipped WebSocket delete - character has pending sync: $characterId")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting from WebSocket", e)
        }
    }
}

