package io.driverdoc.testapp.ui.main.dashboard

import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.ItemSong
import io.driverdoc.testapp.data.model.trip.IncompleTripsRespone
import io.driverdoc.testapp.data.model.trip.ScheduleTripRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class DashboardModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<DashboardCallback>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obTrip = MutableLiveData<IncompleTripsRespone>()
        get
    val obScheluteTrip = MutableLiveData<ScheduleTripRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun getTrip(auth: String) {
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getIncomple()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obTrip.value = it
        }
    }
    fun getTripSchedule(auth: String) {
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getSchedue()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obScheluteTrip.value = it
        }
    }

}