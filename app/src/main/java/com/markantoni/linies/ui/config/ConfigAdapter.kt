package com.markantoni.linies.ui.config

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.markantoni.linies.ui.config.holders.*

class ConfigAdapter : RecyclerView.Adapter<BaseConfigViewHolder>() {
    private val HOLDERS = arrayOf(
            ColorsConfigViewHolder::class.java,
            ComplicationsConfigViewHolder::class.java,
            DigitalConfigViewHolder::class.java,
            DateConfigViewHolder::class.java
    )

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HOLDERS[viewType].getConstructor(ViewGroup::class.java).newInstance(parent)

    override fun onBindViewHolder(holder: BaseConfigViewHolder, position: Int) = holder.bind()

    override fun getItemCount() = HOLDERS.size
}

