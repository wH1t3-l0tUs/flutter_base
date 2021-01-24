package io.driverdoc.testapp.ui.splash

import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.VersionRespone
import io.driverdoc.testapp.data.model.login.VerifiCodeRespone
import io.driverdoc.testapp.data.model.trip.TripRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class SplashModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<SplashCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obHasTrip = MutableLiveData<TripRespone>()
        get
    val obInfor = MutableLiveData<VerifiCodeRespone>()
        get
    val obCheckVersion = MutableLiveData<VersionRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun getTrip(auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getActiveTrip()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obHasTrip.value = it
        }
    }

    fun getInfor(auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getInfor()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obInfor.value = it
        }
    }

    fun checkVersion(appVersion: String, platform: String) {
        return subscribeNotDisposeNotLoading(
                restApi.getVersion(appVersion, platform)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obCheckVersion.value = it
        }
    }

}