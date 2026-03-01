package com.example.inventar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val code: Int,
    val quantity: Int,
    val uploadStatus: UploadStatus = UploadStatus.PENDING
)

enum class UploadStatus {
    PENDING,
    SUBMITTING,
    SUBMITTED,
    FAILED
}

