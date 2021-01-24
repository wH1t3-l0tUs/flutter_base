package io.driverdoc.testapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.isSplash
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.databinding.DialogAuthBinding
import io.driverdoc.testapp.ui.base.activity.BaseActivity
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.detectdocument.DocumentActivity
import io.driverdoc.testapp.ui.main.dashboard.DashboardFragment
import io.driverdoc.testapp.ui.main.tripsactive.TripFragment
import io.driverdoc.testapp.ui.splash.SplashActivity
import io.driverdoc.testapp.ui.start.StartActivity
import io.driverdoc.testapp.ui.utils.LoadDataBinding.clearApplicationData
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), LocationListener {
    private var EXTERNAL = 1
    private val LOCATION = 100
    private var dialogBand: android.app.AlertDialog? = null
    private var loadingGPS: Dialog? = null
    private var loading: Dialog? = null
    private var disDone: Disposable? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        var isLogout = MutableLiveData<Boolean>()
        var curentIdTrip: String? = null
    }

    override fun getLayoutMain() = R.layout.activity_main

    override fun setEvents() {
    }

    override fun onCreateControl(savedInstanceState: Bundle?) {
        super.onCreateControl(savedInstanceState)
        marginBottom()
        showDialogGPS()
        showDialog()

        MVVMApplication.liveDataNotifi.observe(this, androidx.lifecycle.Observer {
            if (it.data.equals(Constants.UPDATE_TRIP) || it.data.equals(Constants.REMOVED_TRIP)) {
                if (!StringUtils.isEmpty(it.tripNumber)) {
                    BaseFragment.showDialogNewTrip(this, it.data!!, it.tripNumber!!, this)
                }
            }

        })
    }


    private fun checkPermistionLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient!!.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            location?.let {
                                if (it.longitude != 0.0 && it.latitude != 0.0) {
                                    MVVMApplication.location.value = location
                                }
                            }
                        }

//                if (BaseFragment.getCurrentFragment(supportFragmentManager) == null) {
                    OpenFragmentUtils.openHomeRootFragment(supportFragmentManager, SharedPfPermissionUtils.getHasTrips(this))
//                }
            } else {
                loading?.let {
                    if (it.isShowing) {
                        it.dismiss()
                        openSplashActivity()
                    } else {
                        it.show()
                    }
                }
                return
            }
        } else {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient!!.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            location?.let {
                                if (it.longitude != 0.0 && it.latitude != 0.0) {
                                    MVVMApplication.location.value = location
                                }
                            }
                        }

                if (BaseFragment.getCurrentFragment(supportFragmentManager) == null) {
                    OpenFragmentUtils.openHomeRootFragment(supportFragmentManager, SharedPfPermissionUtils.getHasTrips(this))
                }
            } else {
                loading?.let {
                    if (it.isShowing) {
                        it.dismiss()
                        openSplashActivity()
                    } else {
                        it.show()
                    }
                }
                return
            }
        }

    }

    @SuppressLint("InlinedApi")
    private fun initPermissionsLocationAndroidP() {
        val hasForegroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasForegroundLocationPermission) {
            val hasBackgroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (hasBackgroundLocationPermission) {
                // handle location update
            } else {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), LOCATION)
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION), LOCATION)
        }
    }

    private fun initPermissionsLocation() {
        val checkPermisonLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkPermisonLocation == PackageManager.PERMISSION_DENIED) {
            //hien thi dialog yeu cau nguoi dung dong y permission nay
            var shouldShow = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shouldShow = shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (shouldShow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(Array(1, { Manifest.permission.ACCESS_FINE_LOCATION }), LOCATION)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(this)) {
                        requestPermissions(Array(1, { Manifest.permission.ACCESS_FINE_LOCATION }), LOCATION)
                    }
                }

            }
        }
    }


    fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.getClassName()) {
                return true
            }
        }
        return false
    }

    @SuppressLint("CheckResult")
    override fun initComponents() {
        isSplash = false
        isLogout.observe(this, Observer {
            if (it!!) {
                checkPermistionExternal()
                SharedPfPermissionUtils.clearData(this)
                isLogout.postValue(false)
                openStartActivity()
            }
        })
        liveData.observe(this, Observer {
            if (it == 5) {
                if (SharedPfPermissionUtils.getLogin(this)) {
                    SharedPfPermissionUtils.saveJwtToken("")
                    showDialogAuth()
                }
                liveData.postValue(-1)
            }
            if (it == 8) {
                if (null != curentIdTrip) {
                    openSplashActivity()
                    curentIdTrip = null
                    liveData.postValue(-1)
                }
            }
            if (it == Constants.UPDATE_VERSION_REQUIRED) {
                showDialogUpdate()
                liveData.postValue(-1)
            }
        })
        try {
            MVVMApplication.obLocation.observe(this, androidx.lifecycle.Observer {
                MVVMApplication.obLocation.value?.let {
                    if (it.getSucces()) {
                        it.getData()?.id_trip?.let {
                            if (null == curentIdTrip) {
                                curentIdTrip = it
                            }

                            if (!MVVMApplication.isManual && null != curentIdTrip && !curentIdTrip.equals(MVVMApplication.obLocation.value!!.getData()?.id_trip)) {
                                curentIdTrip = MVVMApplication.obLocation.value?.getData()?.id_trip
                                if (SharedPfPermissionUtils.getHasTrips(this)) {
//                                    openSplashActivity()
                                    checkPermistionLocation()
                                }else {
                                    OpenFragmentUtils.openHomeRootFragment(supportFragmentManager, SharedPfPermissionUtils.getHasTrips(this))
                                }
                            }
                        }
                    }
                }

            })
        } catch (e: KotlinNullPointerException) {

        }
        checkPermistionLocation()

        Observable.interval(0, 12, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val date = Date()
                    if (!SharedPfPermissionUtils.getCallLocation(this) || (SharedPfPermissionUtils.getTimeCallLocation(this) != 0L && (date.seconds - Date(SharedPfPermissionUtils.getTimeCallLocation(this)).seconds > 15))) {

                        if (null != disDone) {
                            disDone!!.dispose()
                        }
                        MVVMApplication.location.value?.let {
                            if(!StringUtils.isEmpty(SharedPfPermissionUtils.getToken(this))) {
                                disDone = location(it.longitude, it.latitude, SharedPfPermissionUtils.getToken(this).toString())
                            }
                        }
                        Log.d("-----Call location in ", "Main")
                    }
                }


    }

    private fun showDialogAuth() {
        if (null != dialogBand && dialogBand!!.isShowing) {
            return
        }
        val binding = DialogAuthBinding.inflate(LayoutInflater.from(this))
        dialogBand = android.app.AlertDialog.Builder(this).create()
        dialogBand!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBand!!.setView(binding.getRoot())
//        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getMessageVerifyTokenFail(this))){
//            binding.tvMessage.text = SharedPfPermissionUtils.getMessageVerifyTokenFail(this)
//        }
        binding.btnOk.setOnClickListener {
            dialogBand!!.dismiss()
            SharedPfPermissionUtils.saveLogin(this, false)
            openStartActivity()
        }

        val window: Window = dialogBand!!.window!!
        val wlp = window.getAttributes()

        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialogBand!!.setCancelable(false)
        if (null != dialogBand && !dialogBand!!.isShowing) {
            dialogBand!!.show()
        }
    }

    private fun checkGPS() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGpsEnabled || isNetworkEnabled) {
            // Handle Location turned ON
            val han = Handler()
            han.postDelayed({
                loadingGPS?.dismiss()
            }, 500)
        } else {
            loadingGPS?.let {
                if (!it.isShowing) {
                    it.show()
                    return
                }
            }
        }
    }

    override fun onResumeControl() {
        super.onResumeControl()
        loading?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        checkPermistionLocation()
        checkGPS()

        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        this.registerReceiver(gpsSwitchStateReceiver, filter)
//        registerTimezoneListener()
    }

    fun registerTimezoneListener() {
        val timezoneFilter = IntentFilter(Intent.ACTION_TIMEZONE_CHANGED)

        val timezoneReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                intent.action?.let {
                    if (it.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                        liveData.postValue(Constants.EVEN_CHANGE_TIMEZONE)
                    }
                }
            }
        }
        registerReceiver(timezoneReceiver, timezoneFilter)
    }

    private val gpsSwitchStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {

                val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (isGpsEnabled || isNetworkEnabled) {
                    // Handle Location turned ON
                    val han = Handler()
                    han.postDelayed({
                        loadingGPS?.dismiss()
                    }, 500)
                } else {
                    loadingGPS?.let {
                        if (!it.isShowing) {
                            it.show()
                            return
                        }
                    }

                }
            }
        }
    }

    override fun onDestroyControl() {
        super.onDestroyControl()
        loading?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun onPauseControl() {
        super.onPauseControl()
        this.unregisterReceiver(gpsSwitchStateReceiver);
    }

    private fun showDialogGPS() {
        loadingGPS = Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val binding = io.driverdoc.testapp.databinding.FragmentServiceLocationBinding.inflate(LayoutInflater.from(this))

        loadingGPS!!.setContentView(binding.getRoot())

        binding.root.setBackgroundColor(resources.getColor(R.color.white))

        loadingGPS!!.setContentView(binding.getRoot())
        binding.btnTurn.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        loadingGPS!!.setCancelable(false)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    fun openDashboardFragment(){
        finish()
        startActivity(intent)
    }

    fun openStartActivity() {
        val intent = Intent(this@MainActivity, StartActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }

    fun openSplashActivity() {

        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null && fragment.tag?.equals(DashboardFragment::class.java.name)!!) {
                supportFragmentManager.fragments.remove(fragment)
                supportFragmentManager.beginTransaction().commit()
            }
        }
        val intent = Intent(this@MainActivity, SplashActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }

    fun openCapDocumentActivity() {
        Thread(clearApplicationData(this)).start()
        val intent = Intent(this@MainActivity, DocumentActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }

    private fun checkPermistionExternal() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openCapDocumentActivity()
        } else {
            initPermissionsExternal()
        }
    }

    private fun initPermissionsExternal() {

        val checkPermisonLocation = ActivityCompat.checkSelfPermission(this.applicationContext,
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
                    if (!Settings.System.canWrite(this)) {
                        requestPermissions(Array(1, { Manifest.permission.WRITE_EXTERNAL_STORAGE }), EXTERNAL)
                    }
                }

            }
        }
    }

    private fun showDialog() {
        val binding = io.driverdoc.testapp.databinding.FragmentServiceLocationBinding.inflate(LayoutInflater.from(this))
        loading = Dialog(this, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)

        loading!!.setContentView(binding.getRoot())

        binding.root.setBackgroundColor(resources.getColor(R.color.white))

        loading!!.setContentView(binding.getRoot())
        binding.btnTurn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                initPermissionsLocationAndroidP()
            } else {
                initPermissionsLocation()
            }
        }
        loading!!.setCancelable(false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        openCapDocumentActivity()
                    }

                }
            }
        }
        if (requestCode == LOCATION) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        openSplashActivity()
                    }
                }
            }
        }
        if (requestCode == LOCATION) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        openSplashActivity()
                    }
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        MVVMApplication.location.value = location
    }

    var restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(RestApi::class.java)

    fun location(longitude: Double, latitude: Double, auth: String): Disposable? {
        val request = io.driverdoc.testapp.data.model.trip.LocationRequest()
        request.latitude = latitude
        request.longitude = longitude
        SharedPfPermissionUtils.getSpeed(this)?.let { it ->
            request.speed = it.toDouble()
        }

        val location = Location(LocationManager.NETWORK_PROVIDER)
        location.longitude = longitude
        location.latitude = latitude
        MVVMApplication.location.value = location
        val body = RequestBody.create(MediaType.parse("application/json"), Gson().toJson(request))
        val headerPare = Pair(Constants.AUTHORIZATION, Constants.BEARER + auth)
        val header = mutableListOf<Pair<String, String>>()
        header.add(headerPare)
        restApi = ApiHelp.createRetrofit(endpoint = io.driverdoc.testapp.common.Constants.BASE_URL, formatDate = io.driverdoc.testapp.common.Constants.LIST_FORMAT_TIME, headers = header)
                .create(RestApi::class.java)
        return subscribeHasResultDispose(
                restApi.postLocation(body)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()))
        {
            MVVMApplication.obLocation.value = it
            MVVMApplication.isManual = true
            SharedPfPermissionUtils.saveTimeCallLocation(this, Date())
        }
    }


    protected inline fun <reified T> subscribeHasResultDispose(observable: Observable<T>?, crossinline onNext: (T) -> Unit): Disposable? {
        if (observable == null) {
            return null
        }

        val className = T::class.java.name
        return observable.subscribe(
                {
                    MVVMApplication.isNotrip = true
                    onNext(it)
                },
                {
                    run {
                        if (Constants.DEBUG) {
                            it.printStackTrace()
                        }

                    }
                    if (it is HttpException) {
                        if ((it as HttpException).code() == 401) {
                            handlerErrorExecption(it, { className })
                            return@subscribe
                        }
                        if ((it as HttpException).code() == 404) {
                            if (SharedPfPermissionUtils.getHasTrips(this)) {
                                liveData.postValue(8)
                            }

                            return@subscribe
                        }

                    }
                })
    }

    protected fun handlerErrorExecption(error: Throwable, className: () -> String) {
        if (error is HttpException) {
            if (error.code() == 500) {

            } else {
                try {
                    val resStrError = JSONObject(error.response().errorBody()?.string()!!)
                    val errorResponse = resStrError.getString("errorCode")
                    if (errorResponse.toString().equals(Constants.VERIFY_APP_VERSION)) {
                        liveData.postValue(Constants.UPDATE_VERSION_REQUIRED)
                    } else {
                        SharedPfPermissionUtils.saveJwtToken("")
                        SharedPfPermissionUtils.saveMessageVerifyTokenFail(resStrError.getString("messages"))
                        liveData.postValue(5)
                    }
                } catch (e: JsonSyntaxException) {

                } catch (e: TypeCastException) {

                }
            }
        }


    }
}
