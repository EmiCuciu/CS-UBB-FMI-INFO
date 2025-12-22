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

class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        val characterDao = AppDatabase.getDatabase(application).characterDao()
        repository = CharacterRepository(characterDao, RetrofitClient.characterApi, application.applicationContext)
    }

    fun createCharacter(name: String, balance: Double, profilePhoto: String? = null) {
        if (name.isBlank()) {
            _error.value = "Name is required"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val character = Character(
                name = name,
                balance = balance,
                profilePhoto = profilePhoto
            )
            val result = repository.createCharacter(character)
            _isLoading.value = false

            result.onSuccess {
                _saveSuccess.value = true
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to create character"
            }
        }
    }

    fun updateCharacter(id: String, name: String, balance: Double, profilePhoto: String? = null) {
        if (name.isBlank()) {
            _error.value = "Name is required"
            return
        }

        android.util.Log.d("CharacterDetailVM", "📤 Updating character $id with profilePhoto = ${if (profilePhoto == null) "NULL" else "BASE64"}")

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val character = Character(
                id = id,
                name = name,
                balance = balance,
                profilePhoto = profilePhoto
            )
            val result = repository.updateCharacter(character)
            _isLoading.value = false

            result.onSuccess {
                _saveSuccess.value = true
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to update character"
            }
        }
    }

    fun deleteCharacter(id: String, name: String, balance: Double) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            val character = Character(id = id, name = name, balance = balance)
            val result = repository.deleteCharacter(character)
            _isLoading.value = false

            result.onSuccess {
                _saveSuccess.value = true
            }.onFailure { exception ->
                _error.value = exception.message ?: "Failed to delete character"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}

