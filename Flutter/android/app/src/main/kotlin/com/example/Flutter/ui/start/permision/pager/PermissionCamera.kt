package io.driverdoc.testapp.ui.start.permision.pager

import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.base.fragment.BaseFragment

class PermissionCamera : BaseFragment() {
    override fun getLayoutMain()= R.layout.fragment_camera

    override fun setEvents() {
    }

    override fun initComponents() {
    }
    fun getDataBinding()= mBinding as io.driverdoc.testapp.databinding.FragmentCameraBinding
}