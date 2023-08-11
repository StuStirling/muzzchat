package com.stustirling.muzzchat.data.messages

import com.stustirling.muzzchat.core.model.Message
import com.stustirling.muzzchat.data.database.dao.MessageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val messageEntityMapper: MessageEntityMapper
) : MessagesRepository {

    override fun getMessages(participants: Set<String>): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        authorId: String,
        recipientId: String,
        message: String,
        timestamp: Long
    ) {
        TODO("Not yet implemented")
    }
}