package com.example.inventar.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inventar.data.model.Product
import com.example.inventar.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(application)

    val allProducts: LiveData<List<Product>> = repository.allProducts

    private val _downloadState = MutableLiveData<DownloadState>()
    val downloadState: LiveData<DownloadState> = _downloadState

    private val _searchResults = MutableLiveData<List<Product>>()
    val searchResults: LiveData<List<Product>> = _searchResults

    private var searchJob: Job? = null

    init {
        // Check if previous download was successful
        if (!repository.isDownloadSuccessful()) {
            _downloadState.value = DownloadState.NotStarted
        } else {
            _downloadState.value = DownloadState.Success
        }
    }

    fun startDownload() {
        viewModelScope.launch {
            _downloadState.value = DownloadState.Downloading(0, 1)

            val result = repository.downloadProducts { current, total ->
                _downloadState.postValue(DownloadState.Downloading(current, total))
            }

            if (result.isSuccess) {
                _downloadState.value = DownloadState.Success
            } else {
                _downloadState.value = DownloadState.Failed(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun searchProducts(query: String) {
        searchJob?.cancel()

        if (query.isEmpty()) {
            _searchResults.value = emptyList()
            return
        }

        searchJob = viewModelScope.launch {
            delay(2000) // 2 second debounce
            val results = repository.searchProducts(query)
            _searchResults.value = results
        }
    }

    fun resetDownloadState() {
        repository.resetDownloadState()
        _downloadState.value = DownloadState.NotStarted
    }

    fun isDownloadSuccessful(): Boolean {
        return repository.isDownloadSuccessful()
    }
}

sealed class DownloadState {
    object NotStarted : DownloadState()
    data class Downloading(val currentPage: Int, val totalPages: Int) : DownloadState()
    object Success : DownloadState()
    data class Failed(val message: String) : DownloadState()
}

