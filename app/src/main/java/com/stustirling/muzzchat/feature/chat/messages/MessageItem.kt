package com.stustirling.muzzchat.feature.chat.messages

data class MessageItem(
    val id: Int,
    val showTimeHeading: Boolean,
    val showTail: Boolean,
    val isCurrentUser: Boolean,
    val timestamp: Long,
    val content: String
)