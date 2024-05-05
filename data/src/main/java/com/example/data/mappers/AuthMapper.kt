package com.example.data.mappers

import com.example.data.dto.response.ConfirmOtpResponse
import com.example.data.dto.response.RefreshTokenResponse
import com.example.domain.entity.ConfirmOtpEntity
import com.example.domain.entity.ConfirmOtpTokens
import com.example.domain.entity.RefreshTokenEntity

class AuthMapper : BaseMapper() {
    fun toConfirmOtpEntity(response: ConfirmOtpResponse): ConfirmOtpEntity {
        return ConfirmOtpEntity(
            code = response.code,
            message = response.message,
            body = ConfirmOtpTokens(
                refresh = response.body?.refresh,
                access = response.body?.access
            )
        )
    }

    fun toRefreshTokenEntity(response: RefreshTokenResponse): RefreshTokenEntity {
        return RefreshTokenEntity(
            code = response.code,
            message = response.message,
            accessToken = response.body?.accessToken
        )
    }
}