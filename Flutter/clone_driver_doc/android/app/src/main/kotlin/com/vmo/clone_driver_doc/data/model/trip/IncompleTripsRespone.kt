package io.driverdoc.testapp.data.model.trip

import io.driverdoc.testapp.data.model.Location
import io.driverdoc.testapp.data.model.base.BaseResponse

class IncompleTripsRespone : BaseResponse<DataResponeTripIncom>() {
}

class DataResponeTripIncom {
    var trips: MutableList<IncomeTrip>? = null
    var total_incomplete_trip: Int? = null

}

class IncomeTrip {
    var id_trip: Int? = null
    var latest_time: String? = null
    var earliest_time: String? = null
    var departure: String? = null
    var trip_number: String? = null
    var realtime_departure = ""
    var realtime_arrival = ""
    var realtime_start = ""
    var documents: MutableList<Document>? = null
    var location: Location? = null
    var arrival_status: Boolean? = null
    var departure_status: Boolean? = null
    var document_status: Boolean? = null
    var document_type: String? = null
    var is_appointment: Int? = null
    var appointment_number: String? = null
    var load: Load? = null
    var note: String? = null
    var trip_type: Int? = null
    var isBlock: Boolean? = null
    var is_manual_time: Boolean? = null
    var state: String? = null

}

class Load {
    var reference: String? = null
    var bill_of_lading: String? = null
    var purchase_order: String? = null
}

class ScheduleTripRespone : BaseResponse<DataResponeSchedule>() {

}

class DataResponeSchedule {
    var total_schedule_trip: Int? = null
    var data: MutableList<IncomeTrip>? = null
}