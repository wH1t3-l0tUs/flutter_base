package io.driverdoc.testapp.data.model.trip

import io.driverdoc.testapp.data.model.base.BaseResponse

class Document {
    var id_document: Int? = null
    var id_enterprise: String? = null
    var id_trip: String? = null
    var url: String? = null
    var page: String? = null
    var created_at: String? = null
    var updated_at: String? = null
    var documentIdTrip: String? = null
}

class ViewDocumentRespone : BaseResponse<MutableList<Document>>() {

}