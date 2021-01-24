package io.driverdoc.testapp.ui.main.documentview

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.document.DeleteDocumentRespone
import io.driverdoc.testapp.data.model.document.PresignRequest
import io.driverdoc.testapp.data.model.trip.ViewDocumentRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.concurrent.Executor

class DocumentModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<DocumentViewCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi

    val deleteDocument = MutableLiveData<DeleteDocumentRespone>()
        get
    val getPresign = MutableLiveData<ViewDocumentRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun deleteDocument(id_trip: String, auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        val builder = MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
        builder.addFormDataPart("idTrip", id_trip)
        val requestBody = builder.build()
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDispose(
                restApi.delete(requestBody)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            deleteDocument.value = it
        }
    }

    fun getPresign(id_trip: String, auth: String) {
        val request = PresignRequest()
        request.idTrip = id_trip
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDisposeNotLoading(
                restApi.getPresign(body)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            getPresign.value = it
        }
    }
}