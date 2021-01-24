package io.driverdoc.testapp.data.model.user

import io.driverdoc.testapp.data.model.base.BaseResponse

class UserRespone:BaseResponse<User>() {
     //


}
class User{
    var first_name: String? = null
    var last_name: String? = null
    var phone_number: String? = null
    var email: String? = null
    var cdl_number: String? = null
    var cdl_state: String? = null
    var exp_date: String? = null
}