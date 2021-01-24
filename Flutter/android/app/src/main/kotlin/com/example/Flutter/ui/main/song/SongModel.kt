package io.driverdoc.testapp.ui.main.song

import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.ItemSong
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class SongModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<SongCallBack>(appDatabase, interactCommon, schedulers) {
    private val restApi: RestApi
    val obSong = MutableLiveData<MutableList<ItemSong>>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun getSongs(name: String): Disposable? {
        return subscribeHasResultDispose(
                restApi.getSongs(name = name)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obSong.value = it
        }
    }

    fun getAllSongOffline() {
        obSong.value = appDatabase.songDao().findAll()
    }

}