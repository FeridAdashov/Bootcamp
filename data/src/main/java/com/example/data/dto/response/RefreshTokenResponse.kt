package com.example.data.dto.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @field:SerializedName("body")
    val body: RefreshTokenResponseBody? = null,
) : BaseResponse() {

    data class RefreshTokenResponseBody(
        @field:SerializedName("access")
        val accessToken: String?,
    )
}

