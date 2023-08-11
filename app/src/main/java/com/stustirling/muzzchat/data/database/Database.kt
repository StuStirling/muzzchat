package com.stustirling.muzzchat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stustirling.muzzchat.data.database.dao.MessageDao
import com.stustirling.muzzchat.data.database.dao.UserDao
import com.stustirling.muzzchat.data.database.entities.MessageEntity
import com.stustirling.muzzchat.data.database.entities.UserEntity

@Database(entities = [UserEntity::class, MessageEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun messageDao() : MessageDao
}