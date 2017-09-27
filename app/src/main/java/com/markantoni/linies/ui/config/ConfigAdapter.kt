package com.markantoni.linies.ui.config

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.markantoni.linies.R

class ConfigAdapter(private val actions: List<ConfigItem>) : RecyclerView.Adapter<ConfigViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ConfigViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_config_item, parent, false))

    override fun getItemCount() = actions.size

    override fun onBindViewHolder(holder: ConfigViewHolder, position: Int) = holder.bind(actions[position])
}

