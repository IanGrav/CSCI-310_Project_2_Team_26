package com.bestllm.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bestllm.data.model.Post

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE llm_tag = :tag ORDER BY id DESC")
    fun getPostsByTag(tag: String): LiveData<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: Int): LiveData<PostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()
}

