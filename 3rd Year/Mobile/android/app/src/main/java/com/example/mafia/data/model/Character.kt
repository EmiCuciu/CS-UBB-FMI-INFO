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
    val userId: String = ""
)

