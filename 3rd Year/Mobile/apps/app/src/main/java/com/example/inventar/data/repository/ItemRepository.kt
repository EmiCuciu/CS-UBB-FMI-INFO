package com.example.inventar.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.inventar.data.dao.ItemDao
import com.example.inventar.data.database.InventoryDatabase
import com.example.inventar.data.model.Item
import com.example.inventar.data.model.UploadStatus
import com.example.inventar.data.network.InventoryApiService
import com.example.inventar.data.network.ItemRequest
import com.example.inventar.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(context: Context) {

    private val itemDao: ItemDao = InventoryDatabase.getDatabase(context).itemDao()
    private val apiService: InventoryApiService = RetrofitClient.apiService

    val allItems: LiveData<List<Item>> = itemDao.getAllItems()

    suspend fun addItem(code: Int, quantity: Int): Long = withContext(Dispatchers.IO) {
        val item = Item(code = code, quantity = quantity, uploadStatus = UploadStatus.PENDING)
        itemDao.insert(item)
    }

    suspend fun uploadItems(onProgress: (Item, Boolean) -> Unit): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val pendingItems = itemDao.getItemsByStatus(UploadStatus.PENDING)
            val failedItems = itemDao.getItemsByStatus(UploadStatus.FAILED)
            val itemsToUpload = pendingItems + failedItems

            for (item in itemsToUpload) {
                // Update status to submitting
                itemDao.updateStatus(item.id, UploadStatus.SUBMITTING)
                onProgress(item.copy(uploadStatus = UploadStatus.SUBMITTING), false)

                try {
                    val response = apiService.postItem(ItemRequest(item.code, item.quantity))
                    if (response.isSuccessful) {
                        itemDao.updateStatus(item.id, UploadStatus.SUBMITTED)
                        onProgress(item.copy(uploadStatus = UploadStatus.SUBMITTED), true)
                    } else {
                        itemDao.updateStatus(item.id, UploadStatus.FAILED)
                        onProgress(item.copy(uploadStatus = UploadStatus.FAILED), false)
                    }
                } catch (e: Exception) {
                    itemDao.updateStatus(item.id, UploadStatus.FAILED)
                    onProgress(item.copy(uploadStatus = UploadStatus.FAILED), false)
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasItemsToUpload(): Boolean = withContext(Dispatchers.IO) {
        val pending = itemDao.getItemsByStatus(UploadStatus.PENDING)
        val failed = itemDao.getItemsByStatus(UploadStatus.FAILED)
        pending.isNotEmpty() || failed.isNotEmpty()
    }
}

