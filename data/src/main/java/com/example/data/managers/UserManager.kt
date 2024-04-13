package com.example.data.managers


import android.content.Context
import android.content.SharedPreferences

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

        fun isLogged(): Boolean = preferenceManager?.getString(KEY_REFRESH_TOKEN, "")
            .isNullOrBlank()

        fun refreshToken(): String? {
            return ""
        }


        private const val KEY_SHARED_PREFERENCE = "KEY_SHARED_PREFERENCE"
        private const val KEY_REFRESH_TOKEN = "KEY_REFRESH_TOKEN"
    }
}