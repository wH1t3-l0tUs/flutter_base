package io.driverdoc.testapp.data.model.map

import com.google.gson.JsonArray
import io.driverdoc.testapp.data.model.Lo

class MapRespone {

   var routes :JsonArray?=null
//    val routes: Router? = null
}

class Router {
    var legs: Leg? = null
}

class Leg {
    var steps: JsonArray? = null
}

class Step {
    var end_location: Lo? = null
    var start_location: Lo? = null
}