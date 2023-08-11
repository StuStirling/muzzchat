package com.stustirling.muzzchat.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stustirling.muzzchat.data.database.entities.MessageEntity
import com.stustirling.muzzchat.data.database.entities.MessageTableName
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM $MessageTableName WHERE author_id IN (:participants) AND recipient_id IN (:participants)")
    fun getMessages(
        participants: Set<String>
    ): Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message: MessageEntity)
}