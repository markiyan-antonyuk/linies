package com.markantoni.linies

@Deprecated("Soon to be removed")
object Key {
    const val TYPE = "key.type"
    const val COLOR = "key.color"
    const val VISIBLE = "key.visible"
    const val HOURS24 = "key.hours24"
    const val DATE_FORMAT = "key.date.format"
    const val ANIMATING = "key.animate"
}

@Deprecated("Soon to be removed")
object Type {
    const val UNKNOWN = -1
    const val HOUR = 0
    const val MINUTE = 1
    const val SECOND = 2
    const val DIGITAL = 3
    const val DATE = 4
    const val COMPLICATIONS = 5
}