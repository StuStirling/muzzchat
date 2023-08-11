package com.stustirling.muzzchat.data.messages.di

import com.stustirling.muzzchat.data.messages.MessagesRepository
import com.stustirling.muzzchat.data.messages.MessagesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
fun interface MessagesModule {
    @Binds
    fun bindsMessagesRepository(messageRepositoryImpl: MessagesRepositoryImpl) : MessagesRepository
}