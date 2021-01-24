package io.driverdoc.testapp.ui.base.adapter

import android.view.View
import io.driverdoc.testapp.ui.base.IGetPosition

abstract class BaseViewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder {
    constructor(itemView: View?) : super(itemView!!)

    protected fun setOnClickView(onClick: View.OnClickListener, vararg vs: View) {
        val position = object : IGetPosition {
            override fun getPosition(): Int {
                return adapterPosition
            }
        }
        for (view in vs) {
            view.tag = position
            view.setOnClickListener(onClick)
        }
    }
}