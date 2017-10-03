package com.markantoni.linies.ui.config.holders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseConfigViewHolder(parent: ViewGroup, @LayoutRes layout: Int) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false)) {
    val context = itemView.context
    open fun release() {}
}