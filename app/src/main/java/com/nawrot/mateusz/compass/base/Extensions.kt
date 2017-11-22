package com.nawrot.mateusz.compass.base

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar


fun ProgressBar.setColor(color: Int) {
    indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun Context.getColorCompat(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getDimenValue(@DimenRes dimen: Int): Float {
    return resources.getDimension(dimen)
}

fun ImageView.setDrawable(@DrawableRes imageDrawable: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, imageDrawable))
}

fun Context.getDrawableCompat(@DrawableRes imageDrawable: Int): Drawable? {
    return ContextCompat.getDrawable(this, imageDrawable)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissionCompat(permissions: Array<out String>, requestCode: Int) {
    ActivityCompat.requestPermissions(this, permissions, requestCode)
}

fun View.showSnackbar(message: String, length: Int = Snackbar.LENGTH_LONG, action: String? = null, actionListener: (View) -> Unit = {}, actionColor: Int? = null) {
    val snack = Snackbar.make(this, message, length)

    action?.let { snack.setAction(action, actionListener) }
    actionColor?.let { snack.setActionTextColor(actionColor) }

    snack.show()
}

fun String.toOptionalDouble(): Double? {
    return try {
        this.toDouble()
    } catch (exception: NumberFormatException) {
        null
    }
}

fun Float.difference(otherFloat: Float) : Float {
    return Math.abs(this - otherFloat)
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun TextInputLayout.clearError() {
    isErrorEnabled = false
    error = null
}

fun TextInputLayout.enableAndSetError(@StringRes errorRes: Int) {
    isErrorEnabled = true
    error = context.getString(errorRes)
}