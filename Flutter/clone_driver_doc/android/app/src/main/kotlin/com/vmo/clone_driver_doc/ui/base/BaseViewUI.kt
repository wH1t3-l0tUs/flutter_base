package io.driverdoc.testapp.ui.base

interface BaseViewUI {
    fun getLayoutMain(): Int

    fun setEvents()

    fun initComponents()

    fun onBackRoot()

    fun showMessage(message: String)

    fun showMessage(messageId: Int)

    val isDestroyView: Boolean

    fun onResumeControl()

    fun onPauseControl()

    fun hideKeyBoard(): Boolean
}