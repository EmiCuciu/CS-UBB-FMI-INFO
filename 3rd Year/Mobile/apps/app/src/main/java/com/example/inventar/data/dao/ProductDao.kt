package com.example.inventar.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.inventar.data.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY code ASC")
    fun getAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products ORDER BY code ASC")
    suspend fun getAllProductsList(): List<Product>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' LIMIT 5")
    suspend fun searchProducts(query: String): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}


