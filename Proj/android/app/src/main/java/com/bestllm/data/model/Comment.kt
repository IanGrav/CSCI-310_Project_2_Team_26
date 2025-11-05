package com.bestllm.data.model

data class Comment(
    val id: Int,
    val post_id: Int,
    val author_id: Int,
    val text: String,
    val author_name: String?
)

data class CreateCommentRequest(
    val text: String
)

