package com.example.inventar.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.inventar.data.dao.ProductDao
import com.example.inventar.data.database.InventoryDatabase
import com.example.inventar.data.model.Product
import com.example.inventar.data.network.InventoryApiService
import com.example.inventar.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepository(context: Context) {

    private val productDao: ProductDao = InventoryDatabase.getDatabase(context).productDao()
    private val apiService: InventoryApiService = RetrofitClient.apiService
    private val prefs: SharedPreferences = context.getSharedPreferences("inventory_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_FAILED_PAGE = "last_failed_page"
        private const val KEY_DOWNLOAD_SUCCESS = "download_success"
    }

    val allProducts: LiveData<List<Product>> = productDao.getAllProducts()

    suspend fun searchProducts(query: String): List<Product> = withContext(Dispatchers.IO) {
        productDao.searchProducts(query)
    }

    suspend fun downloadProducts(onProgress: (Int, Int) -> Unit): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            var currentPage = getLastFailedPage()
            var totalPages = 1

            // Get first page to determine total pages
            val firstResponse = apiService.getProducts(currentPage)
            if (!firstResponse.isSuccessful) {
                saveLastFailedPage(currentPage)
                return@withContext Result.failure(Exception("Failed to download page $currentPage"))
            }

            val firstPageData = firstResponse.body()!!
            totalPages = kotlin.math.ceil(firstPageData.total / 10.0).toInt()

            if (currentPage == 0) {
                // Clear old data on fresh download
                productDao.deleteAll()
            }

            // Insert first page
            productDao.insertAll(firstPageData.products)
            onProgress(currentPage + 1, totalPages)

            // Download remaining pages
            for (page in (currentPage + 1) until totalPages) {
                val response = apiService.getProducts(page)
                if (!response.isSuccessful) {
                    saveLastFailedPage(page)
                    return@withContext Result.failure(Exception("Failed to download page $page"))
                }

                val pageData = response.body()!!
                productDao.insertAll(pageData.products)
                onProgress(page + 1, totalPages)
            }

            // Mark download as successful
            markDownloadSuccess()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isDownloadSuccessful(): Boolean {
        return prefs.getBoolean(KEY_DOWNLOAD_SUCCESS, false)
    }

    fun getLastFailedPage(): Int {
        return prefs.getInt(KEY_LAST_FAILED_PAGE, 0)
    }

    private fun saveLastFailedPage(page: Int) {
        prefs.edit().putInt(KEY_LAST_FAILED_PAGE, page).apply()
        prefs.edit().putBoolean(KEY_DOWNLOAD_SUCCESS, false).apply()
    }

    private fun markDownloadSuccess() {
        prefs.edit().putBoolean(KEY_DOWNLOAD_SUCCESS, true).apply()
        prefs.edit().putInt(KEY_LAST_FAILED_PAGE, 0).apply()
    }

    fun resetDownloadState() {
        prefs.edit().putBoolean(KEY_DOWNLOAD_SUCCESS, false).apply()
        prefs.edit().putInt(KEY_LAST_FAILED_PAGE, 0).apply()
    }
}

