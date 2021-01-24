package io.driverdoc.testapp.ui.main.acc

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.login.VerifiCodeRespone
import io.driverdoc.testapp.data.model.user.CreateUserRequest
import io.driverdoc.testapp.data.model.user.UserRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.Executor

class AccountModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<AccountCallBack>(appDatabase, interactCommon, schedulers) {
    private var restApi: RestApi
    val obUser = MutableLiveData<UserRespone>()
        get
    val obInfor = MutableLiveData<VerifiCodeRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

    fun updateUser(phoneNumber: String, email: String, firstName: String, lastName: String, expDate: String, cdlNumber: String, cdlState: String, auth: String) {
        val request = CreateUserRequest()
        request.phoneNumber = phoneNumber
        request.email = email
        request.firstName = firstName
        request.lastName = lastName
        request.expDate = expDate
        request.cdlNumber = cdlNumber
        request.cdlState = cdlState
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))
        return subscribeNotDispose(
                restApi.updateDriver(body)
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obUser.value = it
        }
    }
    fun getInfor(auth: String) {
        val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeNotDispose(
                restApi.getInfor()
                        .subscribeOn(Schedulers.from(schedulers))
                        .observeOn(AndroidSchedulers.mainThread())
        ) {
            obInfor.value = it
        }
    }
}