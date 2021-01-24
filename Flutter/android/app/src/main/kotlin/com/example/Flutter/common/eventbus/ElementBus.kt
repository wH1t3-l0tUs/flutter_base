package com.vmodev.realmmvp.eventbus

import io.driverdoc.testapp.common.eventbus.BaseAction


/**
 * Created by ducnd on 8/18/17.
 */
class ElementBus(id: String) {
    val listAction: MutableList<BaseAction> = mutableListOf()
    val id: String = id
}