package com.stustirling.muzzchat.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stustirling.muzzchat.data.database.dao.UserDao
import com.stustirling.muzzchat.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao() : UserDao
}