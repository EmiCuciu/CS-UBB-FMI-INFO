package com.example.mafia.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.mafia.data.local.CharacterDao
import com.example.mafia.data.model.Character
import com.example.mafia.data.remote.CharacterApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
    private val characterApi: CharacterApi
) {

    val allCharacters: LiveData<List<Character>> = characterDao.getAllCharacters()

    suspend fun refreshCharacters(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = characterApi.getAllCharacters()
            if (response.isSuccessful) {
                response.body()?.let { characters ->
                    characterDao.deleteAll()
                    characterDao.insertAll(characters)
                    Result.success(Unit)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error refreshing characters", e)
            Result.failure(e)
        }
    }

    suspend fun createCharacter(character: Character): Result<Character> = withContext(Dispatchers.IO) {
        try {
            val response = characterApi.createCharacter(character)
            if (response.isSuccessful) {
                response.body()?.let { newCharacter ->
                    characterDao.insert(newCharacter)
                    Result.success(newCharacter)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error creating character", e)
            Result.failure(e)
        }
    }

    suspend fun updateCharacter(character: Character): Result<Character> = withContext(Dispatchers.IO) {
        try {
            val response = characterApi.updateCharacter(character.id, character)
            if (response.isSuccessful) {
                response.body()?.let { updatedCharacter ->
                    characterDao.update(updatedCharacter)
                    Result.success(updatedCharacter)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error updating character", e)
            Result.failure(e)
        }
    }

    suspend fun deleteCharacter(character: Character): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = characterApi.deleteCharacter(character.id)
            if (response.isSuccessful) {
                characterDao.delete(character)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error deleting character", e)
            Result.failure(e)
        }
    }

    suspend fun clearAllLocalData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            characterDao.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("CharacterRepository", "Error clearing local data", e)
            Result.failure(e)
        }
    }
}

