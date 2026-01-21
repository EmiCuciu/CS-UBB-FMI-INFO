package com.example.inventar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inventar.data.dao.ItemDao
import com.example.inventar.data.dao.ProductDao
import com.example.inventar.data.model.Item
import com.example.inventar.data.model.Product

@Database(entities = [Product::class, Item::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InventoryDatabase::class.java,
                    "inventory_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


