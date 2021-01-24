package io.driverdoc.testapp.ui.start.permision

import android.content.Intent
import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.base.activity.BaseActivity
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.splash.SplashActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils

/**
 * Created by khaipc on 06,January,2020
 */
class PermisionActivity : BaseActivity() {
    override fun getLayoutMain() = R.layout.activity_main

    override fun setEvents() {
    }

    override fun initComponents() {
        marginBottom()
        if (null == BaseFragment.getCurrentFragment(supportFragmentManager)) {
            OpenFragmentUtils.openPermistionFragment(supportFragmentManager)
        }
    }

    fun openSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)
    }
}