package com.example.mafia.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "characters")
data class Character(
    @PrimaryKey
    @SerializedName("_id")
    val id: String = "",

    val name: String,
    val balance: Double,
    val userId: String = "",

    // Offline-first sync fields
    val pendingSync: Boolean = false,  // True if needs to be synced to server
    val syncAction: String = "NONE"    // NONE, CREATE, UPDATE, DELETE
)

