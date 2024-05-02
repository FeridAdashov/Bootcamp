package com.example.data.interceptors

//import com.example.data.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

internal fun makeLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = if (true)
        HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE
    return logging
}