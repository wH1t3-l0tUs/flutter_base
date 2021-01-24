package io.driverdoc.testapp.ui.main.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.RestApi
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

class LocationUpdatesService : Service() {

    private val mBinder = LocalBinder()
    private var mNotificationManager: NotificationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null

    private var mServiceHandler: Handler? = null
    private var mLocation: Location? = null
    private var disDone: Disposable? = null

    private val notification: Notification
        get() {
            val intent = Intent(this, LocationUpdatesService::class.java)

            intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
            val builder = NotificationCompat.Builder(this)
                    .setContentText(resources.getString(R.string.app_name) + " is running...")
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_app2)
                    .setSound(null)
                    .setWhen(System.currentTimeMillis())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID)
            }
            return builder.build()
        }

    override fun onCreate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult!!.lastLocation)
            }
        }

        createLocationRequest()
        getLastLocation()

        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW)

            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false)
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        return START_NOT_STICKY
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
            SharedPfPermissionUtils.saveCallLocation(this, true)
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
                    try {
                        removeLocationUpdates()
                        stopSelf()
                    } catch (e: Exception) {

                    }
                } catch (e: JsonSyntaxException) {

                } catch (e: TypeCastException) {

                }
            }
        }


    }


    override fun onBind(intent: Intent): IBinder? {
        stopForeground(true)
        return mBinder

    }

    override fun onRebind(intent: Intent) {
        stopForeground(true)
        super.onRebind(intent)

    }

    override fun onUnbind(intent: Intent): Boolean {
        startForeground(NOTIFICATION_ID, notification)
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        mServiceHandler!!.removeCallbacksAndMessages(null)
    }


    fun requestLocationUpdates() {
        startService(Intent(applicationContext, LocationUpdatesService::class.java))
        try {
            mFusedLocationClient!!.requestLocationUpdates(mLocationRequest,
                    mLocationCallback!!, Looper.myLooper())
        } catch (unlikely: SecurityException) {
        }

    }

    fun removeLocationUpdates() {
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback!!)
            stopSelf()
            SharedPfPermissionUtils.saveCallLocation(this, false)
        } catch (unlikely: SecurityException) {
        }
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        } else {
            stopSelf()
        }
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancelAll();
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null) {
                            mLocation = task.result
                        } else {
                            Log.w(TAG, "Failed to get location.")
                        }
                    }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }

    }

    private fun onNewLocation(location: Location) {
        if (null != disDone) {
            disDone!!.dispose()
        }
        location.let {
            if (it.longitude != 0.0 && it.latitude != 0.0) {
                if (SharedPfPermissionUtils.getSpeed(this) != ((location.speed * 3600F) / 1000F)) {
                    SharedPfPermissionUtils.saveSpeed(this, ((location.speed * 3600F) / 1000F))
                    Log.d("LocationUpdate....", ((location.speed * 3600F) / 1000F).toString())
                }
                if(!StringUtils.isEmpty(SharedPfPermissionUtils.getToken(this))) {
                    disDone = location(it.longitude, it.latitude, SharedPfPermissionUtils.getToken(this).toString())
                    Log.d("-----Call location in ", "Service")
                }

            }
        }
        Log.i(TAG, "New location: $location")
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    inner class LocalBinder : Binder() {
        internal val service: LocationUpdatesService
            get() = this@LocationUpdatesService
    }


    companion object {
        private val PACKAGE_NAME = "com.google.android.gms.location.sample.locationupdatesforegroundservice"
        private val TAG = LocationUpdatesService::class.java.simpleName
        private val CHANNEL_ID = "channel_01"
        val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"
        val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 20000
        val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
        private val NOTIFICATION_ID = 12345678
    }

}
