package com.bestllm.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val id: Int,
    val author_id: Int,
    val title: String,
    val content: String,
    val llm_tag: String?,
    val votes: Int,
    val author_name: String?
)

