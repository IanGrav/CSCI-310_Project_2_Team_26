package com.bestllm.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey
    val id: Int,
    val post_id: Int,
    val author_id: Int,
    val text: String,
    val author_name: String?
)

