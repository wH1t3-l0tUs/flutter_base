package io.driverdoc.testapp.ui.main.incompltripdetail

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.SystemClock
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.isBack
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.databinding.FragmentIncompleTripDetailBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class IncompTripDetailFragment : BaseMvvmFragment<IncomeTripDetailCallBack, IncomeTripDetailModel>(), View.OnClickListener {
    private var mLastClickTime: Long = 0
    val EXTERNAL = 21
    private var timeTrip: String? = null
    private var typeTrip: String? = null
    private var document_type: String? = null
    private var number: String? = null
    private var id: Int? = null
    private var depature: String? = null
    private var isChangeData: Boolean? = null
    private var listUrl: ArrayList<String>? = null
    private var isBack = false

    override fun createModel(): IncomeTripDetailModel {
        val model = IncomeTripDetailModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(IncomeTripDetailModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_incomple_trip_detail
    override fun setEvents() {
        getDataBinding().btnDocument.setOnClickListener(this)
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().btnCreate.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initComponents() {
        isChangeData = false
        val bundle = arguments
        bundle?.let {
            timeTrip = it.getString(Constants.TIME_TRIP)
            number = it.getString(Constants.NUMBER_TRIP)
            id = it.getInt(Constants.ID_TRIP)
            depature = it.getString(Constants.DEPARTURE)
            document_type = it.getString(Constants.TYPE_DOCUMENT)
            listUrl = it.getStringArrayList(Constants.LIST_URL)
            if (!it.getBoolean(Constants.DE_DOCUMENT)) {
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_red)
            } else {
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_blue)
            }
            if (!it.getBoolean(Constants.DE_STATUS)) {
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_red)
            } else {
                getDataBinding().img1.setImageResource(R.drawable.ic_oval_blue)
            }
            if (!it.getBoolean(Constants.DE_DEP)) {
                getDataBinding().img5.setImageResource(R.drawable.ic_oval_red)
            } else {
                getDataBinding().img5.setImageResource(R.drawable.ic_oval_blue)
            }
            getDataBinding().tvName.setText("Trip " + number.toString())
            typeTrip = it.getString(Constants.TYPE)
            getDataBinding().tvIncom.setText(typeTrip)
            depature?.let {
                if (!StringUtils.isEmpty(depature)) {
                    getDataBinding().tvDepar.setText(StringUtils.fomatDateNormal(it) + " - " + StringUtils.fomatTimeNormal(it))

                }
            }
            document_type?.let {
                if (!StringUtils.isEmpty(document_type)) {
                    getDataBinding().tvBill.setText(document_type)
                }
            }

            timeTrip?.let {
                getDataBinding().tvAddr.setText(StringUtils.fomatDateNormal(it) + " - " + StringUtils.fomatTimeNormal(it))
            }
            if (listUrl!!.size < 2) {
                getDataBinding().tvCount.setText(listUrl!!.size.toString() + " page")
            } else {
                getDataBinding().tvCount.setText(listUrl!!.size.toString() + " page(s)")
            }
        }

        if (null != typeTrip && null != listUrl) {
            if (typeTrip.equals("Incomplete") && listUrl!!.size == 0) {
                getDataBinding().lnCapture.visibility = View.VISIBLE
                getDataBinding().rl2.visibility = View.GONE
            } else {
                getDataBinding().lnCapture.visibility = View.GONE
                getDataBinding().rl2.visibility = View.VISIBLE
            }
        }

        liveData.observe(this, androidx.lifecycle.Observer {
            if (it == 2) {
                isChangeData = true
                getDataBinding().lnCapture.visibility = View.GONE
                getDataBinding().rl2.visibility = View.VISIBLE
                val dir = File(context!!.getExternalCacheDir(), SharedPfPermissionUtils.getIDTrip(context!!).toString())
                if (dir.isDirectory()) {
                    val children = dir.list()
                    listUrl = ArrayList<String>()
                    for (element in children) {
                        listUrl!!.add(Uri.parse(dir.getAbsolutePath() + "/" + element).toString())
                    }
                    getDataBinding().tvBill.setText(SharedPfPermissionUtils.getNameDocument(context!!))
                    if (children.size < 2) {
                        getDataBinding().tvCount.setText(children.size.toString() + " page")
                    } else {
                        getDataBinding().tvCount.setText(children.size.toString() + " page(s)")
                    }
                }
                getDataBinding().img2.setImageResource(R.drawable.ic_oval_blue)
                liveData.postValue(-1)
                LoadDataBinding.clearApplicationData(context!!).start()

            } else if (it == 6) {
                getDataBinding().lnCapture.visibility = View.VISIBLE
                getDataBinding().rl2.visibility = View.GONE
            }
        })

    }


    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_document -> {

                SharedPfPermissionUtils.saveIncomplete(context!!, false)
                OpenFragmentUtils.openDocumentViewFragment(getBaseActivity().supportFragmentManager, IncompTripDetailFragment::class.java, listUrl, false, id!!, false)
            }
            R.id.btn_close -> {
                onBackRoot()
            }
            R.id.btn_create -> {
                isBack = true
                if (ContextCompat.checkSelfPermission(getBaseActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getBaseActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    SharedPfPermissionUtils.saveIncomplete(context!!, true)
                    id?.let {
                        MVVMApplication.listText.clear()
                        MVVMApplication.listTextCheck.clear()
                        SharedPfPermissionUtils.saveIDTrip(context!!, it)
                        (activity as MainActivity).openCapDocumentActivity()
                        MVVMApplication.isFocusStart = true
                    }
                } else {
                    initPermissionsExternal()
                }
            }
        }
    }

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
                        id?.let {
                            SharedPfPermissionUtils.saveIDTrip(context!!, it)
                            SharedPfPermissionUtils.saveIncomplete(context!!, true)
                            (activity as MainActivity).openCapDocumentActivity()
                        }
                    }
                }
            }
        }
    }

    override fun onBackRoot() {
        if (isChangeData!!) {
            isChangeData = false
            liveData.postValue(3)
            isBack = true
            super.onBackRoot()
            super.onBackRoot()
        }
        else if (isBack){
            isBack = false
            super.onBackRoot()
            super.onBackRoot()
        }
        else {
            super.onBackRoot()
        }
    }

    fun getDataBinding() = mBinding as FragmentIncompleTripDetailBinding
}