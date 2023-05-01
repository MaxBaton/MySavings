package com.example.mysavings.app.presentation.common

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.inputmethod.InputMethodManager
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

fun Activity.hideKeyboard() {
    val isVisible = KeyboardVisibilityEvent.isKeyboardVisible(this)
    if (isVisible) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }else {
            this.currentFocus?.let { view ->
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}

fun Activity.toggleKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}