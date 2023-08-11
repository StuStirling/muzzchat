package com.stustirling.muzzchat.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.stustirling.muzzchat.data.database.entities.UserEntity
import com.stustirling.muzzchat.data.database.entities.UsersTableName
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM $UsersTableName")
    fun getUsers() : Flow<List<UserEntity>>
}