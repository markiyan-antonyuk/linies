package com.markantoni.linies.ui.config.events

data class VisibilityChangeEvent(val type: Int, val visible: Boolean)
data class OpenColorPickerEvent(val type: Int)
data class OpenComplicationConfigurationEvent(val id: Int)