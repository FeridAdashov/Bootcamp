package com.example.common.extensions

import android.content.Context

fun Int.dpToPx(context: Context): Float = this * context.resources.displayMetrics.density

fun Float.pxToDp(context: Context): Int = (this / context.resources.displayMetrics.density).toInt()