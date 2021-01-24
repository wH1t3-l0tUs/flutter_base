package io.driverdoc.testapp.data.model.trip

import io.driverdoc.testapp.data.model.Location
import io.driverdoc.testapp.data.model.base.BaseResponse

class TripRespone : BaseResponse<IncomeTrip>() {
//    var name: String? = null
//        get
//        set
//    var addres: String? = null
//        get
//        set
//    var day: String? = null
//        get
//        set
//    var time: String? = null
//        get
//        set

}

class Trip {
    var id_load: String? = null
    var id_enterprise: String? = null
    var id_trip: Int? = null
    var realtime_start: String? = null
    var realtime_arrival: String? = null
    var departure_address: String? = null
    var arrival_address: String? = null
    var trip_number: String? = null
    var departure: String? = null
    var arrival: String? = null
    var arrival_status: Boolean? = null
    var departure_status: Boolean? = null
    var time_start: String? = null
    var departure_latitude: String? = null
    var departure_longitude: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var document_status: String? = null
    var loadIdLoad: String? = null
    var locationIdLocation: String? = null
    var latest_time: String? = null
    var location: Location? = null
    var is_appointment: Int? = null
}

