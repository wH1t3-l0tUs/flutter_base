package io.driverdoc.testapp.ui.base.activity

import android.R
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jaeger.library.StatusBarUtil
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.databinding.FragmentUpdateVersionBinding
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.LoadDataBinding.openCHPlay
import io.driverdoc.testapp.ui.utils.permision.PermissionGrantUtils


abstract class BaseActivity : AppCompatActivity(), ViewActivity, ActivityCompat.OnRequestPermissionsResultCallback {


    protected var mIsClearMemoryActivity: Boolean = false
    protected var mIsDestroyView = true
    private var mViewRoot: View? = null
    protected var mIsStarted: Boolean = false
    protected lateinit var mBinding: ViewDataBinding
    private var x1 = 0f
    private var x2 = 0f
    var MIN_DISTANCE = 100
    private var dialogVersion: Dialog? = null
    protected var actionWhenResume: (() -> Unit)? = null
    private var isVisibleView = false
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mIsDestroyView = false
        beforLoadUI()
        StatusBarUtil.setTransparent(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
        onCreateControl(savedInstanceState)


    }

    override fun onCreateControl(savedInstanceState: Bundle?) {
        if (!mIsClearMemoryActivity) {
            mBinding = DataBindingUtil.setContentView(this, getLayoutMain())
            setEvents()
            initComponents()
        }
    }

    private var isFocus = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        liveData.observe(this, Observer {
            if (it == -1) {
                if (isFocus) {
                    super.dispatchTouchEvent(ev)
                }
                val v = getCurrentFocus()
                if (v != null &&
                        (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                        v is EditText) {
                    val scrcoords = IntArray(2)
                    v.getLocationOnScreen(scrcoords);
                    val x = ev.getRawX() + v.x - scrcoords[0]
                    val y = ev.getRawY() + v.getTop() - scrcoords[1]
                    if (y < v.getTop() || y > v.getBottom()) {
                        isFocus = true
                        Handler().postDelayed({
                            if (mIsDestroyView) {
                                return@postDelayed
                            }
                            isFocus = false
                            val v2 = getCurrentFocus()
                            if (v2 is EditText) {
                                if (v2 == v) {
                                    hideKeyBoard()
                                    if (mBinding != null) {
                                        mBinding.root.isFocusable = true
                                        mBinding.root.isFocusableInTouchMode = true
                                        mBinding.root.requestFocus()
                                    }

                                }
                            }

                        }, 100)
                    }
                }

            }
        })



        BaseFragment.getCurrentFragment(supportFragmentManager)?.let {

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = ev.getX()
                }
                MotionEvent.ACTION_UP -> {
                    x2 = ev.getX()
                    val deltaX = x2 - x1
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (x1 < 50) {

                            it.onBackRoot()
                        }

                    } else {
                        // consider as something else - a screen tap for example
                    }


                }

            }
        }
        return super.dispatchTouchEvent(ev)
    }

    protected fun beforLoadUI() {
        mIsClearMemoryActivity = false
    }

    override fun findFragmentByTag(tag: String): BaseFragment {
        return supportFragmentManager.findFragmentByTag(tag) as BaseFragment
    }

    override fun setViewRoot(viewRoot: View) {
        mViewRoot = viewRoot
    }


    override fun showMessage(message: String) {
        if (!mIsDestroyView) {
            if (mViewRoot == null) {
                val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return
            }
            val snackbar = Snackbar.make(mViewRoot!!, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    override fun showMessage(messageId: Int) {
        if (!mIsDestroyView) {
            if (mViewRoot == null) {
                val toast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                return
            }
            val snackbar = Snackbar.make(mViewRoot!!, messageId, Snackbar.LENGTH_LONG)
            snackbar.show()
        }

    }

    final override fun onBackParent() {
        super.onBackPressed()
    }

    override fun onBackRoot() {
        onBackParent()
    }

    override fun onBackPressed() {
        val baseFragment = BaseFragment.getCurrentFragment(supportFragmentManager)
        if (null == baseFragment) {
            onBackParent()
        } else {
            baseFragment.onBackRoot()
        }
    }

    final override fun onResume() {
        super.onResume()
        hideKeyBoard()
        if (!mIsClearMemoryActivity) {
            onResumeControl()
            isVisibleView = true
            actionWhenResume?.invoke()
            actionWhenResume = null
        }


    }


    override fun onResumeControl() {

    }

    final override fun onPause() {
        isVisibleView = false
        if (!mIsClearMemoryActivity) {
            onPauseControl()
        }
        super.onPause()

    }

    fun isVisibleView(): Boolean {
        return isVisibleView
    }

    override fun onPauseControl() {

    }

    override fun hideKeyBoard(): Boolean {
        val view = currentFocus ?: return false
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override final fun onStart() {
        super.onStart()
        mIsStarted = true
        onStartControl()
    }

    override fun onStartControl() {

    }

    override final fun onStop() {
        mIsStarted = false
        onStopControl()
        super.onStop()
    }

    override fun onStopControl() {

    }

    final override fun onDestroy() {
        mIsDestroyView = true
        onDestroyControl()
        super.onDestroy()
    }

    override fun onDestroyControl() {
    }

    override val isDestroyView: Boolean
        get() = mIsDestroyView

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        PermissionGrantUtils.checkPermissionFinish(this, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun appDatabase() = (applicationContext as MVVMApplication).appDatabase()
    fun interactCommon() = (applicationContext as MVVMApplication).interactCommon()
    fun schedule() = (applicationContext as MVVMApplication).schedule()
    fun gson() = (applicationContext as MVVMApplication).gson()


    fun showDialogUpdate() {
        dialogVersion?.let {
            if (it.isShowing) {
                return
            }
        }
        dialogVersion = Dialog(this, R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val binding = FragmentUpdateVersionBinding.inflate(LayoutInflater.from(this))

        dialogVersion!!.setContentView(binding.getRoot())

        binding.root.setBackgroundColor(resources.getColor(io.driverdoc.testapp.R.color.white))

        dialogVersion!!.setContentView(binding.getRoot())
        binding.btnUpdate.setOnClickListener {
            openCHPlay(this)
            dialogVersion!!.dismiss()
        }
        dialogVersion!!.setCancelable(false)
        dialogVersion!!.show()
    }

    fun marginBottom() {
        if (hasNavBar(resources)) {
            // no navigation bar, unless it is enabled in the settings
            try {
                val frameLayout = findViewById(io.driverdoc.testapp.R.id.content) as FrameLayout
                LoadDataBinding.getHeightNavBar(this)?.let { frameLayout.setPadding(0, 0, 0, it) };
            } catch (e: Exception) {

            }
        } else {
            // 99% sure there's a navigation bar
        }

    }

    open fun hasNavBar(resources: Resources): Boolean {
        val id: Int = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && resources.getBoolean(id)
    }

}
