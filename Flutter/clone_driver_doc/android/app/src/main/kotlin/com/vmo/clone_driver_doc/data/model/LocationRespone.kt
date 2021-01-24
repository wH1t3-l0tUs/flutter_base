package io.driverdoc.testapp.data.model

import io.driverdoc.testapp.data.model.base.BaseResponse

class LocationRespone : BaseResponse<TrackLoca>() {

}

class TrackLoca {
    var distance: Double? = null
    var state: String? = null
    var result: String? = null
    var id_trip: String? = null
    var limit_speed: Double? = null
}