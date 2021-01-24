package io.driverdoc.testapp.ui.main.incompletrip

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
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.main.incompltripdetail.FragmentCallback
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils

class TripCompleteFragment : BaseMvvmFragment<TripCompleteCallBack, TripCompleteModel>(), TripCompleteAdapter.IStoreAdapter, View.OnClickListener, FragmentCallback {
    private var mLastClickTime: Long = 0
    private var isHide = false
    private var isDetail = false
    private var listUrl: ArrayList<String>? = null
    private var trips: MutableList<IncomeTrip>? = null
    private var dialog: android.app.AlertDialog? = null

    override fun createModel(): TripCompleteModel {
        val model = TripCompleteModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(TripCompleteModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_incomp_trip
    override fun setEvents() {
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().refresh.setOnRefreshListener {
            getDataBinding().tvNoTrip.visibility = View.GONE
            mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
        }
    }
    override fun initComponents() {
        MVVMApplication.liveDataNotifi.observe(this, androidx.lifecycle.Observer {
            if (it.data.equals("NEW_ACTIVE_TRIP") || it.data.equals("NEW_SCHEDULE_TRIP")) {
                if (!isHide) {
                    dialog?.dismiss()
                    showDialog()
                }
            }
        })
        trips = mutableListOf()
        initData()
        Handler().postDelayed({
            mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
        }, 300)
        mModel.obTrip.observe(this, Observer {
            if (mModel.obTrip.value!!.getSucces()) {
                getDataBinding().refresh.isRefreshing = false
                if (MVVMApplication.isBack){
                    MVVMApplication.isBack = false
                    activity?.onBackPressed()
                }
                trips!!.clear()
                mModel.obTrip.value!!.getData()?.let {
                    it.trips?.let { it1 -> trips!!.addAll(it1) }
                    if (trips!!.size > 0) {
                        getDataBinding().rcView.visibility = View.VISIBLE
                        getDataBinding().tvNoTrip.visibility = View.GONE
                    } else {
                        getDataBinding().rcView.visibility = View.GONE
                        getDataBinding().tvNoTrip.visibility = View.VISIBLE
                    }
                    getDataBinding().rcView.adapter!!.notifyDataSetChanged()
                }

            }
        })


        liveData.observe(this, Observer {
            if (it == 3) {
                isDetail = true
//                Handler().postDelayed({
                    mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                    liveData.postValue(-1)
//                }, 3000)
            }
            if (it == Constants.RELOAD_INCOMPLE_TRIP) {
                mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
               liveData.postValue(-1)
            }
        })

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

    override fun onBackRoot() {
        super.onBackRoot()
        liveData.postValue(Constants.RELOAD_TRIP)
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
        listUrl = ArrayList()
        try {
            trips!!.get(position).documents?.let {
                for (i in 0..it.size - 1) {
                    it.get(i).url?.let { listUrl?.add(it) }
                }
            }

            if (null != trips!!.get(position).document_type) {
                OpenFragmentUtils.openIncompleDetailFragment(getBaseActivity().supportFragmentManager, TripCompleteFragment::class.java,
                        trips!!.get(position).trip_number, trips!!.get(position).id_trip, trips!!.get(position).realtime_arrival, trips!!.get(position).realtime_departure, listUrl,
                        trips!!.get(position).departure_status, trips!!.get(position).arrival_status!!, trips!!.get(position).document_status!!, "Incomplete", trips!!.get(position).document_type!!)
            } else {
                OpenFragmentUtils.openIncompleDetailFragment(getBaseActivity().supportFragmentManager, TripCompleteFragment::class.java,
                        trips!!.get(position).trip_number, trips!!.get(position).id_trip, trips!!.get(position).realtime_arrival, trips!!.get(position).realtime_departure, listUrl,
                        trips!!.get(position).departure_status, trips!!.get(position).arrival_status!!, trips!!.get(position).document_status!!, "Incomplete", "Trip DocumentLocal")

            }

        } catch (e: KotlinNullPointerException) {

        }
    }


    private fun showDialog() {
        val binding = DialogNewTripBinding.inflate(LayoutInflater.from(context!!))
        dialog = android.app.AlertDialog.Builder(context!!).create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setView(binding.getRoot())
        binding.btnNo.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.btnYes.setOnClickListener {
            dialog!!.dismiss()
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
        MVVMApplication.liveDataNotifi.postValue(DataNotifi())

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isHide = hidden
    }

    private fun initData() {
        getDataBinding().rcView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        getDataBinding().rcView.adapter = TripCompleteAdapter(this)
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentIncompTripBinding

    override fun onEventSent(yourEvent: String?) {

    }
}