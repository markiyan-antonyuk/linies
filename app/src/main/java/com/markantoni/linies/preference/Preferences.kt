package com.markantoni.linies.preference

interface Preferences {
    fun setColor(type: Int, color: Int)
    fun setVisible(type: Int, visible: Boolean)
    fun set24Hours(hours24: Boolean)
    fun setDateFormat(dateFormat: String)
    fun setAnimating(type: Int, animate: Boolean)

    fun getColor(type: Int): Int
    fun isVisible(type: Int): Boolean
    fun is24Hours(): Boolean
    fun getDateFormat(): String
    fun isAnimating(type: Int): Boolean
}