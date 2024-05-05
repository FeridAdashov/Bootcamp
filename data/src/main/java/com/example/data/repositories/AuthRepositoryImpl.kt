package com.example.data.repositories

import com.example.data.api.AuthService
import com.example.data.dto.request.LoginWithPhoneRequest
import com.example.data.dto.request.RefreshTokenRequest
import com.example.data.dto.response.BaseResponse
import com.example.data.dto.response.ConfirmOtpResponse
import com.example.data.dto.response.RefreshTokenResponse
import com.example.data.mappers.AuthMapper
import com.example.domain.entity.BaseEntity
import com.example.domain.entity.ConfirmOtpEntity
import com.example.domain.entity.RefreshTokenEntity
import com.example.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val apiService: AuthService,
    private val authMapper: AuthMapper,
) : AuthRepository {

    /**
     * Old version
     */
//    override suspend fun loginWithPhone2(phone: String): BaseEntity {
//        return try {
//            val loginWithPhoneResponse = apiService.loginWithPhone(LoginWithPhoneRequest(phone))
//
//            if (loginWithPhoneResponse.isSuccessful) {
//                authMapper.toBaseEntity(loginWithPhoneResponse.body()!!)
//            } else {
//                val errorResponse = Gson().fromJson(
//                    loginWithPhoneResponse.errorBody()?.string(),
//                    BaseResponse::class.java
//                )
//                authMapper.toBaseEntity(errorResponse)
//            }
//        } catch (e: Exception) {
//            return BaseEntity(code = ExceptionUtils.getExceptionCode(e), message = e.message)
//        }
//    }

    override suspend fun loginWithPhone(phone: String): BaseEntity {
        return requestInsideCatch {
            val response = apiService.loginWithPhone(LoginWithPhoneRequest(phone))

            return@requestInsideCatch generateResultEntity(
                BaseResponse::class.java,
                response
            ) { authMapper.toBaseEntity(it) }
        }
    }

    override suspend fun confirmOtp(
        params: HashMap<String, String>
    ): ConfirmOtpEntity {
        return requestInsideCatch {
            val response = apiService.confirmOtp(params)

            return@requestInsideCatch generateResultEntity(
                ConfirmOtpResponse::class.java,
                response
            ) { authMapper.toConfirmOtpEntity(it) }
        }
    }

    override suspend fun confirmRegisterOtp(
        params: HashMap<String, String>
    ): ConfirmOtpEntity {
        return requestInsideCatch {
            val response = apiService.confirmRegisterOtp(params)

            return@requestInsideCatch generateResultEntity(
                ConfirmOtpResponse::class.java,
                response
            ) { authMapper.toConfirmOtpEntity(it) }
        }
    }

    override suspend fun getRefreshToken(refreshToken: String): RefreshTokenEntity {
        return requestInsideCatch {
            val response = apiService.getRefreshToken(RefreshTokenRequest(refreshToken))

            return@requestInsideCatch generateResultEntity(
                RefreshTokenResponse::class.java,
                response
            ) { authMapper.toRefreshTokenEntity(it) }
        }
    }

}