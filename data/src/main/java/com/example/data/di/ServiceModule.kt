package com.example.data.di

import com.example.data.Constants
import com.example.data.api.AuthService
import com.example.data.api.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideCardService(): AuthService {
        return RetrofitClient.makeRetrofitService(
            baseUrl = Constants.BASE_URL,
            mClass = AuthService::class.java
        )
    }
}