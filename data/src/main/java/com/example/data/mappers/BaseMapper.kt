package com.example.data.mappers

import com.example.data.dto.response.BaseResponse
import com.example.domain.entity.BaseEntity

open class BaseMapper {

    fun toBaseEntity(baseResponse: BaseResponse): BaseEntity {
        return BaseEntity(
            code = baseResponse.code,
            message = baseResponse.message,
        )
    }
}