package com.example.domain.interactors

import com.example.domain.entity.BaseEntity
import com.example.domain.entity.RequestResult
import com.example.domain.repositories.AuthRepository

class AuthInteractor(private val repository: AuthRepository) : BaseInteractor() {

    suspend fun loginWithPhone(phoneNumber: String): RequestResult<BaseEntity> {
        return generateResult(repository.loginWithPhone(phoneNumber))
    }

    suspend fun loginWithGmail(gmail: String): RequestResult<BaseEntity> {
        TODO("Not yet implemented")
        //        return generateResult(repository.loginWithGmail(gmail))
    }
}