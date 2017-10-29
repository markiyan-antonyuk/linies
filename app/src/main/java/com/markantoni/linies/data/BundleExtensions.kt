package com.markantoni.linies.data

import android.os.Bundle
import com.markantoni.linies.Key
import com.markantoni.linies.Type

fun Bundle.setType(type: Int) = putInt(Key.TYPE, type)
fun Bundle.getType() = getInt(Key.TYPE, Type.UNKNOWN)