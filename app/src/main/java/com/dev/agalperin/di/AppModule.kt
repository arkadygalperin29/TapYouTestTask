package com.dev.agalperin.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dev.agalperin.BuildConfig
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import com.dev.core.AppDispatchers
import com.dev.tapyouapi.TapYouApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context): RequestManager {
        return Glide.with(context)
            .setDefaultRequestOptions(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
            )
    }

    @Provides
    @Singleton
    fun provideAppCoroutinesDispatchers(): AppDispatchers {
        return AppDispatchers()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}