package io.driverdoc.testapp.ui.splash

import android.content.Intent
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.ui.base.activity.BaseActivity
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.start.StartActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class SplashActivity : BaseActivity() {
    override fun getLayoutMain() = R.layout.activity_splash

    override fun setEvents() {
    }

    override fun initComponents() {
        MainActivity.curentIdTrip = null
        MVVMApplication.isNotrip = false
        SharedPfPermissionUtils.saveTrip(this, false)
        OpenFragmentUtils.openSplashFragment(supportFragmentManager)
//        marginBottom()
        if (getIntent().getExtras() != null && getIntent().getExtras()?.containsKey("data")!!) {
            val msg = getIntent().getExtras()?.getString("data")
            if (msg.equals("NEW_SCHEDULE_TRIP")) {
                msg?.let { SharedPfPermissionUtils.saveTypeNoti(this, it) }
            }
            if (msg.equals("NEW_ACTIVE_TRIP")) {
                msg?.let { SharedPfPermissionUtils.saveTypeNoti(this, it) }
            }
        }
        onNewIntent(intent)
        run(Constants.URL_GET_IP)

    }

    override fun onNewIntent(intent: Intent?) {
        val extras = intent!!.getExtras()
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                // extract the extra-data in the Notification
                val msg = extras.getString("NotificationMessage")
                if (msg.equals("NEW_SCHEDULE_TRIP")) {
                    msg?.let { SharedPfPermissionUtils.saveTypeNoti(this, it) }
                }
                if (msg.equals("NEW_ACTIVE_TRIP")) {
                    msg?.let { SharedPfPermissionUtils.saveTypeNoti(this, it) }
                }
            }
        }
        super.onNewIntent(intent)
    }

    fun openStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }

    fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }

    fun run(url: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
                .url(url)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {}
            override fun onResponse(call: okhttp3.Call, response: Response){
                response.body()?.string()?.let { SharedPfPermissionUtils.saveIpPublic(applicationContext, it) }
            }
        })
    }

}