package io.driverdoc.testapp.data.model.base

import com.google.gson.annotations.SerializedName

open class BaseResponse<Data> : BaseResponseService {
    override fun getSucces(): Boolean {
        return success!!
    }

    @SerializedName("success")
    private var success: Boolean? = null

    @SerializedName("message")
    private var message = ""

    @SerializedName("data")
    private var data: Data? = null

    @SerializedName("errorCode")
    private var errorCode = ""

    override fun getMessage(): String {
        return message
    }

    fun getData(): Data? {
        return data!!
    }

    fun setData(data: Data) {
        this.data = data;
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun setSuccess(success: Boolean) {
        this.success = success
    }

    fun getSuccess(): Boolean {
        return success!!
    }

    fun setErrorCode(errorCode: String) {
        this.errorCode = errorCode
    }

    fun getErrorCode(): String {
        return errorCode
    }
}