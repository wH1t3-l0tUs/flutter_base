package io.driverdoc.testapp.ui.start.started

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.login.LoginRequest
import io.driverdoc.testapp.data.model.login.LoginRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.Executor

class StartedModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<StartedCallBack>(appDatabase, interactCommon, schedulers) {
    private val restApi: RestApi
    val obPhone = MutableLiveData<LoginRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun login(phoneNumber: String, countryCode: String, osVersion: String, ip: String) {
        val request = LoginRequest()
        request.phoneNumber = phoneNumber.replace("(", "").replace(")", "").replace(" ", "").replace("-", "")
        request.countryCode = countryCode
        request.osVersion = "ANDROID " + osVersion
        request.ipPublic = ip
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))
        return subscribeNotDispose(
                restApi.sendOtp(body)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obPhone.value = it
        }
    }
}