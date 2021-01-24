package io.driverdoc.testapp.ui.start.started

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.driverdoc.testapp.ui.base.adapter.BaseViewHolder
import io.driverdoc.testapp.data.model.CCPCountry
import io.driverdoc.testapp.ui.base.IGetPosition
import io.driverdoc.testapp.ui.utils.StringUtils


class CountryCodeAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CountryCodeAdapter.Companion.StoreViewHolder>, View.OnClickListener {

    private val inter: IStoreAdapter

    constructor(inter: IStoreAdapter) {
        this.inter = inter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = io.driverdoc.testapp.databinding.ItemCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        )
        return StoreViewHolder(binding, this)
    }

    override fun getItemCount() = inter.count()

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val data = inter.getData(position)
        if (!StringUtils.isEmpty(data.phoneCode)) {
            holder.binding.tvCode.setText("+" + data.phoneCode)
        }

        if (!StringUtils.isEmpty(data.name)) {
            holder.binding.tvName.setText(data.name)
        }

        if (!StringUtils.isEmpty(data.nameCode)) {
            holder.binding.ivImage.setImageResource(StringUtils.getFlagMasterResID(data.nameCode))
        }
    }


    override fun onClick(p0: View?) {
        var position: IGetPosition? = null
        position = p0!!.tag as IGetPosition
        inter.onClickItem(position.getPosition())
    }

    companion object {
        class StoreViewHolder(val binding: io.driverdoc.testapp.databinding.ItemCountryBinding, onClick: View.OnClickListener) : BaseViewHolder(binding.root) {
            init {
                val view = binding.root
                setOnClickView(onClick, view)
            }
        }

    }

    interface IStoreAdapter {
        fun count(): Int
        fun getData(position: Int): CCPCountry
        fun onClickItem(position: Int)
    }


}