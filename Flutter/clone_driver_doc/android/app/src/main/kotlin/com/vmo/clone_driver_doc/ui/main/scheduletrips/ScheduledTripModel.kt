package io.driverdoc.testapp.ui.main.scheduletrips

import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.trip.IncompleTripsRespone
import io.driverdoc.testapp.data.model.trip.ScheduleTripRespone
import io.driverdoc.testapp.data.model.trip.TripRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

class ScheduledTripModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<ScheduledTripCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obIncomplTrip = MutableLiveData<TripRespone>()
        get
    val obScheluteTrip = MutableLiveData<ScheduleTripRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun getIncomplTrip(auth: String) {
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDispose(
                restApi.getActiveTrip()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obIncomplTrip.value = it
        }
    }

    fun getTripSchedule(auth: String) {
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDispose(
                restApi.getSchedue()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obScheluteTrip.value = it
        }
    }

}