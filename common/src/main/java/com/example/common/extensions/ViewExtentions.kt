package com.legalist.userapp.common.extensions

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.ViewCompat

/**
 * Sets radius to every corner in dp (Density-independent Pixels)
 * @param topLeftRadius Top Left corner radius
 * @param topRightRadius Top Right corner radius
 * @param bottomRightRadius Bottom Right corner radius
 * @param bottomLeftRadius Bottom Left corner radius
 */
fun View.setCorners(
    topLeftRadius: Float = 0f,
    topRightRadius: Float = 0f,
    bottomRightRadius: Float = 0f,
    bottomLeftRadius: Float = 0f
) {
    val gradientDrawable = try {
        background as GradientDrawable
    } catch (e: Exception) {
        GradientDrawable()
    }

    gradientDrawable.cornerRadii = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        bottomRightRadius,
        bottomRightRadius,
        bottomLeftRadius,
        bottomLeftRadius
    )
    background = gradientDrawable
}

/**
 * Sets radius to all corners in dp (Density-independent Pixels)
 * @param radius Corner radius
 */
fun View.setCorners(
    radius: Float
) {
    val gradientDrawable = background as GradientDrawable
    val radiusInPixels =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, radius, resources.displayMetrics)
    gradientDrawable.cornerRadius = radiusInPixels
    background = gradientDrawable
}


fun View.applyInsetsToContentView(applyStatusBarInsetToContentView: Boolean) {
    this.fitsSystemWindows = !applyStatusBarInsetToContentView
    ViewCompat.requestApplyInsets(this)
}

fun View.addKeyboardOpenListener(listener: (Boolean) -> Unit) {
    this.viewTreeObserver.addOnGlobalLayoutListener {
        val r = Rect()
        this.getWindowVisibleDisplayFrame(r);
        val screenHeight = this.rootView.height

        val keypadHeight = screenHeight - r.bottom;
        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
            // keyboard is opened
            listener(true)
        } else {
            // keyboard is closed
            listener(false)
        }
    }
}


fun EditText.showKeyboard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    requestFocus()
}