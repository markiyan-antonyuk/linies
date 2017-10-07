package com.markantoni.linies.ui.config.events

data class VisibilityChangeEvent(val type: Int, val visible: Boolean)
data class OpenColorPickerEvent(val type: Int)
class OpenDateFormatPickerEvent
data class Hours24ChangeEvent(val hours24: Boolean)
data class OpenComplicationConfigurationEvent(val id: Int)