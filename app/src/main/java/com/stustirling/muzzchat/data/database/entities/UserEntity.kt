package com.stustirling.muzzchat.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stustirling.muzzchat.model.User

internal const val UsersTableName = "users"
@Entity(tableName = UsersTableName)
data class UserEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "is_current_user") val isCurrentUser: Boolean,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?
)
