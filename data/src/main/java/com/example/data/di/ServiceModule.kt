package com.example.data.di

import android.content.Context
import com.example.data.Constants
import com.example.data.api.RetrofitClient
import com.example.data.api.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideCardService(@ApplicationContext appContext: Context): AuthService {
        return RetrofitClient.makeRetrofitService(
            context = appContext,
            baseUrl = Constants.BASE_URL,
            mClass = AuthService::class.java
        )
    }
}