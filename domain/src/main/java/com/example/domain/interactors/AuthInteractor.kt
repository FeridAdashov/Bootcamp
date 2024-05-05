package com.example.domain.interactors

import com.example.domain.entity.BaseEntity
import com.example.domain.entity.ConfirmOtpEntity
import com.example.domain.entity.OtpType
import com.example.domain.entity.RefreshTokenEntity
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

    suspend fun confirmOtp(
        otpType: OtpType,
        value: String,
        otpCode: String
    ): RequestResult<ConfirmOtpEntity> {
        val map = HashMap<String, String>()
        map[otpType.value] = value
        map["code"] = otpCode

        return generateResult(repository.confirmOtp(map))
    }

    suspend fun confirmRegisterOtp(
        otpType: OtpType,
        value: String,
        otpCode: String
    ): RequestResult<ConfirmOtpEntity> {
        val map = HashMap<String, String>()
        map[otpType.value] = value
        map["code"] = otpCode

        return generateResult(repository.confirmOtp(map))
    }

    suspend fun getRefreshToken(
        refreshToken: String,
    ): RequestResult<RefreshTokenEntity> {
        return generateResult(repository.getRefreshToken(refreshToken))
    }
}