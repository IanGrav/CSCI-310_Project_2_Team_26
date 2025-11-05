package com.bestllm.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bestllm.data.database.PostEntity
import com.bestllm.data.model.Post
import com.bestllm.data.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository = PostRepository(application)
    private var currentTag: String? = null

    // Posts will be accessed via getPostsLiveData()

    private val _currentPost = MutableLiveData<Post?>()
    val currentPost: LiveData<Post?> = _currentPost

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadPosts(tag: String? = null) {
        currentTag = tag
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                postRepository.refreshPosts(tag)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getPostsLiveData(): LiveData<List<PostEntity>> {
        return postRepository.getPostsLiveData(currentTag)
    }

    fun getPostById(postId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val post = postRepository.getPostById(postId)
                _currentPost.value = post
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getPostByIdLiveData(postId: Int): LiveData<PostEntity?> {
        return postRepository.getPostByIdLiveData(postId)
    }

    fun createPost(title: String, content: String, tag: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = postRepository.createPost(title, content, tag)
                result.onSuccess {
                    // Refresh posts
                    loadPosts()
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

    fun votePost(postId: Int, type: String) {
        viewModelScope.launch {
            try {
                val result = postRepository.votePost(postId, type)
                result.onSuccess {
                    // Refresh the post
                    getPostById(postId)
                }.onFailure {
                    _error.value = it.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

