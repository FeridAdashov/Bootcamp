package com.example.domain.repositories

import com.example.domain.entity.BaseEntity
import com.example.domain.entity.ConfirmOtpEntity
import com.example.domain.entity.RefreshTokenEntity


interface AuthRepository : BaseRepository {

    /**
     * Login with phone number. This will send otp to that phone
     * @return BaseEntity
     */
    suspend fun loginWithPhone(phone: String): BaseEntity


    /**
     * Confirm phone otp
     * @return ConfirmOtpEntity : access, refresh tokens
     */
    suspend fun confirmOtp(
        params: HashMap<String, String>
    ): ConfirmOtpEntity

    /**
     * Confirm register phone otp
     * @return ConfirmOtpEntity : access, refresh tokens
     */
    suspend fun confirmRegisterOtp(
        params: HashMap<String, String>
    ): ConfirmOtpEntity


    /**
     * Refresh token
     * @return RefreshTokenEntity
     */
    suspend fun getRefreshToken(refreshToken: String): RefreshTokenEntity
}