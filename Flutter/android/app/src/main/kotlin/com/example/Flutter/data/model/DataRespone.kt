package io.driverdoc.testapp.data.model

import io.driverdoc.testapp.data.model.base.BaseResponse

/**
 * Created by khaipc on 30,December,2019
 */
class VersionRespone : BaseResponse<Version>() {

}

class Version {
    var appVersionResult: String? = null
}

class DataNotifi {
    var data: String? = null
    var tripNumber: String? = null
}