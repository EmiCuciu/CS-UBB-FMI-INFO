package com.example.mafia.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mafia.data.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAllCharacters(): LiveData<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: String): Character?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: Character)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character)

    @Query("DELETE FROM characters")
    suspend fun deleteAll()

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteById(id: String)
}

