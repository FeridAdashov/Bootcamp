package com.example.data.api

import com.example.data.dto.request.LoginWithPhoneRequest
import com.example.data.dto.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("v1/auth/login/")
    suspend fun loginWithPhone(@Body body: LoginWithPhoneRequest): Response<BaseResponse>
}