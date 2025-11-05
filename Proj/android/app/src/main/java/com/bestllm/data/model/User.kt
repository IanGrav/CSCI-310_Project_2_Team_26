package com.bestllm.data.model

data class User(
    val id: Int,
    val username: String,
    val email: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

// Optional update fields
data class UpdateUserRequest(
    val username: String?,
    val email: String?,
    val password: String?
)

