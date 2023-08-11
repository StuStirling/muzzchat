package com.stustirling.muzzchat.data.database.di

import android.content.Context
import androidx.room.Room
import com.stustirling.muzzchat.data.database.Database
import com.stustirling.muzzchat.data.database.DatabaseCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext applicationContext: Context
    ) = Room.databaseBuilder(
        context = applicationContext,
        klass = Database::class.java,
        name = null
    ).addCallback(DatabaseCallback()).build()

    @Provides
    fun providesUserDao(database: Database) = database.userDao()
}