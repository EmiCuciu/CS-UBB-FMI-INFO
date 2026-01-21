package com.example.inventar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.inventar.data.model.Item
import com.example.inventar.data.model.UploadStatus

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY id DESC")
    fun getAllItems(): LiveData<List<Item>>

    @Insert
    suspend fun insert(item: Item): Long

    @Update
    suspend fun update(item: Item)

    @Query("UPDATE items SET uploadStatus = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: UploadStatus)

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    @Query("SELECT * FROM items WHERE uploadStatus = :status")
    suspend fun getItemsByStatus(status: UploadStatus): List<Item>
}

