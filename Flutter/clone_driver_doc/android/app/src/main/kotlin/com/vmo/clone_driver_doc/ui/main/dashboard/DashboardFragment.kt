package io.driverdoc.testapp.ui.main.dashboard

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.lifecycle.Observer
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveDataNotifi
import io.driverdoc.testapp.data.model.DataNotifi
import io.driverdoc.testapp.databinding.DialogNewTripBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference

class DashboardFragment : BaseMvvmFragment<DashboardCallback, DashboardModel>(), View.OnClickListener, DashboardCallback {
    private var mLastClickTime: Long = 0
    private var isHide = false
    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_close -> {
                onBackRoot()
            }
            R.id.btn_acc -> {
                OpenFragmentUtils.openAccountFragment(getBaseActivity().supportFragmentManager, DashboardFragment::class.java)
            }
            R.id.btn_score -> {
                OpenFragmentUtils.openScoreCardFragment(getBaseActivity().supportFragmentManager, DashboardFragment::class.java)
            }
            R.id.btn_schedule -> {
                SharedPfPermissionUtils.saveNewTrip(context!!, false)
                OpenFragmentUtils.openScheduleFragment(getBaseActivity().supportFragmentManager, DashboardFragment::class.java)
            }
            R.id.btn_income -> {
                OpenFragmentUtils.openIncompleteFragment(getBaseActivity().supportFragmentManager, DashboardFragment::class.java)

            }
        }
    }

    private var dialog: android.app.AlertDialog? = null
    private fun showDialog() {
        val binding = DialogNewTripBinding.inflate(LayoutInflater.from(context!!))
        dialog = android.app.AlertDialog.Builder(context!!).create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setView(binding.getRoot())
        binding.btnNo.setOnClickListener {
            dialog!!.dismiss()
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTrip(it) }
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }

        }
        binding.btnYes.setOnClickListener {
            dialog!!.dismiss()
            SharedPfPermissionUtils.saveNewTrip(context!!, false)
            (activity as MainActivity).openSplashActivity()
        }

        val window: Window = dialog!!.window!!
        val wlp = window.getAttributes()
        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialog!!.setCancelable(false)
        if (null != dialog && !dialog!!.isShowing) {
            dialog!!.show()
        }
        liveDataNotifi.postValue(DataNotifi())

    }

    override fun createModel(): DashboardModel {
        val model = DashboardModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(DashboardModel::class.java)

    }

    override fun onBackRoot() {
        if (!SharedPfPermissionUtils.getHasTrips(context!!)
        ) {
            return
        }
        super.onBackRoot()
    }

    override fun getLayoutMain() = R.layout.fragment_dashboard

    override fun setEvents() {
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().btnAcc.setOnClickListener(this)
        getDataBinding().btnScore.setOnClickListener(this)
        getDataBinding().btnSchedule.setOnClickListener(this)
        getDataBinding().btnIncome.setOnClickListener(this)
    }

    override fun initComponents() {
        liveDataNotifi.observe(this, androidx.lifecycle.Observer {
            if (it.data.equals("NEW_ACTIVE_TRIP") || it.data.equals("NEW_SCHEDULE_TRIP")) {
                if (!isHide) {
                    dialog?.let {
                        it.dismiss()
                    }
                    showDialog()
                    SharedPfPermissionUtils.saveNewTrip(context!!, true)
                }
            }
        })
        SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTrip(it) }
        SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }

        getDataBinding().btnClose.setColorFilter(resources.getColor(R.color.black))
        if (SharedPfPermissionUtils.getHasTrips(context!!)) {
            getDataBinding().btnClose.visibility = View.VISIBLE
        } else {
            getDataBinding().btnClose.visibility = View.INVISIBLE
        }
        liveData.observe(this, Observer {
            if (it == 4) {
                getDataBinding().btnClose.visibility = View.INVISIBLE
                liveData.postValue(-1)
            }
            if (it == 3) {
                SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTrip(it) }
                SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }
                liveData.postValue(-1)
            }
            if (it == Constants.RELOAD_TRIP) {
                SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTrip(it) }
                SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }
                liveData.postValue(-1)
            }
        })

        mModel.obTrip.observe(this, Observer {
            if (it.getSucces()) {
                it.getData()?.total_incomplete_trip?.let {
                    if (it > 0) {
                        getDataBinding().tvCountIncome.visibility = View.VISIBLE
                        getDataBinding().tvCountIncome.setText(it.toString())
                    }else{
                        getDataBinding().tvCountIncome.visibility = View.GONE
                    }
                }
            }
        })
        mModel.obScheluteTrip.observe(this, Observer {
            if (it.getSucces()) {
                it.getData()?.total_schedule_trip?.let {
                    if (it > 0) {
                        getDataBinding().tvCountShedule.visibility = View.VISIBLE
                        getDataBinding().tvCountShedule.setText(it.toString())
                    }else{
                        getDataBinding().tvCountShedule.visibility = View.GONE

                    }

                }
            }
        })

        mModel.callBack = WeakReference(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isHide = hidden
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentDashboardBinding
}