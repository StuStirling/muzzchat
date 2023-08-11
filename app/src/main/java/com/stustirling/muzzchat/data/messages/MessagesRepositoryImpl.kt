package com.stustirling.muzzchat.data.messages

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.data.database.dao.MessageDao
import com.stustirling.muzzchat.data.database.entities.MessageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageEntityMapper: MessageEntityMapper
) : MessagesRepository {

    override fun getMessages(participants: Set<String>): Flow<List<Message>> =
        messageDao.getMessages(participants)
            .map { messages -> messages.map { messageEntityMapper.mapEntityToDomain(it) } }

    override suspend fun sendMessage(
        authorId: String,
        recipientId: String,
        message: String,
        timestamp: Long
    ) {
        messageDao.insertMessage(
            MessageEntity(
                authorId = authorId,
                recipientId = recipientId,
                content = message,
                timestamp = timestamp
            )
        )
    }
}