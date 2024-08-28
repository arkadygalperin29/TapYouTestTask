package com.dev.agalperin.di

import com.dev.agalperin.data.TapYouRepositoryImpl
import com.dev.agalperin.domain.TapYouRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTapYouRepository(
        tapYouRepositoryImpl: TapYouRepositoryImpl
    ): TapYouRepository
}