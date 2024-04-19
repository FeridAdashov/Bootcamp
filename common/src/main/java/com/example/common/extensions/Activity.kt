package com.example.common.extensions

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Window.setStatusBarColor(@ColorRes color: Int, windowLightStatusBar: Boolean) {
    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var flags = decorView.systemUiVisibility
        flags = if (!windowLightStatusBar)
            flags.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        else
            flags.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        decorView.systemUiVisibility = flags
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        statusBarColor = ContextCompat.getColor(context, color)
    }
}

