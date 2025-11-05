package com.bestllm.data.repository

import android.content.Context
import com.bestllm.data.api.RetrofitClient
import com.bestllm.data.database.AppDatabase
import com.bestllm.data.database.PostEntity
import com.bestllm.data.model.CreatePostRequest
import com.bestllm.data.model.Post
import com.bestllm.data.model.VoteRequest

class PostRepository(context: Context) {
    private val apiService = RetrofitClient.apiService
    private val postDao = AppDatabase.getDatabase(context).postDao()

    // Removed - use getPostsLiveData and refreshPosts instead

    fun getPostsLiveData(tag: String? = null) = if (tag != null) {
        postDao.getPostsByTag(tag)
    } else {
        postDao.getAllPosts()
    }

    suspend fun refreshPosts(tag: String? = null) {
        try {
            val response = apiService.getPosts(tag)
            if (response.isSuccessful && response.body() != null) {
                val posts = response.body()!!
                val entities = posts.map { post ->
                    PostEntity(
                        id = post.id,
                        author_id = post.author_id,
                        title = post.title,
                        content = post.content,
                        llm_tag = post.llm_tag,
                        votes = post.votes,
                        author_name = post.author_name
                    )
                }
                postDao.insertPosts(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getPostById(postId: Int): Post? {
        return try {
            val response = apiService.getPostById(postId)
            if (response.isSuccessful && response.body() != null) {
                val post = response.body()!!
                // Save to Room
                val entity = PostEntity(
                    id = post.id,
                    author_id = post.author_id,
                    title = post.title,
                    content = post.content,
                    llm_tag = post.llm_tag,
                    votes = post.votes,
                    author_name = post.author_name
                )
                postDao.insertPost(entity)
                post
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getPostByIdLiveData(postId: Int) = postDao.getPostById(postId)

    suspend fun createPost(title: String, content: String, tag: String?): Result<Post> {
        return try {
            val response = apiService.createPost(CreatePostRequest(title, content, tag))
            if (response.isSuccessful && response.body() != null) {
                val post = response.body()!!
                // Save to Room
                val entity = PostEntity(
                    id = post.id,
                    author_id = post.author_id,
                    title = post.title,
                    content = post.content,
                    llm_tag = post.llm_tag,
                    votes = post.votes,
                    author_name = post.author_name
                )
                postDao.insertPost(entity)
                Result.success(post)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to create post"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun votePost(postId: Int, type: String): Result<Int> {
        return try {
            val response = apiService.votePost(postId, VoteRequest(type))
            if (response.isSuccessful && response.body() != null) {
                val voteResponse = response.body()!!
                // Refresh the post to get updated vote count
                // The LiveData will automatically update
                refreshPosts()
                Result.success(voteResponse.votes)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to vote"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

