package io.driverdoc.testapp.ui.start.permision

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.view.View
import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.base.fragment.BaseFragment
import io.driverdoc.testapp.ui.start.StartActivity


class PermissionFragment : BaseFragment(), View.OnClickListener {
    private val LOCATION = 100
    private val CAMERA = 101
    private var requestAll = false
    private var requestCamera = false
    private var requestLocation = false
    private var isResume = false

    override fun getLayoutMain() = R.layout.fragment_permission

    override fun setEvents() {
        getDataBinding().btnTurn.setOnClickListener(this)
        getDataBinding().btnClose.setOnClickListener(this)
    }

    override fun initComponents() {
        getDataBinding().btnClose.visibility = View.GONE

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_turn -> {
                if (requestAll) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        initPermissionsLocationAndroidP()
                    } else {
                        initPermissionsLocation()
                    }
                } else if (requestLocation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        initPermissionsLocationAndroidP()

                    } else {
                        initPermissionsLocation()
                    }
                } else if (requestCamera) {
                    initPermissionsCamera()
                } else {
                    (activity as PermisionActivity).openSplash()
                }
            }

        }
    }


    private fun initPermissionsLocationAndroidP() {
        val hasForegroundLocationPermission = ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasForegroundLocationPermission) {
            val hasBackgroundLocationPermission = ActivityCompat.checkSelfPermission(context!!,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            if (hasBackgroundLocationPermission) {
            } else {
                ActivityCompat.requestPermissions(getBaseActivity(),
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), LOCATION)
            }
        } else {
            ActivityCompat.requestPermissions(getBaseActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION), LOCATION)
        }
    }

    private fun initPermissionsLocation() {

        val checkPermisonLocation = ActivityCompat.checkSelfPermission(getBaseActivity().applicationContext,
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
                    if (!Settings.System.canWrite(context)) {
                        requestPermissions(Array(1, { Manifest.permission.ACCESS_FINE_LOCATION }), LOCATION)
                    }
                }

            }
        }
    }

    private fun initPermissionsCamera() {

        val checkPermisonLocation = ActivityCompat.checkSelfPermission(getBaseActivity().applicationContext,
                Manifest.permission.CAMERA)
        if (checkPermisonLocation == PackageManager.PERMISSION_DENIED) {
            var shouldShow = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                shouldShow = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            }
            if (shouldShow) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(Array(1, { Manifest.permission.CAMERA }), CAMERA)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(context)) {
                        requestPermissions(Array(1, { Manifest.permission.CAMERA }), CAMERA)
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.ACCESS_BACKGROUND_LOCATION) || permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                         isResume = true
                    }

                }
            }

        }
        if (requestCode == CAMERA) {
            for (i in 0..permissions.size - 1) {
                val permission = permissions[i]
                val grantResult = grantResults[i]
                if (permission.equals(Manifest.permission.CAMERA)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        getDataBinding().btnTurn.setText("Done")
                        isResume = true

                    }

                }
            }


        }

    }




    override fun onStop() {
        super.onStop()
        isResume = false
    }

    override fun onResumeControl() {
        super.onResumeControl()
        checkPermistion()
        if (!isResume) {
            if (requestAll) {
                getDataBinding().img1.visibility = View.VISIBLE
                getDataBinding().img2.visibility = View.VISIBLE
                getDataBinding().img3.visibility = View.VISIBLE
            } else if (requestCamera) {
                getDataBinding().img1.visibility = View.GONE
                getDataBinding().img2.visibility = View.VISIBLE
                getDataBinding().img3.visibility = View.VISIBLE
            } else if (requestLocation) {
                getDataBinding().img1.visibility = View.VISIBLE
                getDataBinding().img2.visibility = View.GONE
                getDataBinding().img3.visibility = View.VISIBLE
            } else {
                getDataBinding().img1.visibility = View.GONE
                getDataBinding().img2.visibility = View.GONE
                getDataBinding().img3.visibility = View.VISIBLE
            }
        }
    }

    private fun checkPermistion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                requestAll = true
                requestLocation = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.VISIBLE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")

            } else if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_DENIED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                requestLocation = true
                requestAll = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.VISIBLE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")

            } else if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestLocation = false
                requestAll = false
                requestCamera = true
                getDataBinding().lnLocation.visibility = View.GONE
                getDataBinding().lnCamera.visibility = View.VISIBLE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")

            } else {
                requestLocation = false
                requestAll = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.GONE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.VISIBLE
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Done")

            }
        } else {
            if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            ) {
                requestAll = true
                requestLocation = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.VISIBLE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")
            } else if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                requestLocation = true
                requestAll = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.VISIBLE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")

            } else if (ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(getBaseActivity(),
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                requestLocation = false
                requestAll = false
                requestCamera = true
                getDataBinding().lnLocation.visibility = View.GONE
                getDataBinding().lnCamera.visibility = View.VISIBLE
                getDataBinding().lnDone.visibility = View.GONE
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Turn on")
            } else {
                requestLocation = false
                requestAll = false
                requestCamera = false
                getDataBinding().lnLocation.visibility = View.GONE
                getDataBinding().lnCamera.visibility = View.GONE
                getDataBinding().lnDone.visibility = View.VISIBLE
                getDataBinding().img3.setImageResource(R.drawable.ic_oval_blue)
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_gray)
                getDataBinding().btnTurn.setText("Done")
            }
        }
    }

    override fun onBackRoot() {
        return
    }


    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentPermissionBinding
}