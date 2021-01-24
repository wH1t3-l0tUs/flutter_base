package io.driverdoc.testapp.ui.main.incompletrip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.trip.IncomeTrip
import io.driverdoc.testapp.ui.base.IGetPosition
import io.driverdoc.testapp.ui.base.adapter.BaseViewHolder
import io.driverdoc.testapp.ui.utils.StringUtils
import java.util.*

class TripCompleteAdapter(private val inter: IStoreAdapter) : androidx.recyclerview.widget.RecyclerView.Adapter<TripCompleteAdapter.Companion.StoreViewHolder>(), View.OnClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = io.driverdoc.testapp.databinding.ItemTripCompleteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        )
        return StoreViewHolder(binding, this)
    }

    override fun getItemCount() = inter.count()

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val data = inter.getData(position)
//        holder.itemView.animation=AnimationUtils.loadAnimation(holder.itemView.context,R.anim.item_animation_fall_down)
        data.trip_number?.let {
            holder.binding.tvName.setText("Trip " + it)
        }
        holder.binding.img2.setColorFilter(holder.binding.tvTime.context.resources.getColor(R.color.black))
        holder.binding.img3.setColorFilter(holder.binding.tvTime.context.resources.getColor(R.color.black))
        data.location?.name?.let {
            holder.binding.tvAddr.setText(it)
        }
        data.is_appointment?.let {
            if (it == 1) {
                holder.binding.tvTime2.visibility = View.GONE
            } else {
                holder.binding.tvTime2.visibility = View.VISIBLE
            }
        }
        data.earliest_time?.let {
            holder.binding.tvTime.setText(StringUtils.fomatDateSb(it) + " - " + StringUtils.fomatTimeNormal(it))
        }
        data.latest_time?.let {
            holder.binding.tvTime2.setText(StringUtils.fomatDateSb(it) + " - " + StringUtils.fomatTimeNormal(it))
        }
    }

    override fun onClick(p0: View?) {
        var position: IGetPosition? = null
        position = p0!!.tag as IGetPosition
        inter.onClickItem(position.getPosition())
    }

    companion object {
        class StoreViewHolder(val binding: io.driverdoc.testapp.databinding.ItemTripCompleteBinding, onClick: View.OnClickListener) : BaseViewHolder(binding.root) {
            init {
                val view = binding.item
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