package io.driverdoc.testapp.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import io.driverdoc.testapp.data.model.DataNotifi
import io.driverdoc.testapp.data.model.LocationRespone
import io.driverdoc.testapp.di.compoment.AppComponent
import java.util.*


class MVVMApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {
    lateinit var appComponent: AppComponent
        get

    companion object {

        val isDone = MutableLiveData<Boolean>()
        val isDoneDetext = MutableLiveData<Boolean>()
        val liveData = MutableLiveData<Int>()
        var isFocusStart = true
            get
            set
        val liveDataNotifi = MutableLiveData<DataNotifi>()
        var isSplash = true
        var isBack = false
        var activity: Activity? = null
        var isNotrip = false
        var isManual = false
        var isRetake = MutableLiveData<Boolean>()
        var isTriggerTrip = false
        var mActivitiesStarted: MutableList<Activity> = mutableListOf()
        var listText: MutableList<String> = mutableListOf()
        var listTextCheck: MutableList<Boolean> = mutableListOf()
        var isStarted = MutableLiveData<Boolean>()
        val obLocation = MutableLiveData<LocationRespone>()
        val location = MutableLiveData<Location>()
        private var context: Context? = null
        var timeCallLocation: Date? = null
            get


        @Synchronized
        fun getContext(): Context? {
            return context
        }
    }

    private fun init() {
        mActivitiesStarted = mutableListOf()
        registerActivityLifecycleCallbacks(this)
    }


    fun getCurrentActivity(): Activity? {
        if (mActivitiesStarted.size > 0) {
            activity = mActivitiesStarted[mActivitiesStarted.size - 1]
            return activity
        } else {
            return null
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = io.driverdoc.testapp.di.compoment.DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this)
        init()
        FirebaseApp.initializeApp(this)
        context = applicationContext
        //appcenter
        //TEST
        AppCenter.start(this, "2a8099f3-13a2-4a12-ba4b-4852dc942613",
                Analytics::class.java, Crashes::class.java)

    }

    fun appDatabase() = appComponent.appDatabase()
    fun interactCommon() = appComponent.interactCommon()
    fun schedule() = appComponent.schedule()
    fun gson() = appComponent.gson()

    override fun onActivityPaused(p0: Activity?) {

    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(act: Activity?) {
        if (act != null) {
            mActivitiesStarted.add(act)
        }
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(act: Activity?) {
        if (act != null) {
            mActivitiesStarted.remove(act)
        }
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }
}