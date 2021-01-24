package io.driverdoc.testapp.ui.main.song

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.ItemSong

class SongAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<SongAdapter.Companion.SongViewHolder> {
    private val inter: ISongAdapter

    constructor(inter: ISongAdapter) {
        this.inter = inter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = io.driverdoc.testapp.databinding.ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        )
        return SongViewHolder(binding)
    }

    override fun getItemCount() = inter.count()
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val data = inter.getData(position)
        holder.binding.tvName.text = data.title
        if (data.urlJunDownload == null) {
            io.driverdoc.testapp.ui.customview.GlideApp.with(holder.itemView.context)
                    .load(R.drawable.ao_dai)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivAvatar)
        } else {
            io.driverdoc.testapp.ui.customview.GlideApp.with(holder.itemView.context)
                    .load(data.avatar)
                    .error(R.drawable.ao_dai)
                    .placeholder(R.drawable.ao_dai)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.binding.ivAvatar)
        }
    }

    companion object {
        class SongViewHolder(val binding: io.driverdoc.testapp.databinding.ItemSongBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)
    }

    interface ISongAdapter {
        fun count(): Int
        fun getData(position: Int): ItemSong
    }
}