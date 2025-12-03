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
        repository = CharacterRepository(characterDao, RetrofitClient.characterApi)
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
            result.onSuccess {
                refreshCharacters()
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to delete character"
            }
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
}

