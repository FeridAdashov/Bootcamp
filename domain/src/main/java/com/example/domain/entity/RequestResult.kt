package com.example.domain.entity

sealed class RequestResult<out R> {

    data class Success<out R>(
        val code: Int,
        val body: R,
    ) : RequestResult<R>()

    data class Error<out R>(
        val code: Int,
        val message: String,
    ) : RequestResult<R>()
}

fun <T> RequestResult.Error<T>.mapToBaseEntity() = BaseEntity(this.code, this.message)