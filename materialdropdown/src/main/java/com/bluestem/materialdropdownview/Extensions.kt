package com.bluestem.materialdropdownview

import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver


// Inline global layout listener for views.
inline fun View.waitForLayout(crossinline action: () -> Unit) {
    val observer = viewTreeObserver
    observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            when {
                observer.isAlive -> {
                    observer.removeOnGlobalLayoutListener(this)
                    Handler().postDelayed({ action() }, 0)
                }
                else -> viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
    })
}

// Inline layoutParams width setter for views.
fun View.setLayoutParamsWidth(width: Int) {
    val layoutParamsCopy = layoutParams
    layoutParamsCopy.width = width
    layoutParams = layoutParamsCopy
}

// Inline layoutParams width setter for views.
fun View.setLayoutParamsHeight(height: Int) {
    val layoutParamsCopy = layoutParams
    layoutParamsCopy.height = height
    layoutParams = layoutParamsCopy
}