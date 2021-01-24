package io.driverdoc.testapp.ui.base.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelProviderFactory<V : Any> : ViewModelProvider.Factory {
    private val viewModel: V

    constructor(viewMode: V) {
        this.viewModel = viewMode
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModel.javaClass)) {
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}