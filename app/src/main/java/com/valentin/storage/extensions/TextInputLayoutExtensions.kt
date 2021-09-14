package com.valentin.storage.extensions

import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.clearError() {
    isErrorEnabled = false
    error = ""
}

fun TextInputLayout.callError(message: String) {
    isErrorEnabled = true
    error = message
}