package com.example.data.interceptors

import com.example.data.managers.UserManager
import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor() : Interceptor {

    private val token = "3bGll7KpEkCKCRxQJFh8M346cTCc8IbR"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        val shouldAddAuthHeader = request.headers["isAuthorize"] != "false"
        val isMultiPartContent = request.headers["isMultiPartContent"] == "true"

        //You can change url by sending with header
        val fullUrl = request.headers["fullUrl"]

        requestBuilder.apply {
            if (!fullUrl.isNullOrEmpty()) url(fullUrl)

//            if (UserManager.isLogged() && shouldAddAuthHeader) {
//                header("Authorization", "Bearer " + UserManager.getToken())
//            }
            addHeader("App-Token", token)
            addHeader(
                "Content-Type",
                if (isMultiPartContent) "multipart/form-data" else "application/json"
            )
            addHeader("Language", UserManager.getLanguage() ?: UserManager.getDefaultLanguage())
        }
        return chain.proceed(requestBuilder.build())
    }
}

