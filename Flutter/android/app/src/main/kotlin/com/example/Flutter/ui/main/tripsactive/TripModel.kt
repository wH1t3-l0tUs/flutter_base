package io.driverdoc.testapp.ui.main.tripsactive

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.document.DeleteDocumentRespone
import io.driverdoc.testapp.data.model.map.MapRespone
import io.driverdoc.testapp.data.model.trip.ManualRequest
import io.driverdoc.testapp.data.model.trip.ManualResponse
import io.driverdoc.testapp.data.model.trip.ScheduleTripRespone
import io.driverdoc.testapp.data.model.trip.TripRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils.saveTitleDocument
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import java.util.concurrent.Executor

class TripModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<TripCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obTrip = MutableLiveData<TripRespone>()
        get
    val obMap = MutableLiveData<MapRespone>()
        get

    val postDocument = MutableLiveData<DeleteDocumentRespone>()
        get

    val obManual = MutableLiveData<ManualResponse>()
        get

    val obScheluteTrip = MutableLiveData<ScheduleTripRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun getTrip(auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoadingActiveTrip(
                restApi.getActiveTrip()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obTrip.value = it
        }
    }

    fun upDocument(document_type: String, id_trip: String, filePaths: ArrayList<String>, auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        Collections.sort(filePaths)
        for (i in 0 until filePaths.sortedWith(compareBy({ it })).size) {
            val file = File(filePaths[i])
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
        }


        if (MVVMApplication.listText.size > 0) {
            val listText: MutableList<String> = mutableListOf()
            for (i in 0 until MVVMApplication.listTextCheck.size) {
                if (MVVMApplication.listTextCheck[i]){
                    listText.add(MVVMApplication.listText[i])
                }
            }

            if (listText.size > 0){
                for (i in 0 until listText.size) {
                    builder.addFormDataPart("content", listText[i])
                    if (i == 0){
                        if (listText[i].toUpperCase(Locale.ROOT).contains("BILL OF LADING")) {
                            builder.addFormDataPart("documentType", "Bill of Lading")
                        } else if (listText[i].toLowerCase(Locale.ROOT).contains("delivery")) {
                            builder.addFormDataPart("documentType", "Proof of Delivery")
                        } else {
                            builder.addFormDataPart("documentType", "Trip Document")
                        }
                    }
                }
            }else {
                builder.addFormDataPart("content", "")
                builder.addFormDataPart("documentType", "Trip Document")
            }

        } else {
            builder.addFormDataPart("content", "")
            builder.addFormDataPart("documentType", "Trip Document")
        }

        Log.d("------size: ", MVVMApplication.listText.size.toString())
        Log.d("------size: ", MVVMApplication.listTextCheck.size.toString())
        builder.addFormDataPart("idTrip", id_trip)

        val requestBody = builder.build()
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        setIsLoading(true)
        return subscribeNotDispose(
                restApi.postDocument(requestBody)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            setIsLoading(false)
            postDocument.value = it
        }
    }


    fun getTripMap(origin: String, destination: String, key: String, mode: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofitMapApi(endpoint = Constants.BASE_URL_MAP, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getLaLo(origin, destination, mode, key)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            obMap.value = it
        }
    }

    fun updateManual(idTrip: String, departureTime: String, arrivalTime: String, longitude: String, latitude: String, auth: String) {
        val request = ManualRequest()
        request.idTrip = idTrip
        request.departureTime = departureTime
        request.arrivalTime = arrivalTime
        request.longitude = longitude
        request.latitude = latitude
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))
        setIsLoading(true)
        return subscribeNotDisposeTrip(
                restApi.updateManual(body)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            setIsLoading(false)
            obManual.value = it
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