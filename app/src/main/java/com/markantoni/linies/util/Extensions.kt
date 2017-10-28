package com.markantoni.linies.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.RectF
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.markantoni.linies.LiniesWatchFaceService
import org.greenrobot.eventbus.EventBus

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, message, duration).show()

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun Context.startActivityForResult(intent: Intent, requestCode: Int, options: Bundle? = null) {
    val activity = this as? Activity ?: (this as ContextWrapper).baseContext as Activity
    activity.startActivityForResult(intent, requestCode, options)
}

fun <T> MutableList<T>.moveToStart(index: Int) {
    val item = removeAt(index)
    add(0, item)
}

fun <T> MutableList<T>.moveElementToStart(element: T) = moveToStart(indexOf(element))

fun ViewGroup.inflate(@LayoutRes layout: Int) = LayoutInflater.from(context).inflate(layout, this, false)

fun Context.startActivity(activityClass: Class<out Activity>) = startActivity(Intent(this, activityClass))

val RecyclerView.ViewHolder.context get() = itemView.context

fun Activity.startActivityWithRevealAnimation(intent: Intent) {
    val screenCenter = getCenterOfScreen(this)
    val options = ActivityOptionsCompat.makeClipRevealAnimation(window.decorView, screenCenter[0], screenCenter[1], 0, 0).toBundle()
    startActivity(intent, options)
}

fun Context.getWatchFaceServiceComponentName() = ComponentName(this, LiniesWatchFaceService::class.java)

fun RectF.scale(scale: Float) {
    bottom -= height() / 2 * scale
    top += height() / 2 * scale
    left += width() / 2 * scale
    right -= width() / 2 * scale
}

fun Any.sendEvent(event: Any) = EventBus.getDefault().post(event)
fun Any.registerEventBus() = EventBus.getDefault().register(this)
fun Any.unregisterEventBus() = EventBus.getDefault().unregister(this)
