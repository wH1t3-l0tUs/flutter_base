package io.driverdoc.testapp.ui.start.acctype

import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.data.local.AppDatabase
import io.driverdoc.testapp.data.model.login.LoginRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.InteractCommon
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import java.util.concurrent.Executor

class AccountTypeModel(appDatabase: AppDatabase, interactCommon: InteractCommon, schedulers: Executor) :
        BaseViewModel<AccountTypeCallBack>(appDatabase, interactCommon, schedulers) {
    private val restApi: RestApi
    val obPhone = MutableLiveData<LoginRespone>()
        get

    init {
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME).create(RestApi::class.java)
    }

}