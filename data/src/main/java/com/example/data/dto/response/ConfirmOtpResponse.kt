package com.example.data.dto.response

import com.google.gson.annotations.SerializedName

class ConfirmOtpResponse(
    @field:SerializedName("body")
    val body: ConfirmOtpResponseBody? = null,
) : BaseResponse() {
    data class ConfirmOtpResponseBody(
        @field:SerializedName("refresh")
        val refresh: String? = null,

        @field:SerializedName("access")
        val access: String? = null,
    )
}