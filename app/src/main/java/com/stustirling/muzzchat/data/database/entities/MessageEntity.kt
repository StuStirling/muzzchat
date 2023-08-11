package com.stustirling.muzzchat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

internal const val MessageTableName = "message"

@Entity(tableName = MessageTableName, foreignKeys = [
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["uid"],
        childColumns = ["author_id"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["uid"],
        childColumns = ["recipient_id"],
        onDelete = ForeignKey.CASCADE
    )
])
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo("author_id") val authorId: String,
    @ColumnInfo("recipient_id") val recipientId: String,
    @ColumnInfo("content") val content: String,
    @ColumnInfo("timestamp") val timestamp: Long
)
