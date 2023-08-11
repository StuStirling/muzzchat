package com.stustirling.muzzchat.core.model

data class Message(
    val id: Int,
    val authorId: String,
    val recipientId: String,
    val content: String,
    val timestamp: Long
)
