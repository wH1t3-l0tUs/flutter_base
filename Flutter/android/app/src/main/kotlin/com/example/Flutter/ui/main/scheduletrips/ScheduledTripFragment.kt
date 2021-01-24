package io.driverdoc.testapp.ui.main.scheduletrips

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.model.DataNotifi
import io.driverdoc.testapp.data.model.trip.IncomeTrip
import io.driverdoc.testapp.databinding.DialogNewTripBinding
import io.driverdoc.testapp.databinding.FragmentScheduledTripBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.main.dashboard.DashboardFragment
import io.driverdoc.testapp.ui.main.incompletrip.TripCompleteAdapter
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference

class ScheduledTripFragment : BaseMvvmFragment<ScheduledTripCallBack, ScheduledTripModel>(), ScheduledTripCallBack, TripCompleteAdapter.IStoreAdapter, ScheduleTripAdapter.IStoreAdapter, View.OnClickListener {
    private var mLastClickTime: Long = 0
    private var isHide = false
    private var doneActive = false
    private var doneSchedule = false
    private var trips: MutableList<IncomeTrip>? = null
    private var tripsSchedule: MutableList<IncomeTrip>? = null

    override fun createModel(): ScheduledTripModel {
        val model = ScheduledTripModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(ScheduledTripModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_scheduled_trip
    override fun setEvents() {
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().refresh.setOnRefreshListener {
            getDataBinding().tvNoTrip.visibility = View.GONE
            mModel.getTripSchedule(SharedPfPermissionUtils.getToken(context!!).toString())
            mModel.getIncomplTrip(SharedPfPermissionUtils.getToken(context!!).toString())
        }
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_close -> {
                onBackRoot()
            }
        }
    }


    override fun countSchedule(): Int {
        if (null == tripsSchedule) {
            return 0
        }
        return tripsSchedule!!.size
    }

    override fun getDataSchedule(position: Int) = tripsSchedule!!.get(position)

    override fun onClickItemSchelude(position: Int) {

    }

    override fun count(): Int {
        if (null == trips) {
            return 0
        }
        return trips!!.size
    }

    override fun getData(position: Int) = trips!!.get(position)
    override fun onClickItem(position: Int) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        if (SharedPfPermissionUtils.getHasTrips(context!!)) {
            liveData.postValue(Constants.RELOAD_ACTIVE_TRIP)
            back()
        } else {
            (activity as MainActivity).openSplashActivity()
        }
    }

    private fun back() {
        val ac = getBaseActivity()
        val step1 = ac.supportFragmentManager.findFragmentByTag(ScheduledTripFragment::class.java.name)
        if (step1 != null && step1 is ScheduledTripFragment) {
        }
        ac.onBackRoot()
        val step2 = ac.supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.name)
        if (step2 != null && step2 is DashboardFragment) {
            step2.getDataBinding().root.visibility = View.INVISIBLE
        }
        ac.onBackRoot()
    }



    override fun initComponents() {
        MVVMApplication.liveDataNotifi.observe(this, androidx.lifecycle.Observer {
            if (it.data.equals("NEW_ACTIVE_TRIP") || it.data.equals("NEW_SCHEDULE_TRIP")) {
                if (!isHide) {
                    dialogNewTrip?.dismiss()
                    showDialogNewTrip()
                }
            }
        })
        Handler().postDelayed({
            mModel.getTripSchedule(SharedPfPermissionUtils.getToken(context!!).toString())
            mModel.getIncomplTrip(SharedPfPermissionUtils.getToken(context!!).toString())
        }, 300)

        trips = mutableListOf()
        tripsSchedule = mutableListOf()
        initData()
        getDataBinding().rcActive.visibility = View.GONE
        getDataBinding().rcSchedule.visibility = View.GONE
        getDataBinding().tv2.visibility = View.GONE
        mModel.obIncomplTrip.observe(this, Observer {
            if (mModel.obIncomplTrip.value!!.getSucces()) {
                mModel.obIncomplTrip.value!!.getData()?.trip_number?.let {
                    getDataBinding().rcActive.visibility = View.VISIBLE
                    getDataBinding().tv2.visibility = View.VISIBLE
                }
                mModel.obIncomplTrip.value!!.getData()?.let {
                    trips = mutableListOf()
                    doneActive = true
                    trips!!.add(it)
                    doneActive = true
                    getDataBinding().rcActive.adapter!!.notifyDataSetChanged()
                }
            }
        })
        liveData.observe(this, Observer {
            if (it == 4) {
                doneActive = true
                getDataBinding().refresh.isRefreshing = false
                if (doneSchedule && tripsSchedule!!.size == 0 && trips!!.size == 0) {
                    getDataBinding().tvNoTrip.visibility = View.VISIBLE
                } else {
                    getDataBinding().tvNoTrip.visibility = View.GONE
                }
                liveData.postValue(-1)
            }
            if (it == Constants.RELOAD_SCHEDULE_TRIP) {
                mModel.getTripSchedule(SharedPfPermissionUtils.getToken(context!!).toString())
                mModel.getIncomplTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                liveData.postValue(-1)
            }
        })
        mModel.obScheluteTrip.observe(this, Observer {
            if (mModel.obScheluteTrip.value!!.getSucces()) {
                mModel.obScheluteTrip.value!!.getData()?.let {
                    doneSchedule = true
                    tripsSchedule = mutableListOf()
                    it.data?.let { it1 -> tripsSchedule!!.addAll(it1) }
                    getDataBinding().rcSchedule.visibility = View.VISIBLE
                    if (it.data?.size == 0) {
                        getDataBinding().tv3.visibility = View.GONE
                    } else {
                        getDataBinding().tv3.visibility = View.VISIBLE
                    }
                    if (doneActive && tripsSchedule!!.size == 0 && trips!!.size == 0) {
                        getDataBinding().tvNoTrip.visibility = View.VISIBLE
                    } else {
                        getDataBinding().tvNoTrip.visibility = View.GONE
                    }
                    getDataBinding().refresh.isRefreshing = false

                    getDataBinding().rcSchedule.adapter!!.notifyDataSetChanged()
                }

            }
        })
        mModel.callBack = WeakReference(this)
    }

    private fun initData() {
        getDataBinding().rcActive.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        getDataBinding().rcActive.adapter = TripCompleteAdapter(this)
        getDataBinding().rcActive.setHasFixedSize(true)
        getDataBinding().rcActive.setNestedScrollingEnabled(false)
        getDataBinding().rcSchedule.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        getDataBinding().rcSchedule.adapter = ScheduleTripAdapter(this)
        getDataBinding().rcSchedule.setHasFixedSize(true)
        getDataBinding().rcSchedule.setNestedScrollingEnabled(false)
    }

    private var dialogNewTrip: android.app.AlertDialog? = null
    private fun showDialogNewTrip() {
        val binding = DialogNewTripBinding.inflate(LayoutInflater.from(context!!))
        dialogNewTrip = android.app.AlertDialog.Builder(context!!).create()
        dialogNewTrip!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogNewTrip!!.setView(binding.getRoot())
        binding.btnNo.setOnClickListener {
            dialogNewTrip!!.dismiss()
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }
            SharedPfPermissionUtils.getToken(context!!)?.let {
                mModel.getIncomplTrip(it)
                liveData.postValue(Constants.RELOAD_TRIP)
            }
        }
        binding.btnYes.setOnClickListener {
            dialogNewTrip!!.dismiss()
            (activity as MainActivity).openSplashActivity()
        }

        val window: Window = dialogNewTrip!!.window!!
        val wlp = window.getAttributes()

        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialogNewTrip!!.setCancelable(false)
        if (null != dialogNewTrip && !dialogNewTrip!!.isShowing) {
            dialogNewTrip!!.show()
        }
        MVVMApplication.liveDataNotifi.postValue(DataNotifi())

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isHide = hidden
    }

    fun getDataBinding() = mBinding as FragmentScheduledTripBinding
}