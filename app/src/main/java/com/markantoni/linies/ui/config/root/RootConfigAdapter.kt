package com.markantoni.linies.ui.config.root

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView
import com.markantoni.linies.R
import com.markantoni.linies.util.context
import com.markantoni.linies.util.inflate
import com.markantoni.linies.util.startActivityWithRevealAnimation

class RootConfigAdapter(private val items: List<RootConfigItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE_HOLDER_TYPE = 1

    override fun getItemViewType(position: Int) = when (position) {
        0 -> TITLE_HOLDER_TYPE
        else -> 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TITLE_HOLDER_TYPE -> TitleHolder(parent)
        else -> ItemHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is ItemHolder -> holder.bind(items[position - 1])
        else -> Unit
    }

    override fun getItemCount() = items.size + 1

    class ItemHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.view_holder_root_config)) {
        fun bind(item: RootConfigItem) {
            (itemView as TextView).apply {
                text = context.getText(item.title)
                setOnClickListener { context.startActivityWithRevealAnimation(item.intent) }
            }
        }
    }

    class TitleHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.view_holder_config_title)) {
        init {
            (itemView as TextView).text = context.getText(R.string.config_title_root)
        }
    }
}