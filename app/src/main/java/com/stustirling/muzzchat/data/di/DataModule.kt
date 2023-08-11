package com.stustirling.muzzchat.data.di

import com.stustirling.muzzchat.data.recipients.UsersRepository
import com.stustirling.muzzchat.data.recipients.UsersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsUserRepository(usersRepositoryImpl: UsersRepositoryImpl): UsersRepository
}