package com.example.inventar.data.database

import androidx.room.TypeConverter
import com.example.inventar.data.model.UploadStatus

class Converters {
    @TypeConverter
    fun fromUploadStatus(value: UploadStatus): String {
        return value.name
    }

    @TypeConverter
    fun toUploadStatus(value: String): UploadStatus {
        return UploadStatus.valueOf(value)
    }
}

