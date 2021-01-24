package io.driverdoc.testapp.data.model.base.error


class ResponseException : Throwable {
    companion object {
        const val ERROR_INTERNET = 100
        const val ERROR_TIME_OUT = 101
        const val ERROR_UNKNOWN = 102
        const val ERROR_INTERNET_MESSAGE = "Error connect network"
    }
    val errorResponse: ErrorResponse?
    val otherStatusCode:Int?
    constructor(message: String, errorResponse: ErrorResponse) : super(message) {
        this.errorResponse = errorResponse
        otherStatusCode = null
    }
    constructor(message: String, otherStatusCode: Int) : super(message) {
        this.otherStatusCode = otherStatusCode
        errorResponse = null
    }
}