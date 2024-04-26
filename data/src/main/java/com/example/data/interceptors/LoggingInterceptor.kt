package com.example.data.interceptors

import com.bankofbaku.app.data.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

internal fun makeLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = if (BuildConfig.DEBUG)
        HttpLoggingInterceptor.Level.BODY
    else
        HttpLoggingInterceptor.Level.NONE
    return logging
}