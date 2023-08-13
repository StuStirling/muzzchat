package com.stustirling.muzzchat.data.users.di

import com.stustirling.muzzchat.data.users.UsersRepository
import com.stustirling.muzzchat.data.users.UsersRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
fun interface UsersDataModule {
    @Binds
    fun bindsUserRepository(usersRepositoryImpl: UsersRepositoryImpl) : UsersRepository
}