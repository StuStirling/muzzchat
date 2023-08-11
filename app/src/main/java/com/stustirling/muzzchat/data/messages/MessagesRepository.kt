package com.stustirling.muzzchat.data.messages

import com.stustirling.muzzchat.core.model.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    fun getMessages(
        participants: Set<String>
    ): Flow<List<Message>>

    suspend fun sendMessage(
        authorId: String,
        recipientId: String,
        message: String,
        timestamp: Long
    )
}