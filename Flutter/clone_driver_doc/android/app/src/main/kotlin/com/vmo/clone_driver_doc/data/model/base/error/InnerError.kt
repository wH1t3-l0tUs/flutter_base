package com.vmodev.better.data.model.base.error

import com.google.gson.annotations.SerializedName
import java.util.*

data class InnerError(
        @SerializedName("name")
        val name:String,
        @SerializedName("message")
        val message:String,
        @SerializedName("expiredAt")
        val expiredAt:Date
)