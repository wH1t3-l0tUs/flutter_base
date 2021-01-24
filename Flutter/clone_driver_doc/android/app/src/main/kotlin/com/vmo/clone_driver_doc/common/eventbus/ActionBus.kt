package io.driverdoc.testapp.common.eventbus

interface ActionBus<Data> :BaseAction {
    fun call(data: Data)
}