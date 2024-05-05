package com.example.data.api

import com.example.data.dto.request.LoginWithPhoneRequest
import com.example.data.dto.request.RefreshTokenRequest
import com.example.data.dto.response.BaseResponse
import com.example.data.dto.response.ConfirmOtpResponse
import com.example.data.dto.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {
    @POST("v1/auth/login/")
    suspend fun loginWithPhone(@Body body: LoginWithPhoneRequest): Response<BaseResponse>

    @PUT("v1/auth/login/")
    suspend fun confirmOtp(@Body params: HashMap<String, String>): Response<ConfirmOtpResponse>

    @PUT("v1/auth/register-confirm/")
    suspend fun confirmRegisterOtp(@Body params: HashMap<String, String>): Response<ConfirmOtpResponse>

    @POST("v1/auth/refresh/")
    suspend fun getRefreshToken(@Body body: RefreshTokenRequest): Response<RefreshTokenResponse>
}