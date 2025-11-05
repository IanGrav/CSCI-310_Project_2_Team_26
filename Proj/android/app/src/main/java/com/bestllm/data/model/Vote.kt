package com.bestllm.data.model

data class VoteRequest(
    val type: String // "up" or "down"
)

data class VoteResponse(
    val message: String,
    val votes: Int
)

