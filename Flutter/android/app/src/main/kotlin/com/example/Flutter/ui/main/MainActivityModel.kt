package io.driverdoc.testapp.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.document.DocumentRespone
import io.driverdoc.testapp.data.model.trip.LocationRequest
import io.driverdoc.testapp.data.model.trip.TripRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class MainActivityModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<MainActivityCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obHasTrip = MutableLiveData<TripRespone>()
        get

    val obHasDone = MutableLiveData<TripRespone>()
        get
    val obNotifi = MutableLiveData<DocumentRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }
    fun location(longitude: Double, latitude: Double, auth: String): Disposable? {
        val request = LocationRequest()
        request.latitude = latitude
        request.longitude = longitude
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))

        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeHasResultDisposeLocation(
                restApi.postLocation(body)
                        .retryWhen({ error -> error.retry().delay(10, TimeUnit.SECONDS) })
//                        .repeatWhen({ complete -> complete.delay(10, TimeUnit.SECONDS) })
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            MVVMApplication.obLocation.value = it
        }
    }
}