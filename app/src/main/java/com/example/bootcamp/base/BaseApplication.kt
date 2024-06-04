package com.example.bootcamp.base

import android.app.Application
import android.content.Context
import com.example.data.managers.UserManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {
    init {
        instance = this
    }

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
        UserManager.init(applicationContext())
    }

    companion object {
        private var instance: BaseApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}