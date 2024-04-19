package com.example.data.repositories

import com.example.data.ExceptionUtils
import com.example.data.api.RetrofitService
import com.example.data.dto.request.LoginWithPhoneRequest
import com.example.data.dto.response.BaseResponse
import com.example.data.mappers.AuthMapper
import com.example.domain.entity.BaseEntity
import com.example.domain.repositories.AuthRepository
import com.google.gson.Gson

class AuthRepositoryImpl(
    private val apiService: RetrofitService,
    private val authMapper: AuthMapper,
) : AuthRepository {

    override suspend fun loginWithPhone(phone: String): BaseEntity {
        return try {
            val loginWithPhoneResponse = apiService.loginWithPhone(LoginWithPhoneRequest(phone))

            if (loginWithPhoneResponse.isSuccessful) {
                authMapper.toBaseEntity(loginWithPhoneResponse.body()!!)
            } else {
                val errorResponse = Gson().fromJson(
                    loginWithPhoneResponse.errorBody()?.string(),
                    BaseResponse::class.java
                )
                authMapper.toBaseEntity(errorResponse)
            }
        } catch (e: Exception) {
            return BaseEntity(code = ExceptionUtils.getExceptionCode(e), message = e.message)
        }
    }

}