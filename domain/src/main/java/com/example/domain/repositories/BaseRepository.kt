package com.example.domain.repositories

import android.util.Log
import com.example.domain.ExceptionUtils
import com.example.domain.entity.BaseEntity
import com.google.gson.Gson
import retrofit2.Response

interface BaseRepository {
    suspend fun <T : BaseEntity> requestInsideCatch(request: suspend () -> T): T {
        return try {
            request.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.let { Log.d("ERROR-generateResultEntity", it) }

            BaseEntity(ExceptionUtils.getExceptionCode(e), e.message) as T
        }
    }

    fun <T, Z : BaseEntity> generateResultEntity(
        responseClass: Class<T>,
        response: Response<T>,
        mapper: (errorResponse: T) -> Z,
    ): Z {
        return if (response.isSuccessful) {
            mapper(response.body()!!)
        } else {
            val errorResponse = Gson().fromJson(
                response.errorBody()?.string(),
                responseClass
            )
            mapper(errorResponse)
        }
    }
}