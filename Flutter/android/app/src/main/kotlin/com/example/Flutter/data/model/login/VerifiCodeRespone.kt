package io.driverdoc.testapp.data.model.login

import io.driverdoc.testapp.data.model.base.BaseResponse

class VerifiCodeRespone : BaseResponse<Verifi>() {
}

class Verifi() {
    var jwtToken: String? = null
    var phone_number: String? = null
    var first_name: String? = null
    var id_driver: String? = null
    var last_name: String? = null
    var is_complete: Boolean? = null
    var cdl_number: String? = null
    var email: String? = null
    var exp_date: String? = null
    var cdl_state: String? = null
}