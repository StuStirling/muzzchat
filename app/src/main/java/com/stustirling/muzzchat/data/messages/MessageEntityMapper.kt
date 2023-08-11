package com.stustirling.muzzchat.data.messages

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.data.database.entities.MessageEntity
import javax.inject.Inject

class MessageEntityMapper @Inject constructor() {
    fun mapEntityToDomain(messageEntity: MessageEntity) = with(messageEntity) {
        Message(
            id = id,
            authorId = authorId,
            recipientId = recipientId,
            content = content,
            timestamp = timestamp
        )
    }
}