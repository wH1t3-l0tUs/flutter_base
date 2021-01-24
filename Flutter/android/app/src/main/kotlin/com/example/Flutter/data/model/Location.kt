package io.driverdoc.testapp.data.model

class Location {
    var id_location: String? = null
    var name: String? = null
    var address: String? = null
    var city: String? = null
    var state: String? = null
    var zipcode: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var longitude: String? = null
    var latitude: String? = null
}

data class Lo(
        var lng: Double? = null,
        var lat: Double? = null
)