package com.example.data.dto.response

import com.google.gson.annotations.SerializedName
import java.net.HttpURLConnection

open class BaseResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @SerializedName("code")
    val code: Int = HttpURLConnection.HTTP_OK,
)