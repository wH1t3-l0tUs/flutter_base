package io.driverdoc.testapp.data.model.login

class VerifiCodeRequest {
    var code: String? = null
    var phoneNumber: String? = null
    var countryCode: String? = null
    var osVersion: String? = null
    var ipPublic: String? = null
}