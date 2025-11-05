package com.bestllm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bestllm.data.model.AuthResponse
import com.bestllm.data.model.User
import com.bestllm.data.repository.AuthRepository
import com.bestllm.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val userRepository = UserRepository(application)

    private val _authResult = MutableLiveData<Result<AuthResponse>>()
    val authResult: LiveData<Result<AuthResponse>> = _authResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = authRepository.login(username, password)
                _authResult.value = result
                result.onFailure {
                    _error.value = it.message
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = authRepository.register(username, email, password)
                _authResult.value = result
                result.onFailure {
                    _error.value = it.message
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val userId = userRepository.getCurrentUserId()
                if (userId != -1) {
                    val result = userRepository.getUser(userId)
                    result.onSuccess {
                        _currentUser.value = it
                    }.onFailure {
                        _error.value = it.message
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateCurrentUser(username: String, email: String, password: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val userId = userRepository.getCurrentUserId()
                if (userId == -1) {
                    _error.value = "Not logged in"
                    _isLoading.value = false
                    return@launch
                }
                val result = userRepository.updateUser(userId, username, email, password)
                result.onSuccess { updated ->
                    _currentUser.value = updated
                }.onFailure {
                    _error.value = it.message
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getCurrentUsername(): String? {
        return userRepository.getCurrentUsername()
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun clearError() {
        _error.value = null
    }
}

