package com.markantoni.linies.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.RectF
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

fun <T> MutableList<T>.moveToStart(index: Int) {
    val item = removeAt(index)
    add(0, item)
}

fun <T> MutableList<T>.moveElementToStart(element: T) = moveToStart(indexOf(element))

fun ViewGroup.inflate(@LayoutRes layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)

val RecyclerView.ViewHolder.context get() = itemView.context

fun Context.startActivityWithRevealAnimation(intent: Intent) {
    when (this) {
        is Activity -> startActivityWithRevealAnimation(intent)
        else -> startActivity(intent)
    }
}

fun Activity.startActivityWithRevealAnimation(intent: Intent) {
    val screenCenter = getCenterOfScreen()
    val options = ActivityOptionsCompat.makeClipRevealAnimation(window.decorView, screenCenter[0], screenCenter[1], 0, 0).toBundle()
    startActivity(intent, options)
}

fun RectF.scale(scale: Float) {
    bottom -= height() / 2 * scale
    top += height() / 2 * scale
    left += width() / 2 * scale
    right -= width() / 2 * scale
}

inline fun <reified F> Collection<*>.findInstance(): F? = filterIsInstance<F>().firstOrNull()

fun Float.calculatePercentage(percent: Float) = this * percent / 100f

fun Float.calculatePercentageOf(value: Float) = value * 100 / this

fun Context.getCenterOfScreen() = intArrayOf(resources.displayMetrics.widthPixels / 2, resources.displayMetrics.heightPixels / 2)