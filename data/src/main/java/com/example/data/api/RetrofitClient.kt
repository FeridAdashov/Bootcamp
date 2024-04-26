package com.example.data.api

import android.content.Context
import com.example.data.interceptors.HeadersInterceptor
import com.example.data.interceptors.makeLoggingInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.Arrays
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitClient {


    private var mOkHttpClient: OkHttpClient = OkHttpClient()

    fun <T> makeRetrofitService(context: Context, baseUrl: String, mClass: Class<T>): T {
        val okHttpClientBuilder = makeSecureHttpBuilder(context)

        return makeRetrofit(okHttpClientBuilder.build(), makeGson(), baseUrl)
            .create(mClass)
    }

    fun makeSecureHttpBuilder(context: Context): OkHttpClient.Builder {
        val okHttpClientBuilder = makeOkHttpClientBuilder(context)
        disableSSLCheck(okHttpClientBuilder)
        return okHttpClientBuilder
    }

    private fun makeRetrofit(okHttpClient: OkHttpClient, gson: Gson, baseUrl: String): Retrofit {
        mOkHttpClient = okHttpClient
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun makeDispatcher(): Dispatcher {
        return Dispatcher().apply {
            maxRequests = 3
            maxRequestsPerHost = 1
        }
    }

    private fun makeOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(makeLoggingInterceptor())
            .addInterceptor(HeadersInterceptor())
            .dispatcher(makeDispatcher())
    }

    private fun makeGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    private fun disableSSLCheck(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<out X509Certificate>?,
                        authType: String?,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return emptyArray()
                    }
                }
            )
            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
            builder
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}