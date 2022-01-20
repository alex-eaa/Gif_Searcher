package com.elchaninov.gif_searcher.ui

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun View.hideKeyboard(): Boolean = try {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
} catch (e: RuntimeException) {
    false
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.showSnackbar(
    text: String = "Error",
    actionText: String = "Try Again",
    action: (View) -> Unit,
    length: Int = Snackbar.LENGTH_INDEFINITE,
) {

    Snackbar.make(this, text, length)
        .setAction(actionText) { action(this) }
        .show()

}