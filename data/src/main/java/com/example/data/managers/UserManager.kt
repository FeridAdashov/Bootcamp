package com.example.data.managers


import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit

class UserManager private constructor() {
    companion object {
        @Volatile
        private var preferenceManager: SharedPreferences? = null

        fun init(context: Context) {
            synchronized(this) {
                if (preferenceManager == null) {
                    preferenceManager =
                        context.getSharedPreferences(KEY_SHARED_PREFERENCE, Context.MODE_PRIVATE)
                }
            }
        }

        fun isLogged(): Boolean = !refreshToken().isNullOrBlank()

        fun getLanguage(): String? = preferenceManager?.getString(KEY_LANGUAGE, null)

        fun getDefaultLanguage(): String {
            return "az"
        }

        fun setLanguage(language: String) =
            preferenceManager?.edit { putString(KEY_LANGUAGE, language) }

        fun getToken(): String {
            val token = preferenceManager?.getString(KEY_TOKEN, "")
            return String(Base64.decode(token!!.toByteArray(), Base64.CRLF))
        }

        fun setToken(token: String) = preferenceManager?.edit {
            putString(KEY_TOKEN, Base64.encodeToString(token.toByteArray(), Base64.DEFAULT))
        }


        fun refreshToken(): String? = preferenceManager?.getString(KEY_REFRESH_TOKEN, null)


        fun setRefreshToken(refreshToken: String) = preferenceManager?.edit {
            putString(
                KEY_REFRESH_TOKEN,
                refreshToken
            )
        }

        private const val KEY_SHARED_PREFERENCE = "KEY_SHARED_PREFERENCE"
        private const val KEY_LANGUAGE = "KEY_LANGUAGE"
        private const val KEY_TOKEN = "KEY_TOKEN"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    }
}