package com.markantoni.linies.ui.config

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.markantoni.linies.ui.config.holders.BaseViewHolder
import com.markantoni.linies.ui.config.holders.ColorsConfigViewHolder

class ConfigAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private val HOLDERS = arrayOf(ColorsConfigViewHolder)

    override fun getItemViewType(position: Int) = HOLDERS[position].getType()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ColorsConfigViewHolder.getType() -> ColorsConfigViewHolder(parent)
        else -> error("Not applicable view holder type")
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) = Unit

    override fun getItemCount() = HOLDERS.size
}

