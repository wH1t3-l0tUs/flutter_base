package io.driverdoc.testapp.data.model.base.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("errorCode")
        val errorCode: String?,
        @SerializedName("messages")
        val messages: String?
)