package com.markantoni.linies.ui.config

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.markantoni.linies.ui.config.holders.BaseConfigViewHolder
import com.markantoni.linies.ui.config.holders.ColorsConfigViewHolder
import com.markantoni.linies.ui.config.holders.ComplicationsConfigViewHolder
import com.markantoni.linies.ui.config.holders.VisibilityConfigViewHolder

class ConfigAdapter : RecyclerView.Adapter<BaseConfigViewHolder>() {
    private val HOLDERS = arrayOf(
            ColorsConfigViewHolder::class.java,
            ComplicationsConfigViewHolder::class.java,
            VisibilityConfigViewHolder::class.java
    )

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HOLDERS[viewType].getConstructor(ViewGroup::class.java).newInstance(parent)

    override fun onBindViewHolder(holderConfig: BaseConfigViewHolder, position: Int) {}

    override fun onViewRecycled(holder: BaseConfigViewHolder) = holder.release()

    override fun getItemCount() = HOLDERS.size
}

