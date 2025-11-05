package com.bestllm.data.repository

import android.content.Context
import com.bestllm.data.api.RetrofitClient
import com.bestllm.data.model.AuthResponse
import com.bestllm.data.model.LoginRequest
import com.bestllm.data.model.RegisterRequest
import com.bestllm.data.preferences.AuthPreferences

class AuthRepository(context: Context) {
    private val authPreferences = AuthPreferences(context)
    private val apiService = RetrofitClient.apiService

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                authPreferences.saveToken(authResponse.token)
                authPreferences.saveUserId(authResponse.user.id)
                authPreferences.saveUsername(authResponse.user.username)
                RetrofitClient.setToken(authResponse.token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception(response.message() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(username, email, password))
            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!
                authPreferences.saveToken(authResponse.token)
                authPreferences.saveUserId(authResponse.user.id)
                authPreferences.saveUsername(authResponse.user.username)
                RetrofitClient.setToken(authResponse.token)
                Result.success(authResponse)
            } else {
                Result.failure(Exception(response.message() ?: "Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? {
        return authPreferences.getToken()
    }

    fun logout() {
        authPreferences.clearAll()
        RetrofitClient.setToken(null)
    }

    fun isLoggedIn(): Boolean {
        val token = authPreferences.getToken()
        if (token != null) {
            RetrofitClient.setToken(token)
        }
        return token != null
    }
}

