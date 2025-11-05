package com.bestllm.data.repository

import android.content.Context
import com.bestllm.data.api.RetrofitClient
import com.bestllm.data.database.AppDatabase
import com.bestllm.data.database.CommentEntity
import com.bestllm.data.model.Comment
import com.bestllm.data.model.CreateCommentRequest

class CommentRepository(context: Context) {
    private val apiService = RetrofitClient.apiService
    private val commentDao = AppDatabase.getDatabase(context).commentDao()

    suspend fun getComments(postId: Int): List<Comment> {
        return try {
            val response = apiService.getComments(postId)
            if (response.isSuccessful && response.body() != null) {
                val comments = response.body()!!
                // Save to Room
                val entities = comments.map { comment ->
                    CommentEntity(
                        id = comment.id,
                        post_id = comment.post_id,
                        author_id = comment.author_id,
                        text = comment.text,
                        author_name = comment.author_name
                    )
                }
                commentDao.insertComments(entities)
                comments
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getCommentsLiveData(postId: Int) = commentDao.getCommentsByPostId(postId)

    suspend fun refreshComments(postId: Int) {
        try {
            val response = apiService.getComments(postId)
            if (response.isSuccessful && response.body() != null) {
                val comments = response.body()!!
                val entities = comments.map { comment ->
                    CommentEntity(
                        id = comment.id,
                        post_id = comment.post_id,
                        author_id = comment.author_id,
                        text = comment.text,
                        author_name = comment.author_name
                    )
                }
                commentDao.insertComments(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun createComment(postId: Int, text: String): Result<Comment> {
        return try {
            val response = apiService.createComment(postId, CreateCommentRequest(text))
            if (response.isSuccessful && response.body() != null) {
                val comment = response.body()!!
                // Save to Room
                val entity = CommentEntity(
                    id = comment.id,
                    post_id = comment.post_id,
                    author_id = comment.author_id,
                    text = comment.text,
                    author_name = comment.author_name
                )
                commentDao.insertComment(entity)
                Result.success(comment)
            } else {
                Result.failure(Exception(response.message() ?: "Failed to create comment"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

