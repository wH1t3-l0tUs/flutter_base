package io.driverdoc.testapp.data.model

class CCPCountry () {
    var nameCode: String = ""
    var phoneCode: String = ""
    var name: String = ""
    var flagResID: Int? = null

    constructor(nameCode: String,  phoneCode: String, name: String,  flagResID: Int) : this() {
        this.nameCode = nameCode
        this.phoneCode = phoneCode
        this.name = name
        this.flagResID = flagResID
    }
}