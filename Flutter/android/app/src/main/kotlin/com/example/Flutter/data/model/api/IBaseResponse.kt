package io.driverdoc.testapp.data.model.api

interface IBaseResponse {
    fun getErrorCode(): Int
    fun getMsg(): String?
    fun getStatus():Int
}