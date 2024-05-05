package com.example.domain.entity


class ConfirmOtpEntity(
    code: Int,
    message: String?,
    val body: ConfirmOtpTokens? = null
) : BaseEntity(code, message)

data class ConfirmOtpTokens(
    val refresh: String?,
    val access: String?,
)