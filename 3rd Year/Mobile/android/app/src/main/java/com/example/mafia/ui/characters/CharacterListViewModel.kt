package com.example.mafia.ui.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mafia.data.local.AppDatabase
import com.example.mafia.data.model.Character
import com.example.mafia.data.remote.RetrofitClient
import com.example.mafia.data.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository
    val characters: LiveData<List<Character>>

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        val characterDao = AppDatabase.getDatabase(application).characterDao()
        repository = CharacterRepository(characterDao, RetrofitClient.characterApi, application.applicationContext)
        characters = repository.allCharacters

        refreshCharacters()
    }

    fun refreshCharacters() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val result = repository.refreshCharacters()
            _isLoading.value = false

            result.onFailure { exception ->
                _error.value = exception.message ?: "Failed to load characters"
            }
        }
    }

    fun deleteCharacter(character: Character) {
        viewModelScope.launch {
            val result = repository.deleteCharacter(character)
            result.onFailure { exception ->
                _error.value = exception.message ?: "Failed to delete character"
            }
            // No refresh needed - Room LiveData updates automatically
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun clearLocalData() {
        viewModelScope.launch {
            repository.clearAllLocalData()
        }
    }

    /**
     * Insert/update character from WebSocket
     * Marks as synced (not pending)
     */
    fun insertFromWebSocket(character: Character) {
        viewModelScope.launch {
            repository.insertFromWebSocket(character)
        }
    }

    /**
     * Delete character from WebSocket
     * Only deletes if not pending sync locally
     */
    fun deleteFromWebSocket(characterId: String) {
        viewModelScope.launch {
            repository.deleteFromWebSocket(characterId)
        }
    }
}

