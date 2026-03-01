package com.example.mafia.utils

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    fun decodeToken(token: String): String? {
        try {
            val parts = token.split(".")
            if (parts.size != 3) return null

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val jsonObject = JSONObject(payload)

            return jsonObject.optString("username", null)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}

