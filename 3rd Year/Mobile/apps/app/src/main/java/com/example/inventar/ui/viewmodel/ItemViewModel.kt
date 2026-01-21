package com.example.inventar.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventar.data.model.Item
import com.example.inventar.data.repository.ItemRepository
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ItemRepository(application)

    val allItems: LiveData<List<Item>> = repository.allItems

    private val _uploadState = MutableLiveData<UploadState>()
    val uploadState: LiveData<UploadState> = _uploadState

    fun addItem(code: Int, quantity: Int) {
        viewModelScope.launch {
            repository.addItem(code, quantity)
        }
    }

    fun uploadItems() {
        viewModelScope.launch {
            _uploadState.value = UploadState.Uploading

            val result = repository.uploadItems { item, success ->
                _uploadState.postValue(UploadState.Progress(item, success))
            }

            if (result.isSuccess) {
                _uploadState.value = UploadState.Complete
            } else {
                _uploadState.value = UploadState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun hasItemsToUpload(callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val hasItems = repository.hasItemsToUpload()
            callback(hasItems)
        }
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Progress(val item: Item, val success: Boolean) : UploadState()
    object Complete : UploadState()
    data class Error(val message: String) : UploadState()
}

