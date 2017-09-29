package com.markantoni.linies.ui.config

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.markantoni.linies.ui.config.holders.BaseConfigViewHolder
import com.markantoni.linies.ui.config.holders.ColorsConfigViewHolder
import com.markantoni.linies.ui.config.holders.VisibilityConfigViewHolder

class ConfigAdapter : RecyclerView.Adapter<BaseConfigViewHolder>() {
    private val HOLDERS = arrayOf(ColorsConfigViewHolder, VisibilityConfigViewHolder)

    override fun getItemViewType(position: Int) = HOLDERS[position].getType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ColorsConfigViewHolder.getType() -> ColorsConfigViewHolder(parent)
        VisibilityConfigViewHolder.getType() -> VisibilityConfigViewHolder(parent)
        else -> error("Not applicable view holder type")
    }

    override fun onBindViewHolder(holderConfig: BaseConfigViewHolder, position: Int) = Unit

    override fun getItemCount() = HOLDERS.size
}

