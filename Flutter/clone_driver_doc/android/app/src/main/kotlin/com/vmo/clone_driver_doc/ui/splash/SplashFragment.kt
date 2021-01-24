package io.driverdoc.testapp.ui.splash

import android.app.Dialog
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.isSplash
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.HomeRootFragment
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference
import android.content.pm.PackageManager
import android.provider.Settings
import android.view.LayoutInflater
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.databinding.FragmentUpdateVersionBinding
import android.net.Uri
import android.os.Build
import android.util.Log
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.LoadDataBinding.openCHPlay


class SplashFragment : BaseMvvmFragment<SplashCallBack, SplashModel>(), SplashCallBack {
    var email = ""
    var cdl_number = ""
    var cdl_state = ""
    var exp_date = ""
    private var phone = ""
    private var last_name = ""
    private var dialogVersion: Dialog? = null

    override fun createModel(): SplashModel {
        val model = SplashModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(SplashModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_splash
    override fun setEvents() {
    }

    override fun onResumeControl() {
        super.onResumeControl()
        getVersionApp()?.let {
            mModel.checkVersion(it, Constants.OS_TYPE)
        }
    }
    override fun initComponents() {
        Log.d(SplashFragment::class.java.name,LoadDataBinding.getVersionOS())
        SharedPfPermissionUtils.saveHasTrip(context!!, false)
        initDialogUpdate()
        mModel.obCheckVersion.observe(this, Observer {
            if (it.getSucces()) {
                if (it.getData()?.appVersionResult.equals(Constants.UPDATE_VERSION)) {
                    Handler().postDelayed({
                        showDialog()
                    }, 3000)
                } else {
                    dismisDialog()
                    initDataNormalVertion()

                }
            }
        })
        mModel.callBack = WeakReference(this)

    }

    private fun initDialogUpdate() {
        dialogVersion = Dialog(context!!, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        val binding = FragmentUpdateVersionBinding.inflate(LayoutInflater.from(context!!))

        dialogVersion!!.setContentView(binding.getRoot())

        binding.root.setBackgroundColor(resources.getColor(R.color.white))

        dialogVersion!!.setContentView(binding.getRoot())
        binding.btnUpdate.setOnClickListener {
            openCHPlay(context!!)
        }
        dialogVersion!!.setCancelable(false)
    }

    private fun getVersionApp(): String? {
        try {
            val pInfo = context!!.getPackageManager().getPackageInfo(getBaseActivity().getPackageName(), 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


    private fun showDialog() {
        dialogVersion?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    private fun dismisDialog() {
        dialogVersion?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun initDataNormalVertion() {
        mModel.obInfor.observe(this, Observer {
            if (mModel.obInfor.value?.getSucces()!!) {
                if (null != it.getData()!!.email) {
                    email = it.getData()!!.email!!
                } else {
                    email = ""
                }
                if (null != it.getData()!!.cdl_number) {
                    cdl_number = it.getData()!!.cdl_number!!
                } else {
                    cdl_number = ""
                }
                if (null != it.getData()!!.cdl_state) {
                    cdl_state = it.getData()!!.cdl_state!!
                } else {
                    cdl_state = ""
                }
                if (null != it.getData()!!.exp_date) {
                    exp_date = it.getData()!!.exp_date!!
                } else {
                    exp_date = ""
                }
                if (null != it.getData()!!.last_name) {
                    last_name = it.getData()!!.last_name!!
                } else {
                    last_name = ""
                }
                if (null != it.getData()!!.phone_number) {
                    phone = it.getData()!!.phone_number!!
                } else {
                    phone = ""
                }
                SharedPfPermissionUtils.saveProfile(context!!, it.getData()?.first_name!!,
                        last_name,
                        email,
                        phone,
                        cdl_number,
                        cdl_state,
                        exp_date)
                mModel.obInfor.value?.getData()?.id_driver?.let { it -> SharedPfPermissionUtils.saveUserId(context!!, it) }
                try {
                    if (it.getData()?.is_complete!!) {
                        if (isSplash) {
                            val han = Handler()
                            han.postDelayed({
                                context?.let {
                                    mModel.getTrip(SharedPfPermissionUtils.getToken(it).toString())
                                }
                            }, 2000)
                        } else {
                            mModel.getTrip(SharedPfPermissionUtils.getToken(context!!).toString())
                        }

                    } else {
                        val han = Handler()
                        han.postDelayed({
                            activity?.let {
                                (it as SplashActivity).openStartActivity()
                            }
                        }, 3000)
                    }
                } catch (e: KotlinNullPointerException) {
                    val han = Handler()
                    han.postDelayed({
                        activity?.let {
                            (it as SplashActivity).openStartActivity()
                        }
                    }, 3000)
                }

            }
        })
        MVVMApplication.liveData.observe(this, Observer {
            if (it == 4) {
                SharedPfPermissionUtils.saveHasTrip(context!!, false)
                (activity as SplashActivity).openMainActivity()
                MVVMApplication.liveData.postValue(-1)
            } else if (it == 5) {
                activity?.let {
                    (it as SplashActivity).openStartActivity()
                }
                MVVMApplication.liveData.postValue(-1)
            }
        })
        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getToken(context!!))) {
            mModel.getInfor(SharedPfPermissionUtils.getToken(context!!).toString())
        } else {
            val han = Handler()
            han.postDelayed({
                activity?.let {
                    (it as SplashActivity).openStartActivity()
                }
            }, 3000)
        }
        VerifiFragment.isShowDialogBand.observe(this, Observer {
            if (it!!) {
                activity?.let {
                    SharedPfPermissionUtils.saveLogin(context!!, false)
                    (it as SplashActivity).openStartActivity()
                }
            }
        })
        mModel.obHasTrip.observe(this, Observer {
            if (mModel.obHasTrip.value!!.getSucces()) {
                SharedPfPermissionUtils.saveHasTrip(context!!, true)
                activity?.let {
                    (it as SplashActivity).openMainActivity()
                }
            }
        })
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentSplashBinding
}