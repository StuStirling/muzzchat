package com.stustirling.muzzchat.data.database

import android.content.ContentValues
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.stustirling.muzzchat.data.database.entities.UsersTableName
import java.util.UUID

class DatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.insert(UsersTableName, OnConflictStrategy.IGNORE, ContentValues().apply {
            put("uid", UUID.randomUUID().toString())
            put("is_current_user", true)
            put("name", "Stu")
            put(
                "image_url",
                "https://gravatar.com/avatar/d38fbf1bb52c34557d32a1050c7c2e35?s=400&d=robohash&r=x"
            )
        })
        db.insert(UsersTableName, OnConflictStrategy.IGNORE, ContentValues().apply {
            put("uid", UUID.randomUUID().toString())
            put("is_current_user", false)
            put("name", "Sarah")
            put(
                "image_url",
                "https://gravatar.com/avatar/49d685c31efb0f1b2cfea4ff99500676?s=400&d=robohash&r=x"
            )
        })
    }
}