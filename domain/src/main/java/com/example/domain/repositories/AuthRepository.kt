package com.example.domain.repositories

import com.example.domain.entity.BaseEntity


interface AuthRepository {

    /**
     * Login with phone number. This will send otp to that phone
     * @return BaseEntity
     */
    suspend fun loginWithPhone(phone: String): BaseEntity

}