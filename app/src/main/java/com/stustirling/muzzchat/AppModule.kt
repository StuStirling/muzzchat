package com.stustirling.muzzchat

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesClock() = Clock.systemDefaultZone()
}