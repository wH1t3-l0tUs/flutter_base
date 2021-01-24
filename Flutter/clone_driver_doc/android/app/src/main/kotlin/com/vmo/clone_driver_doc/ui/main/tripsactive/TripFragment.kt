package io.driverdoc.testapp.ui.main.tripsactive

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.singledateandtimepicker.dialog.SingleDateAndTimePicker
import com.example.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.JsonObject
import com.google.maps.android.PolyUtil
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.isDone
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.common.MVVMApplication.Companion.obLocation
import io.driverdoc.testapp.data.model.trip.IncomeTrip
import io.driverdoc.testapp.databinding.DialogConfirmManualModeBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.HomeRootFragment
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.main.dashboard.DashboardFragment
import io.driverdoc.testapp.ui.main.scheduletrips.ScheduledTripFragment
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.LoadDataBinding.getLocation
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.io.File
import java.lang.ref.WeakReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TripFragment : BaseMvvmFragment<TripCallBack, TripModel>(), TripCallBack, TripAdapter.IStoreAdapter, View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private var mLastClickTime: Long = 0
    private var idTrip: String? = null
    private var endLocation = ""
    private var lat: String? = null
    private var long: String? = null
    private var isHide = false
    private var decodedPath: MutableList<LatLng>? = null
    private var fistShow = false
    private val EXTERNAL = 1
    private var curentLocation: Location? = null
    private var listUrlSchedule: ArrayList<String>? = null
    private var trips = mutableListOf<IncomeTrip>()
    private var mMap: GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var dialog: android.app.AlertDialog? = null
    private var isManual = false
    private var isManualApi = false
    private var realtimeArrival: String? = null
    private var arrivalStatus = false

    override fun createModel(): TripModel {
        val model = TripModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(TripModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_trip

    private fun initPermissionsExternal() {
        val checkPermisonLocation = ActivityCompat.checkSelfPermission(getBaseActivity().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (checkPermisonLocation == PackageManager.PERMISSION_DENIED) {
            //hien thi dialog yeu cau nguoi dung dong y permission nay
            var shouldShow = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shouldShow = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (shouldShow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(Array(1, { Manifest.permission.WRITE_EXTERNAL_STORAGE }), EXTERNAL)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(context)) {
                        requestPermissions(Array(1, { Manifest.permission.WRITE_EXTERNAL_STORAGE }), EXTERNAL)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        SharedPfPermissionUtils.saveIncomplete(context!!, false)
                        (activity as MainActivity).openCapDocumentActivity()
                    }
                }
            }
        }
    }

    override fun count(): Int {
        return trips.size
    }

    override fun getData(position: Int): IncomeTrip {
        return trips.get(position)
    }

    override fun onClickItem(position: Int) {
        getDataBinding().rl3.visibility = View.VISIBLE
        getDataBinding().rl1.visibility = View.GONE
        getDataBinding().flMap.visibility = View.GONE
    }

    private fun checkPermistionLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mModel.getTripMap(getLocation(context!!).latitude.toString() + "," + getLocation(context!!).longitude.toString(), lat!!.toString() + "," + long!!.toString(), resources.getString(R.string.google_maps_key), "driving")
            }
        } else {
            if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(getBaseActivity())
                fusedLocationClient!!.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            location?.let {
                                if (it.longitude != 0.0 && it.latitude != 0.0) {
                                    curentLocation = location
                                    mModel.getTripMap(location.latitude.toString() + "," + location.longitude.toString(), lat!!.toString() + "," + long!!.toString(), resources.getString(R.string.google_maps_key), "driving")
                                }
                            }
                        }
            }
        }
    }

    override fun setEvents() {
        getDataBinding().btnDashboard.setOnClickListener(this)
        getDataBinding().btnCreate.setOnClickListener(this)
        getDataBinding().btnDocument.setOnClickListener(this)
        getDataBinding().btnDone.setOnClickListener(this)
        getDataBinding().btnMap.setOnClickListener(this)
        getDataBinding().btnHere.setOnClickListener(this)
//        getDataBinding().btnCompleteTrip.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n", "MissingPermission")
    override fun initComponents() {
        initView()
        initDataResponse()
        initLiveData()
        trackLocation()
        mModel.callBack = WeakReference(this)
    }

    private fun initView() {
        getDataBinding().btnDocument.visibility = View.GONE
        getDataBinding().flMap.visibility = View.GONE
        SharedPfPermissionUtils.saveHasTrip(context!!, true)
        fistShow = false
        isHide = false
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(context!!)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()
        } else {
            googleApiClient?.disconnect()
            googleApiClient = null
            googleApiClient = GoogleApiClient.Builder(context!!)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build()

        }
        dialogBand = android.app.AlertDialog.Builder(context!!).create()
        initViewGoing()
        if (getDataBinding().tvWarning.visibility == View.VISIBLE) {
            if (trips.size > 0) {
                trips.get(0).isBlock = true
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        } else {
            if (trips.size > 0) {
                trips.get(0).isBlock = false
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initDataResponse() {
        mModel.obTrip.observe(this, androidx.lifecycle.Observer {
            if (mModel.obTrip.value!!.getSucces()) {
                mModel.obTrip.value!!.getData()?.let {

                    if (!StringUtils.isEmpty(it.state) && it.state.equals(Constants.TRIP_COMPLETE)) {
                        initViewDone()
                    }

                    val tri = mutableListOf<IncomeTrip>()
                    tri.add(it)
                    fillData(tri)
                    if (it.is_manual_time != null) {
                        MVVMApplication.isManual = it.is_manual_time!!
                        isManualApi = it.is_manual_time!!

                        if (getDataBinding().rl3.visibility != View.VISIBLE) {
                            if (it.is_manual_time!!) {
                                getDataBinding().btnDone.visibility = View.VISIBLE
                            }
                        }

                    }
                    arrivalStatus = it.arrival_status!!
                    realtimeArrival = it.realtime_arrival

                    if (!StringUtils.isEmpty(it.load?.reference)) {
                        it.load?.reference?.let {

                            getDataBinding().tvContentRefer1.text = it
                        }
                    } else {
                        getDataBinding().tvContentRefer1.text = "N/A"
                    }

                    if (!StringUtils.isEmpty(it.load?.purchase_order)) {
                        it.load?.purchase_order?.let {
                            getDataBinding().tvContentPuchase.text = it
                            getDataBinding().tvContentPuchase1.text = it
                        }
                    } else {
                        getDataBinding().tvContentPuchase.text = "N/A"
                        getDataBinding().tvContentPuchase1.text = "N/A"
                    }

                    if (!StringUtils.isEmpty(it.load?.bill_of_lading)) {
                        it.load?.bill_of_lading?.let {
                            getDataBinding().tvContentBol.text = it
                            getDataBinding().tvContentBol1.text = it
                        }
                    } else {
                        getDataBinding().tvContentBol.text = "N/A"
                        getDataBinding().tvContentBol1.text = "N/A"
                    }

                    if (!StringUtils.isEmpty(it.appointment_number)) {
                        it.appointment_number?.let {

                            getDataBinding().tvContentApp.text = it
                            getDataBinding().tvContentApp1.text = it
                        }
                    } else {
                        getDataBinding().tvContentApp.text = "N/A"
                        getDataBinding().tvContentApp1.text = "N/A"
                    }

                    it.trip_type?.let {
                        if (it == 0) {
                            getDataBinding().tvTripType.text = "Pickup"
                        } else {
                            getDataBinding().tvTripType.text = "Delivery"
                        }
                    }

                    it.is_appointment?.let {
                        if (it == 1) {
                            getDataBinding().tvTimeLate.visibility = View.GONE
                        } else {
                            getDataBinding().tvTimeLate.visibility = View.VISIBLE
                        }
                    }
                    it.location?.address?.let {
                        getDataBinding().tvAddr.text = it
                    }
                    it.location?.name?.let {
                        getDataBinding().tvNameAddr.text = it
                    }
                    it.earliest_time?.let {
                        getDataBinding().tvTime.text = StringUtils.fomatDateSb(it) + " " + StringUtils.fomatTimeNormal(it)
                    }
                    it.latest_time?.let {
                        getDataBinding().tvTimeLate.text = StringUtils.fomatDateSb(it) + " " + StringUtils.fomatTimeNormal(it)
                    }

                    if (StringUtils.isEmpty(it.note)) {
                        getDataBinding().tvNote.visibility = View.GONE
                        getDataBinding().tvNote1.visibility = View.GONE
                        getDataBinding().tvContentNote.visibility = View.GONE
                        getDataBinding().tvContentNote1.visibility = View.GONE
                    } else {
                        it.note?.let {
                            getDataBinding().tvNote.visibility = View.VISIBLE
                            getDataBinding().tvNote1.visibility = View.VISIBLE
                            getDataBinding().tvContentNote.visibility = View.VISIBLE
                            getDataBinding().tvContentNote1.visibility = View.VISIBLE

                            getDataBinding().tvContentNote.text = it
                            getDataBinding().tvContentNote1.text = it
                        }
                    }
                    mModel.obTrip.value?.getData()?.location?.longitude?.let {
                        long = it
                    }
                    mModel.obTrip.value?.getData()?.location?.latitude?.let {
                        lat = it
                    }
                    checkPermistionLocation()
                    mModel.obTrip.value?.getData()?.location!!.name?.let {
                        endLocation = it
                    }

                    if (listUrlSchedule != null && listUrlSchedule!!.size > 0) {
                        getDataBinding().btnCreate.visibility = View.GONE
                    } else {
                        getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                        getDataBinding().btnCreate.visibility = View.VISIBLE
                    }

                }
            } else {
//                if (mModel.obTrip.value != null && mModel.obTrip.value?.getErrorCode().equals("ACTIVE_TRIPS_NOT_FOUND")){
//                (activity as MainActivity).openSplashActivity()
//                OpenFragmentUtils.openDashboardAnimationFragment(getBaseActivity().supportFragmentManager, TripFragment::class.java)

                SharedPfPermissionUtils.saveHasTrip(context!!, false)
                OpenFragmentUtils.openDashboardAnimationFragmentNotBack(getBaseActivity().supportFragmentManager, TripFragment::class.java)
            }

        })
        mModel.obMap.observe(this, androidx.lifecycle.Observer {
            mModel.obMap.value!!.routes?.let {
                if (it.size() > 0) {
                    val lngStart = (it.get(0) as JsonObject).getAsJsonObject("overview_polyline").get("points").asString
                    mMap?.clear()
                    decodedPath?.clear()
                    decodedPath = PolyUtil.decode(lngStart)
                    mMap!!.addPolyline(PolylineOptions()
                            .color(Color.parseColor("#023F62"))
                            .width(16f).addAll(decodedPath))
                    mMap!!.isMyLocationEnabled = true
                    mMap!!.addMarker(MarkerOptions()
                            .position(LatLng(lat!!.toDouble(), long!!.toDouble()))
                            .title(convertAddress(lat!!.toDouble(), long!!.toDouble(), context!!)))
                    initZoomMap()
                }
            }
        })
        mModel.postDocument.observe(this, androidx.lifecycle.Observer {
            MVVMApplication.isManual = false
            MVVMApplication.listText.clear()
            MVVMApplication.listTextCheck.clear()
            listUrlSchedule?.clear()
            getDataBinding().tvStatus.text = ""
            LoadDataBinding.clearApplicationData(context!!).start()
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }
        })

        mModel.obManual.observe(this, androidx.lifecycle.Observer {
            if (mModel.obManual.value!!.getSucces()) {
                showMessage(it.getMessage())
                mModel.obManual.value!!.getData()?.let {
                    isManualApi = it.is_manual_time!!
                    realtimeArrival = it.realtime_arrival
                    if (it.is_manual_time!!) {
                        getDataBinding().btnDone.visibility = View.VISIBLE
                    }
                }
                arrivalStatus = isManual
                if (isManual) {
                    dialogBottomArrival.dismiss()
                    initViewDone()
                } else {
                    dialogBottomDeparture.dismiss()
                    if (listUrlSchedule != null && listUrlSchedule!!.size > 0) {
                        listUrlSchedule?.let {
                            idTrip?.let { it1 ->
                                mModel.upDocument(SharedPfPermissionUtils.getNameDocument(context!!).toString(), it1, it, SharedPfPermissionUtils.getToken(context!!).toString())
                            }
                        }
                    } else {
                        getDataBinding().tvStatus.text = ""
                        SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getTripSchedule(it) }
                    }

                }
            } else {
                if (isManual) {
                    dialogBottomArrival.visible(true, mModel.obManual.value!!.getMessage())
                } else {
                    dialogBottomDeparture.visible(true, mModel.obManual.value!!.getMessage())
                }
            }
        })

        mModel.obScheluteTrip.observe(this, androidx.lifecycle.Observer {
            if (it.getSucces()) {
                it.getData()?.total_schedule_trip?.let {
                    if (it > 0) {
                        initView()
                        mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                        MVVMApplication.isManual = false
                    } else {
                        SharedPfPermissionUtils.saveHasTrip(context!!, false)
                        OpenFragmentUtils.openDashboardAnimationFragmentNotBack(getBaseActivity().supportFragmentManager, TripFragment::class.java)
                    }
                }
            } else {
                initView()
                mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                MVVMApplication.isManual = false
            }
        })
    }

    private fun initLiveData() {
        liveData.observe(this, androidx.lifecycle.Observer {
            if (it == 0) {
                listUrlSchedule!!.clear()
                getDataBinding().btnDocument.visibility = View.GONE
                getDataBinding().ln2.visibility = View.VISIBLE
                if (!MVVMApplication.isManual) {
                    getDataBinding().btnDone.visibility = View.GONE
                }
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnCreate.visibility = View.VISIBLE
                liveData.postValue(-1)
            } else if (it == Constants.RELOAD_ACTIVE_TRIP) {
                listUrlSchedule?.clear()

                initView()
                mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
            }
        })

        isDone.observe(this, androidx.lifecycle.Observer {
            if (it!!) {
                val dir = File(context!!.externalCacheDir, SharedPfPermissionUtils.getIDTrip(context!!).toString())
                if (dir.isDirectory) {
                    val children = dir.list()
                    listUrlSchedule = ArrayList<String>()
                    for (i in children.indices) {
                        if (children.get(i).substring(children.get(i).lastIndexOf(".")).equals(".jpg")) {
                            listUrlSchedule!!.add(Uri.parse(dir.absolutePath + "/" + children[i]).toString())
                        }
                    }
                    getDataBinding().btnDocument.visibility = View.VISIBLE
                    getDataBinding().btnCreate.visibility = View.GONE
                    getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
                    if (listUrlSchedule!!.size < 2) {
                        getDataBinding().tvCount.text = listUrlSchedule?.size.toString() + " page"
                    } else {
                        getDataBinding().tvCount.text = listUrlSchedule?.size.toString() + " page(s)"
                    }
                    getDataBinding().btnDone.visibility = View.VISIBLE

//                    Handler().postDelayed({
                        var tvBill = ""
                        if (MVVMApplication.listText.size > 0) {
                            val listText: MutableList<String> = mutableListOf()
                            try {
                                for (i in 0 until MVVMApplication.listTextCheck.size) {
                                    if (MVVMApplication.listTextCheck[i]) {
                                        listText.add(MVVMApplication.listText[i])
                                        break
                                    }
                                }
                            } catch (e: java.lang.IndexOutOfBoundsException) {

                            }

                            if (listText.size > 0) {
                                if (listText[0].toUpperCase(Locale.ROOT).contains("BILL OF LADING")) {
                                    tvBill = "Bill of Lading"
                                } else if (listText[0].toLowerCase(Locale.ROOT).contains("delivery")) {
                                    tvBill = "Proof of Delivery"
                                } else {
                                    tvBill = "Trip Document"
                                }
                            } else {
                                tvBill = "Trip Document"
                            }
                        } else {
                            tvBill = "Trip Document"
                        }
                        getDataBinding().tvBill.text = tvBill
//                    }, 3000)

                }
                isDone.postValue(false)
            }
        })
        MVVMApplication.liveDataNotifi.observe(this, androidx.lifecycle.Observer {
            if (it.data.equals(Constants.TRIP_UPDATED_DEPARTURE) || it.data.equals(Constants.TRIP_UPDATED_ARRIVAL)) {
                mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
            } else if (it.data.equals(Constants.TRIP_ARRIVED_SUCCESS)) {
                arrivalStatus = false
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnCreate.visibility = View.VISIBLE

                if (listUrlSchedule != null && listUrlSchedule!!.size > 0) {
                    getDataBinding().btnCreate.visibility = View.GONE
                } else {
                    getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                    getDataBinding().btnCreate.visibility = View.VISIBLE
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun fillData(it: MutableList<IncomeTrip>) {
        trips.clear()
        trips.addAll(it)
        initData()
        if (getDataBinding().tvWarning.visibility == View.VISIBLE) {
            if (it.size > 0) {
                it.get(0).isBlock = true
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        } else {
            if (it.size > 0) {
                it.get(0).isBlock = false
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        }
        it.get(0).trip_number?.let {

            getDataBinding().tvContent.text = it

        }
    }

    private fun initData() {
        getDataBinding().rcView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
        getDataBinding().rcView.adapter = TripAdapter(this)
    }

    override fun onLocationChanged(location: Location?) {
        lastKnownLatLng = LatLng(location!!.latitude, location.longitude)
        curentLocation = location
        updateTrack()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        startLocationUpdates(); }

    override fun onConnectionSuspended(p0: Int) {
    }

    @SuppressLint("RestrictedApi", "MissingPermission")
    protected fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this)
    }

    private fun updateTrack() {
        val points = gpsTrack!!.points
        points.add(lastKnownLatLng)
        gpsTrack!!.points = points
    }

    private
    var gpsTrack: Polyline? = null

    private
    var googleApiClient: GoogleApiClient? = null

    private
    var lastKnownLatLng: LatLng? = null

    private
    var address: String? = null

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        // set up UI
        mMap!!.uiSettings.isZoomControlsEnabled = false
        mMap!!.uiSettings.isMyLocationButtonEnabled = true
        mMap!!.uiSettings.isZoomGesturesEnabled = true
        mMap!!.uiSettings.isCompassEnabled = true
        mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    private fun initZoomMap() {
        val builder = LatLngBounds.Builder()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            builder.include(getLocation(context!!))
        } else {
            builder.include(curentLocation?.latitude?.let { curentLocation?.longitude?.let { it1 -> LatLng(it, it1) } })
        }
        builder.include(LatLng(lat!!.toDouble(), long!!.toDouble()))
        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels - 300
        val height = resources.displayMetrics.heightPixels - 500
        val padding = (width * 0.2).toInt() // offset from edges of the map 10% of screen
        try {
            val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            val mapView = mapFragment.view
            val locationButton = (mapView!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
            val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)
        } catch (e: Exception) {
        }
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        mMap!!.animateCamera(cu)
    }

    override fun onStop() {
        super.onStop()
        googleApiClient?.disconnect()
        liveData.postValue(-1)
    }

    override fun onResumeControl() {
        super.onResumeControl()

        if (getDataBinding().tvWarning.visibility == View.VISIBLE) {
            if (trips.size > 0) {
                trips.get(0).isBlock = true
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        } else {
            if (trips.size > 0) {
                trips.get(0).isBlock = false
                getDataBinding().rcView.adapter?.notifyDataSetChanged()
            }
        }
        dialogBand?.let {
            dialogBand!!.dismiss()
        }
        googleApiClient?.let {
            if (it.isConnected) {
                startLocationUpdates()
            }
        }
        if (!isHide) {
            mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
        }

        trackLocation()
    }

    private fun convertAddress(latitude: Double, longitude: Double, context: Context): String {
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(context, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (e: IndexOutOfBoundsException) {
        }
        return address!!
    }

    private
    var dialogBand: android.app.AlertDialog? = null

    private fun trackLocation() {
        try {
            obLocation.observe(this, androidx.lifecycle.Observer {

                obLocation.value?.let {
                    if (it.getSucces()) {
                        obLocation.value?.getData()?.state?.let {
                            if (it.equals(Constants.TRIP_BE_GOING)) {
                                if (arrivalStatus) {
                                    initViewDone()
                                } else {
                                    initViewGoing()
                                    mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                                }
                                obLocation.value?.getData()?.result?.let {
                                    getDataBinding().tvStatus.text = it
                                }
                            } else {
                                arrivalStatus = false
                                initViewDone()
                                obLocation.value?.getData()?.result?.let {
                                    getDataBinding().tvStatus.text = it
                                }
                            }
                        }
                        obLocation.value?.getData()?.let {
                            idTrip = it.id_trip
                        }
                        try {
                            obLocation.value?.let {
                                Handler().postDelayed({
                                    it.getData()?.limit_speed?.let {
                                        if (context == null) {
                                            return@let
                                        }
                                        if (it > 0.0 && it < SharedPfPermissionUtils.getSpeed(context!!)!!) {
                                            getCurrentFragment(getBaseActivity().supportFragmentManager)!!.tag!!.let {
                                                if (!it.equals(TripFragment::class.java.name)) {
                                                    try {
                                                        (activity as MainActivity).openSplashActivity()
                                                    } catch (e: ClassCastException) {
                                                    }
                                                }
                                            }
                                            if (getDataBinding().btnDashboard.visibility == View.VISIBLE) {
                                                getDataBinding().btnDashboard.visibility = View.INVISIBLE
                                            }
                                            obLocation.value?.getData()?.state?.let {
                                                if (it.equals(Constants.TRIP_BE_GOING)) {
                                                    if (trips.size > 0) {
                                                        trips.get(0).isBlock = true
                                                        getDataBinding().rcView.adapter?.notifyDataSetChanged()
                                                    }
                                                    getDataBinding().rl1.visibility = View.VISIBLE
                                                    getDataBinding().flMap.visibility = View.VISIBLE
                                                    getDataBinding().rl2.visibility = View.GONE
                                                    getDataBinding().rl3.visibility = View.GONE
                                                }
                                            }
                                            getDataBinding().tvWarning.visibility = View.VISIBLE
                                        } else {
                                            if (getDataBinding().btnDashboard.visibility == View.INVISIBLE) {
                                                if (trips.size > 0) {
                                                    trips.get(0).isBlock = false
                                                    getDataBinding().rcView.adapter?.notifyDataSetChanged()
                                                }

                                                getDataBinding().btnDashboard.visibility = View.VISIBLE
                                                getDataBinding().tvWarning.visibility = View.GONE
                                            }
                                        }
                                    }
                                }, 500)
                            }
                        } catch (e: java.lang.Exception) {
                        }
                    } else {
                        try {
                            (activity as MainActivity).openSplashActivity()
                        } catch (e: ClassCastException) {
                        }
                    }
                }
            })
        } catch (e: KotlinNullPointerException) {
        }
    }

    private fun initViewGoing() {
        if (getDataBinding().rl1.visibility == View.GONE) {
            getDataBinding().rl3.visibility = View.VISIBLE
        }
        getDataBinding().rl2.visibility = View.GONE
        getDataBinding().btnDone.visibility = View.GONE
    }

    private fun initViewDone() {
        getDataBinding().rl2.visibility = View.VISIBLE
        if (isManualApi) {
            if (listUrlSchedule != null && listUrlSchedule!!.size > 0) {
                getDataBinding().btnCreate.visibility = View.GONE
            } else {
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnCreate.visibility = View.VISIBLE
            }
        }
        getDataBinding().rl3.visibility = View.GONE
        getDataBinding().rl1.visibility = View.GONE
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isHide = hidden
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_dashboard -> {
                OpenFragmentUtils.openDashboardAnimationFragment(getBaseActivity().supportFragmentManager, TripFragment::class.java)
            }
            R.id.btn_map -> {
                getDataBinding().rl1.visibility = View.VISIBLE
                getDataBinding().rl3.visibility = View.GONE
                getDataBinding().flMap.visibility = View.VISIBLE
            }
            R.id.btn_create -> {
                if (ContextCompat.checkSelfPermission(getBaseActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getBaseActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    SharedPfPermissionUtils.saveIncomplete(context!!, false)
                    MVVMApplication.isFocusStart = true
                    MVVMApplication.listText.clear()
                    MVVMApplication.listTextCheck.clear()
                    (activity as MainActivity).openCapDocumentActivity()
                } else {
                    initPermissionsExternal()
                }
            }
            R.id.btn_done -> {
                if (isManualApi) {
                    showDatePickerCompleteTrip()
                } else {
                    listUrlSchedule?.let {
                        idTrip?.let { it1 ->
                            mModel.upDocument(SharedPfPermissionUtils.getNameDocument(context!!).toString(), it1, it, SharedPfPermissionUtils.getToken(context!!).toString())
                        }
                    }
                }
            }
            R.id.btn_document -> {
                OpenFragmentUtils.openDocumentViewFragment(getBaseActivity().supportFragmentManager, TripFragment::class.java, listUrlSchedule, true, 1, true)
            }

            R.id.btnHere -> {
                showDialog()
            }

//            R.id.btn_complete_trip ->{
//                showDatePickerCompleteTrip()
//            }

        }
    }


    val dialogBottomArrival = SingleDateAndTimePickerDialog.Builder(context)
    val dialogBottomDeparture = SingleDateAndTimePickerDialog.Builder(context)

    private fun showDatePickerCompleteTrip() {
        var arrivalTime: Date? = null
        if (!StringUtils.isEmpty(realtimeArrival)) {
            try {
                arrivalTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(realtimeArrival!!)
            } catch (e: ParseException) {

            }
        }
        dialogBottomDeparture.context(context)
                .bottomSheet()
                .curved()
                .minutesStep(1)
                .defaultDate(Date())
                .titleTextColor(resources.getColor(R.color.red))
                .mainColor(resources.getColor(R.color.black_picker))
                .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                    override fun onDisplayed(picker: SingleDateAndTimePicker?) {
                    }
                })
                .isDeparture(true)
                .titleSheet("DEPARTURE TIME")
                .contentError("Departure Time must be greater than Arrival Time")
                .setArrivalTime(arrivalTime)
                .listener(object : SingleDateAndTimePickerDialog.Listener {
                    override fun onDateSelected(date: Date?) {
                        callApi(date, false)
                    }
                }).display()

    }

    private fun showDatePicker() {
        dialogBottomArrival.context(context)
                .bottomSheet()
                .curved()
                .minutesStep(1)
                .defaultDate(Date())
                .titleTextColor(resources.getColor(R.color.red))
                .mainColor(resources.getColor(R.color.black_picker))
                .displayListener(object : SingleDateAndTimePickerDialog.DisplayListener {
                    override fun onDisplayed(picker: SingleDateAndTimePicker?) {
                    }
                })
                .isDeparture(false)
                .titleSheet("ARRIVAL TIME")
                .contentError("Future date/time is not allowed")
                .listener(object : SingleDateAndTimePickerDialog.Listener {
                    override fun onDateSelected(date: Date?) {
                        callApi(date, true)
                    }
                }).display()

    }

    private fun callApi(date: Date?, isArrivalTime: Boolean) {
        if (idTrip == null) {
            showMessage("Id trip = null")
        }

        if (curentLocation == null && MVVMApplication.location.value == null) {
            showMessage("Location = null")
        }

        idTrip?.let { it ->
            val dateRes = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date!!)
            if (curentLocation != null) {
                if (isArrivalTime) {
                    mModel.updateManual(it, "", dateRes, curentLocation?.longitude.toString(), curentLocation?.latitude.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
                    isManual = true
                } else {
                    mModel.updateManual(it, dateRes, "", curentLocation?.longitude.toString(), curentLocation?.latitude.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
                    isManual = false
                }
            } else if (MVVMApplication.location.value != null) {
                MVVMApplication.location.value?.let { it1 ->
                    if (isArrivalTime) {
                        mModel.updateManual(it, "", dateRes, it1.longitude.toString(), it1.latitude.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
                        isManual = true
                    } else {
                        mModel.updateManual(it, dateRes, "", it1.longitude.toString(), it1.latitude.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
                        isManual = false
                    }
                }
            }
        }
    }

    private fun showDialog() {
        val binding = DialogConfirmManualModeBinding.inflate(LayoutInflater.from(context!!))
        dialog = android.app.AlertDialog.Builder(context!!).create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setView(binding.root)
        binding.btnNo.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.btnYes.setOnClickListener {
            dialog!!.dismiss()
            showDatePicker()
        }

        val window: Window = dialog!!.window!!
        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        window.attributes = wlp
        dialog!!.setCancelable(false)
        if (null != dialog && !dialog!!.isShowing) {
            dialog!!.show()
        }
    }

    override fun onBackRoot() {
        if (SharedPfPermissionUtils.getHasTrips(context!!)) {
            return
        }
        super.onBackRoot()
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentTripBinding
}
