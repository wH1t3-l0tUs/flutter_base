package io.driverdoc.testapp.ui.main.song

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.view.View
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.ItemSong
import io.driverdoc.testapp.data.model.base.error.ResponseException
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.reactivex.disposables.Disposable

class SongFragment : BaseMvvmFragment<SongCallBack, SongModel>(), SongCallBack, SongAdapter.ISongAdapter, View.OnClickListener {

    private var dis: Disposable? = null
    override fun createModel(): SongModel {
        val model = SongModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(SongModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_song

    override fun setEvents() {
        getBindingData().btnSearch.setOnClickListener(this)
    }

    override fun initComponents() {
        mModel.obSong.observe(this, Observer<MutableList<ItemSong>> {
            if (getBindingData().rcSearch.adapter == null) {
                getBindingData().rcSearch.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(getBaseActivity())
                getBindingData().rcSearch.adapter = SongAdapter(this@SongFragment)
            } else {
                getBindingData().rcSearch.adapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun onError(id: String, error: ResponseException) {
        showMessage(error.message!!)
        when (id) {
            ItemSong::class.java.name -> {
                mModel.getAllSongOffline()
            }
        }
    }

    fun getBindingData() = mBinding as io.driverdoc.testapp.databinding.FragmentSongBinding

    override fun count(): Int {
        if (mModel.obSong.value == null) {
            return 0
        }
        return mModel.obSong.value!!.size
    }

    override fun getData(position: Int) = mModel.obSong.value!!.get(position)

    override fun onClick(view: View) {
        dis?.dispose()
        dis = mModel.getSongs(getBindingData().edtSearch.text.toString())
    }

    override fun onDestroyViewControl() {
        dis?.dispose()
        super.onDestroyViewControl()
    }
}