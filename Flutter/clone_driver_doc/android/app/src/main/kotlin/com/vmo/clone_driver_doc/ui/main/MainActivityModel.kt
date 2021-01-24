package com.vmo.clone_driver_doc.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.vmo.clone_driver_doc.common.Constants
import com.vmo.clone_driver_doc.common.MVVMApplication
import com.vmo.clone_driver_doc.data.local.AppDatabase
import com.vmo.clone_driver_doc.data.model.document.DocumentRespone
import com.vmo.clone_driver_doc.data.model.trip.LocationRequest
import com.vmo.clone_driver_doc.data.model.trip.TripRespone
import com.vmo.clone_driver_doc.data.remote.ApiHelp
import com.vmo.clone_driver_doc.data.remote.InteractCommon
import com.vmo.clone_driver_doc.data.remote.RestApi
import com.vmo.clone_driver_doc.ui.base.model.BaseViewModel
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

        val headerPare = Pair(com.vmo.clone_driver_doc.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = com.vmo.clone_driver_doc.common.Constants.BASE_URL, formatDate = com.vmo.clone_driver_doc.common.Constants.LIST_FORMAT_TIME, headers = header)
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