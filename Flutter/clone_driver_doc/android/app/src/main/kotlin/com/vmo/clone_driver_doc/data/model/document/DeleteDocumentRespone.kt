package io.driverdoc.testapp.data.model.document

import io.driverdoc.testapp.data.model.base.BaseResponse

class DeleteDocumentRespone : BaseResponse<List<UploadDocument>>() {
}

class UploadDocument {
    var key: String? = null
    var name: String? = null
    var url: String? = null
    var type: String? = null
    var size: String? = null
}

class ScoreCardRepone : BaseResponse<ScoreCard>() {

}

class ScoreCard {
    var total_trips: Int? = null
    var on_time: String? = null
    var document: String? = null
}