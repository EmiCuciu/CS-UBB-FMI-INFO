package com.example.sesiune.data

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage(context: Context) {
    private val prefs = context.getSharedPreferences("audit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveZone(zone: String) {
        prefs.edit {
            putString("zone", zone)
        }
    }

    fun getZone(): String? {
        return prefs.getString("zone", null)
    }

    fun saveProducts(products: List<Product>) {
        val json = gson.toJson(products)
        prefs.edit {
            putString("products", json)
        }
    }

    fun getProducts(): List<Product>? {
        val json = prefs.getString("products", null) ?: return null
        val type = object : TypeToken<List<Product>>() {}.type
        return gson.fromJson(json, type)
    }

    fun hasReceivedInventory(): Boolean {
        return prefs.getBoolean("inventory_received", false)
    }

    fun setInventoryReceived(received: Boolean) {
        prefs.edit {
            putBoolean("inventory_received", received)
        }
    }
}

