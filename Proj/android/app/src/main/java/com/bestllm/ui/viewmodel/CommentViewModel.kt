package com.bestllm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bestllm.data.database.CommentEntity
import com.bestllm.data.model.Comment
import com.bestllm.data.repository.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val commentRepository = CommentRepository(application)

    private val _comments = MutableLiveData<List<CommentEntity>>()
    val comments: LiveData<List<CommentEntity>> = _comments

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadComments(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                commentRepository.refreshComments(postId)
                // Observe the LiveData from repository
                commentRepository.getCommentsLiveData(postId).observeForever { comments ->
                    _comments.value = comments
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getCommentsLiveData(postId: Int): LiveData<List<CommentEntity>> {
        return commentRepository.getCommentsLiveData(postId)
    }

    fun createComment(postId: Int, text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = commentRepository.createComment(postId, text)
                result.onSuccess {
                    // Refresh comments
                    loadComments(postId)
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

    fun clearError() {
        _error.value = null
    }
}

