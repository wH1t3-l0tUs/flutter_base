package io.driverdoc.testapp.ui.base.callback

import com.google.gson.Gson
import io.driverdoc.testapp.data.model.base.error.ResponseException
import io.driverdoc.testapp.ui.base.BaseViewUI

interface BaseCallBack : BaseViewUI{
    fun onError(id: String, error: ResponseException)
    fun onErrorList(id: String, error: ResponseException)
    fun gson(): Gson
}