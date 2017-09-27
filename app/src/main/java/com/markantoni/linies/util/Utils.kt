package com.markantoni.linies.util

import android.content.Context

fun calculatePercentage(percent: Float, from: Float) = from * percent / 100f

fun getCenterOfScreen(context: Context) = intArrayOf(context.resources.displayMetrics.widthPixels / 2, context.resources.displayMetrics.heightPixels / 2)