package io.driverdoc.testapp.data.model.base.error

import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("success")
        val success: Boolean,
        @SerializedName("messageContent")
        val messageContent: String,
        @SerializedName("messages")
        val messages: Errorr
//        @SerializedName("status")
//        val status: Int,
//        @SerializedName("inter")
//        val inter: InnerError
)

class Errorr {
    val error: MutableList<String>? = null
}