package com.dev.agalperin.di

import com.dev.agalperin.BuildConfig
import com.dev.tapyouapi.TapYouApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTapYouApi(): TapYouApi {
        return TapYouApi(
            baseUrl = BuildConfig.TAP_YOU_TEST_API_BASE_URL
        )
    }
}