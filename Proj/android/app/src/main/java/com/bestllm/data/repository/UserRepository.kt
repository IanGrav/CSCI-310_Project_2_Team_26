package com.bestllm.data.repository

import android.content.Context
import com.bestllm.data.api.RetrofitClient
import com.bestllm.data.model.UpdateUserRequest
import com.bestllm.data.model.User
import com.bestllm.data.preferences.AuthPreferences

class UserRepository(context: Context) {
    private val apiService = RetrofitClient.apiService
    private val authPreferences = AuthPreferences(context)

    suspend fun getUser(userId: Int): Result<User> {
        return try {
            val response = apiService.getUser(userId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to get user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(userId: Int, username: String?, email: String?, password: String?): Result<User> {
        return try {
            val response = apiService.updateUser(userId, UpdateUserRequest(username, email, password))
            if (response.isSuccessful && response.body() != null) {
                val user = response.body()!!
                // update cached username
                user.username.let { authPreferences.saveUsername(it) }
                Result.success(user)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to update user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): Int {
        return authPreferences.getUserId()
    }

    fun getCurrentUsername(): String? {
        return authPreferences.getUsername()
    }
}

