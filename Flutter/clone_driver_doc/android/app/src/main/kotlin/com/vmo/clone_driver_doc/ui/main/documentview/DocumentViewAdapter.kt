package io.driverdoc.testapp.ui.main.documentview

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.driverdoc.testapp.databinding.ItemDocumentViewBinding
import io.driverdoc.testapp.ui.base.IGetPosition
import io.driverdoc.testapp.ui.base.adapter.BaseViewHolder
import io.driverdoc.testapp.ui.customview.GlideApp
import uk.co.senab.photoview.PhotoViewAttacher



class DocumentViewAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<DocumentViewAdapter.Companion.StoreViewHolder>, View.OnClickListener {

    private val inter: IStoreAdapter

    constructor(inter: IStoreAdapter, context: Context) {
        this.inter = inter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        val binding = ItemDocumentViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
        )
        return StoreViewHolder(binding, this)
    }

    override fun getItemCount() = inter.count()
    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val data = inter.getData(position)
        GlideApp.with(holder.itemView.context)
                .load(data)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .fitCenter()
                .override(2000, 2000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.binding.img)

        val pAttacher: PhotoViewAttacher
        pAttacher = PhotoViewAttacher(holder.binding.img)
        pAttacher.update()
    }

    override fun onClick(p0: View?) {
        var position: IGetPosition? = null
        position = p0!!.tag as IGetPosition

    }

    companion object {
        class StoreViewHolder(val binding: ItemDocumentViewBinding, onClick: View.OnClickListener) : BaseViewHolder(binding.root) {

        }

    }

    interface IStoreAdapter {
        fun count(): Int
        fun getData(position: Int): String

    }


}