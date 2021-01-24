package io.driverdoc.testapp.ui.start

import androidx.lifecycle.Observer
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.databinding.DialogAuthBinding
import io.driverdoc.testapp.ui.base.activity.BaseActivity
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.splash.SplashActivity
import io.driverdoc.testapp.ui.start.permision.PermisionActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils

class StartActivity : BaseActivity() {
    override fun getLayoutMain() = R.layout.activity_start
    override fun setEvents() {
    }

    override fun onCreateControl(savedInstanceState: Bundle?) {
        super.onCreateControl(savedInstanceState)
    }

    override fun initComponents() {
        if (null == BaseFragment.getCurrentFragment(supportFragmentManager)) {
            OpenFragmentUtils.openStartedFragment(supportFragmentManager)
        }
        liveData.observe(this, Observer {
            if (it == 5) {
//                if (SharedPfPermissionUtils.getLogin(this)) {
                showDialogAuth()
//                }
                liveData.postValue(-1)
            }
        })
    }

    override fun onResumeControl() {
        super.onResumeControl()
        hideKeyBoard()
    }

    override fun onPauseControl() {
        super.onPauseControl()
    }

    fun openPermistionActivity() {
        val intent = Intent(this, PermisionActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_to_left, R.anim.exit_to_left)

    }

    fun openSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.animate_zoom_enter, R.anim.animate_zoom_exit)

    }

    private var dialogAu: android.app.AlertDialog? = null
    private fun showDialogAuth() {
        if (null != dialogAu && dialogAu!!.isShowing) {
            return
        }
        val binding = DialogAuthBinding.inflate(LayoutInflater.from(this))
        dialogAu = android.app.AlertDialog.Builder(this).create()
        dialogAu!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogAu!!.setView(binding.getRoot())
//        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getMessageVerifyTokenFail(this))){
//            binding.tvMessage.text = SharedPfPermissionUtils.getMessageVerifyTokenFail(this)
//        }
        binding.btnOk.setOnClickListener {
            SharedPfPermissionUtils.saveLogin(this, false)
            dialogAu!!.dismiss()
        }

        val window: Window = dialogAu!!.window!!
        val wlp = window.getAttributes()

        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialogAu!!.setCancelable(false)
        if (null != dialogAu && !dialogAu!!.isShowing) {
            dialogAu!!.show()
        }
    }

}