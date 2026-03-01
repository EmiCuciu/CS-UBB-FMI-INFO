package com.example.sesiune.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sesiune.data.AuditRequest
import com.example.sesiune.data.ErrorResponse
import com.example.sesiune.data.LocalStorage
import com.example.sesiune.data.Product
import com.example.sesiune.network.RetrofitClient
import com.example.sesiune.network.WebSocketManager
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.IOException

class AuditViewModel(application: Application) : AndroidViewModel(application) {
    private val localStorage = LocalStorage(application)
    private val webSocketManager = WebSocketManager()
    private val apiService = RetrofitClient.apiService

    private val _zone = MutableLiveData<String?>()
    val zone: LiveData<String?> = _zone

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _showDiscrepanciesOnly = MutableLiveData<Boolean>(false)
    val showDiscrepanciesOnly: LiveData<Boolean> = _showDiscrepanciesOnly

    init {
        _zone.value = localStorage.getZone()
        loadProducts()
    }

    fun setZone(zone: String) {
        localStorage.saveZone(zone)
        _zone.value = zone
        loadProductsFromServer()
    }

    private fun loadProducts() {
        val savedProducts = localStorage.getProducts()
        if (savedProducts != null && savedProducts.isNotEmpty()) {
            _products.value = savedProducts.toList()
        } else if (localStorage.hasReceivedInventory()) {
            _products.value = emptyList()
        } else if (_zone.value != null) {
            loadProductsFromServer()
        }
    }

    fun loadProductsFromServer() {
        val currentZone = _zone.value
        if (currentZone == null) {
            _error.value = "Zone is not set"
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            webSocketManager.connectAndReceiveProducts()
                .catch { e ->
                    _isLoading.value = false
                    _error.value = "Failed to connect to server: ${e.message}"
                }
                .collect { result ->
                    _isLoading.value = false
                    result.onSuccess { receivedProducts ->
                        val existingProducts = _products.value ?: emptyList()
                        val mergedProducts = receivedProducts.map { newProduct ->
                            val existing = existingProducts.find { it.code == newProduct.code }
                            if (existing != null) {
                                newProduct.copy(
                                    counted = existing.counted,
                                    isSubmitted = existing.isSubmitted,
                                    hasError = existing.hasError
                                )
                            } else {
                                newProduct
                            }
                        }
                        _products.value = mergedProducts
                        localStorage.setInventoryReceived(true)
                        localStorage.saveProducts(mergedProducts)
                    }.onFailure { e ->
                        _error.value = "Failed to receive products: ${e.message}"
                    }
                }
        }
    }

    fun updateProductCount(code: Int, count: Int?) {
        val currentProducts = _products.value ?: return
        val updatedProducts = currentProducts.map { product ->
            if (product.code == code) {
                product.copy(counted = count, hasError = false, isSubmitted = false)
            } else {
                product
            }
        }
        _products.value = updatedProducts
        localStorage.saveProducts(updatedProducts)
    }

    fun toggleFilter() {
        _showDiscrepanciesOnly.value = !(_showDiscrepanciesOnly.value ?: false)
    }

    fun getFilteredProducts(): List<Product> {
        val allProducts = _products.value ?: emptyList()
        return if (_showDiscrepanciesOnly.value == true) {
            allProducts.filter { product ->
                product.counted != null && product.counted != product.quantity
            }
        } else {
            allProducts
        }
    }

    fun submitAudits() {
        val currentZone = _zone.value ?: return
        val currentProducts = _products.value ?: return

        val productsToSubmit = currentProducts.filter { product ->
            product.counted != null && !product.isSubmitted
        }

        if (productsToSubmit.isEmpty()) {
            _error.value = "No products to submit"
            return
        }

        viewModelScope.launch {
            val updatedProducts = currentProducts.toMutableList()

            val jobs = productsToSubmit.map { product ->
                async {
                    try {
                        // Mark as submitting
                        val index = updatedProducts.indexOfFirst { it.code == product.code }
                        if (index != -1) {
                            updatedProducts[index] = updatedProducts[index].copy(isSubmitting = true)
                            _products.postValue(updatedProducts.toList())
                        }

                        val request = AuditRequest(
                            code = product.code,
                            counted = product.counted!!,
                            zone = currentZone
                        )
                        val response = apiService.submitAudit(request)

                        // Update status based on response
                        val idx = updatedProducts.indexOfFirst { it.code == product.code }
                        if (idx != -1) {
                            if (response.isSuccessful) {
                                updatedProducts[idx] = updatedProducts[idx].copy(
                                    isSubmitted = true,
                                    hasError = false,
                                    isSubmitting = false
                                )
                            } else {
                                // Parse error message from server
                                val errorMessage = try {
                                    val errorBody = response.errorBody()?.string()
                                    if (errorBody != null) {
                                        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                                        errorResponse.text ?: "Server error: ${response.code()}"
                                    } else {
                                        "Server error: ${response.code()}"
                                    }
                                } catch (e: Exception) {
                                    "Server error: ${response.code()}"
                                }

                                updatedProducts[idx] = updatedProducts[idx].copy(
                                    hasError = true,
                                    isSubmitting = false
                                )
                                _error.postValue(errorMessage)
                            }
                        }
                    } catch (e: IOException) {
                        // Network errors (connection failed, timeout, etc.)
                        val idx = updatedProducts.indexOfFirst { it.code == product.code }
                        if (idx != -1) {
                            updatedProducts[idx] = updatedProducts[idx].copy(
                                hasError = true,
                                isSubmitting = false
                            )
                        }
                        _error.postValue("Network error: No connection to server")
                    } catch (e: Exception) {
                        // Other unexpected errors
                        val idx = updatedProducts.indexOfFirst { it.code == product.code }
                        if (idx != -1) {
                            updatedProducts[idx] = updatedProducts[idx].copy(
                                hasError = true,
                                isSubmitting = false
                            )
                        }
                        _error.postValue("Unexpected error: ${e.message}")
                    }
                }
            }

            jobs.awaitAll()
            _products.value = updatedProducts
            localStorage.saveProducts(updatedProducts)
        }
    }

    fun clearError() {
        _error.value = null
    }
}

