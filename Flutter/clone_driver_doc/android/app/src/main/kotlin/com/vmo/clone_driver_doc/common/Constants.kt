package io.driverdoc.testapp.common

import java.util.*

object Constants {
    const val DEBUG = true
    // const val BASE_URL = "https://api-uat.driverdoc.io" //uat
//     const val BASE_URL = "https://api.driverdoc.io" //prod
    const val BASE_URL = "http://18.189.97.50:8080" //test
//        const val BASE_URL = "https://master-api.driverdoc.io" //master
    const val BASE_URL_MAP = "https://maps.googleapis.com"
    const val FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val FORMAT_DATE_TIME_Z = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val CODE_TOKEN_EXPIRE = 401
    const val DURATION_ANIMATION = 500
    val LIST_FORMAT_TIME: List<String> = Arrays.asList(FORMAT_DATE_TIME, FORMAT_DATE_TIME_Z, FORMAT_DATE)
    const val PHONE = "PHONE"
    const val COUNTRY_CODE = "COUNTRY_CODE"
    const val AUTHORIZATION = "Authorization"
    const val BEARER = "Bearer "
    const val NUMBER_TRIP = "NUMBER_TRIP"
    const val ID_TRIP = "ID_TRIP"
    const val TIME_TRIP = "TIME_TRIP"
    const val DEPARTURE = "DEPARTURE"
    const val LIST_URL = "LIST_URL"
    const val DE_STATUS = "DE_STATUS"
    const val DE_DOCUMENT = "DE_DOCUMENT"
    const val TYPE_DOCUMENT = "TYPE_DOCUMENT"
    const val DE_DEP = "DE_DEP"
    const val TYPE = "TYPE"
    const val LOCAL = "LOCAL"
    const val IS_ACTIVE = "ISACTIVE"
    const val EVEN_CHANGE_TIMEZONE = 9
    const val OS_TYPE = "android"
    const val UPDATE_VERSION = "UPDATE_VERSION"
    const val UPDATE_TRIP = "UPDATE_TRIP"
    const val TRIP_UPDATED = "TRIP_UPDATED"
    const val TRIP_UPDATED_DEPARTURE = "TRIP_UPDATED_DEPARTURE"
    const val TRIP_UPDATED_ARRIVAL = "TRIP_UPDATED_ARRIVAL"
    const val TRIP_ARRIVED_SUCCESS = "TRIP_ARRIVED_SUCCESS"
    const val REMOVED_TRIP = "REMOVED_TRIP"
    const val RELOAD_TRIP = 10
    const val RELOAD_SCHEDULE_TRIP = 11
    const val RELOAD_INCOMPLE_TRIP = 12
    const val RELOAD_ACTIVE_TRIP = 13
    const val UPDATE_VERSION_REQUIRED = 14
    const val TRIP_BE_GOING = "TRIP_BE_GOING"
    const val TRIP_COMPLETE = "TRIP_COMPLETE"
    const val VERIFY_APP_VERSION = "VERIFY_APP_VERSION"
    const val URL_GET_IP = "https://api.ipify.org/"
    const val IS_DELETE = "IS_DELETE"
}