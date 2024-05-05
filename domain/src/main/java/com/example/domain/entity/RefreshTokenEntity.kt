package com.example.domain.entity


class RefreshTokenEntity(
    code: Int,
    message: String?,
    val accessToken: String? = null
) : BaseEntity(code, message)