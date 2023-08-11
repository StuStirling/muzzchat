package com.stustirling.muzzchat.testing

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.data.messages.MessagesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestMessagesRepository : MessagesRepository {

    private val messagesFlow: MutableStateFlow<List<Message>> =
        MutableStateFlow(emptyList())

    override fun getMessages(participants: Set<String>): Flow<List<Message>> =
        messagesFlow

    override suspend fun sendMessage(
        authorId: String,
        recipientId: String,
        message: String,
        timestamp: Long
    ) {
        val messages = messagesFlow.value.toMutableList()
            .apply {
                add(
                    Message(
                        id = if (isNotEmpty()) maxOf { it.id } + 1 else 0,
                        authorId = authorId,
                        recipientId = recipientId,
                        content = message,
                        timestamp = timestamp
                    )
                )
            }
        messagesFlow.emit(messages)
    }
}