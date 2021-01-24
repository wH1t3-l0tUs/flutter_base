package io.driverdoc.testapp.data.model.document

import io.driverdoc.testapp.data.model.base.BaseResponse

class PresignRespone : BaseResponse<MutableList<String>>() {
}

class PresignRequest {
    var idTrip: String? = null
}