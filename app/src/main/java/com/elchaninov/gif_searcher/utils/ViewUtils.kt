package com.elchaninov.gif_searcher.utils

import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.TranslateAnimation
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

fun View.fadeIn() {
    val animation = AlphaAnimation(0f, 1f).apply {
        duration = 500
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    startAnimation(animation)
}

fun View.fadeOut() {
    val animation = AlphaAnimation(1f, 0f).apply {
        duration = 500
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    startAnimation(animation)
}

fun View.slideIn(marginBottom: Int = 0) {
    val fromY = height.toFloat() + marginBottom.toFloat()
    val toY = 0f
    val animation = TranslateAnimation(0f, 0f, fromY, toY).apply {
        duration = 300
        this.interpolator = OvershootInterpolator()
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    startAnimation(animation)
}

fun View.slideOut(marginBottom: Int = 0) {
    val fromY = 0f
    val toY = height.toFloat() + marginBottom.toFloat()
    val animation = TranslateAnimation(0f, 0f, fromY, toY).apply {
        duration = 300
        this.interpolator = AccelerateDecelerateInterpolator()
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
    startAnimation(animation)
}
