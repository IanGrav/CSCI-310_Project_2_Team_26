package com.bestllm.data.preferences

import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "bestllm_prefs",
        Context.MODE_PRIVATE
    )

    private val TOKEN_KEY = "auth_token"
    private val USER_ID_KEY = "user_id"
    private val USERNAME_KEY = "username"

    fun saveToken(token: String) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        prefs.edit().remove(TOKEN_KEY).apply()
    }

    fun saveUserId(userId: Int) {
        prefs.edit().putInt(USER_ID_KEY, userId).apply()
    }

    fun getUserId(): Int {
        return prefs.getInt(USER_ID_KEY, -1)
    }

    fun saveUsername(username: String) {
        prefs.edit().putString(USERNAME_KEY, username).apply()
    }

    fun getUsername(): String? {
        return prefs.getString(USERNAME_KEY, null)
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }
}

