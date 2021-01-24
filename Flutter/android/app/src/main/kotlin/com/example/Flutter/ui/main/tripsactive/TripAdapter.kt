package io.driverdoc.testapp.ui.main.tripsactive

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.trip.IncomeTrip
import io.driverdoc.testapp.ui.base.adapter.BaseViewHolder
import io.driverdoc.testapp.ui.base.IGetPosition
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.StringUtils.fomatDateNormal
import io.driverdoc.testapp.ui.utils.StringUtils.fomatTimeNormal
import java.util.*

class TripAdapter(private val inter: IStoreAdapter) : androidx.recyclerview.widget.RecyclerView.Adapter<TripAdapter.Companion.TripViewHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = io.driverdoc.testapp.databinding.ItemTripBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        )
        return TripViewHolder(binding, this)
    }

    override fun getItemCount() = inter.count()

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val data = inter.getData(position)

        data.isBlock?.let {
            if (it) {
                holder.binding.btnMap.visibility = View.GONE
            } else {
                holder.binding.btnMap.visibility = View.VISIBLE
            }
        }

        data.location?.let {
            holder.binding.tvName.setText(it.name)
            if (null != it.address) {
                holder.binding.tvAddr.setText(it.address)
            }
        }

        data.latest_time?.let {
            holder.binding.tvTime.setText(fomatTimeNormal(it))
        }
        data.latest_time?.let {
            holder.binding.tvDay.setText(fomatDateNormal(it))
            if (null == data.is_appointment) return
            if (data.is_appointment == 1) {
                holder.binding.tvTime.setText(fomatTimeNormal(it))
            } else {
                if (null == data.earliest_time) return
                holder.binding.tvTime.setText(fomatTimeNormal(data.earliest_time!!) + " to " + fomatTimeNormal(it))
            }
        }
        holder.binding.img2.setColorFilter(holder.binding.tvTime.context.resources.getColor(R.color.black))

    }

    override fun onClick(p0: View?) {
        var position: IGetPosition? = null
        position = p0!!.tag as IGetPosition
        inter.onClickItem(position.getPosition())
    }


    companion object {
        class TripViewHolder(val binding: io.driverdoc.testapp.databinding.ItemTripBinding, onClick: View.OnClickListener) : BaseViewHolder(binding.root) {
            init {
                val view = binding.btnMap
                setOnClickView(onClick, view)
            }
        }
    }

    interface IStoreAdapter {
        fun count(): Int
        fun getData(position: Int): IncomeTrip
        fun onClickItem(position: Int)
    }


}