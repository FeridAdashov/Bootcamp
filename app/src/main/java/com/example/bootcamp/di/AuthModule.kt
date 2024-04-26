package com.example.bootcamp.di

import com.example.data.api.AuthService
import com.example.data.mappers.AuthMapper
import com.example.data.repositories.AuthRepositoryImpl
import com.example.domain.interactors.AuthInteractor
import com.example.domain.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideAuthInteractor(authRepository: AuthRepository): AuthInteractor {
        return AuthInteractor(authRepository)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(apiService: AuthService, authMapper: AuthMapper): AuthRepository {
        return AuthRepositoryImpl(apiService, authMapper)
    }

    @Singleton
    @Provides
    fun provideMapper(): AuthMapper = AuthMapper()

}