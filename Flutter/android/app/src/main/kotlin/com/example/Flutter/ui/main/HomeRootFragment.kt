package io.driverdoc.testapp.ui.main


import android.content.*
import android.os.Bundle
import android.os.IBinder

import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference
import io.driverdoc.testapp.ui.main.service.LocationUpdatesService


class HomeRootFragment : BaseMvvmFragment<HomeRootCallBack, HomeRootModel>(), HomeRootCallBack {
    private var mService: LocationUpdatesService? = null

    override fun createModel(): HomeRootModel {
        val model = HomeRootModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(HomeRootModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_root

    override fun setEvents() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBaseActivity().bindService(Intent(getBaseActivity(), LocationUpdatesService::class.java), mServiceConnection,
                Context.BIND_AUTO_CREATE)
        if (null == getCurrentFragment(getBaseActivity().supportFragmentManager)?.tag) {
            val bun = arguments
            bun?.getBoolean(Constants.IS_ACTIVE)?.let {
                if (!SharedPfPermissionUtils.getTrip(context!!)) {
                    if (it) {
                        OpenFragmentUtils.openTripFragment(getBaseActivity().supportFragmentManager, HomeRootFragment::class.java)
                    } else {
                        OpenFragmentUtils.openDashboardFragment(getBaseActivity().supportFragmentManager, HomeRootFragment::class.java)
                    }
                    SharedPfPermissionUtils.saveTrip(context!!, true)
                }

            }

        }
    }

    // Tracks the bound state of the service.
    private val mServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationUpdatesService.LocalBinder
            mService = binder.service
            startService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }
    }

    override fun initComponents() {
        FirebaseInstanceId.getInstance().token?.let {
            mModel.notifi(it, "android", SharedPfPermissionUtils.getUserId(context!!).toString(), SharedPfPermissionUtils.getToken(context!!).toString())
        }
        getDataBinding().viewModel = mModel
        mModel.callBack = WeakReference(this)
    }

    private fun startService() {
        mService!!.requestLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        try {
            if ((activity as MainActivity).isMyServiceRunning(LocationUpdatesService::class.java)) {
                getBaseActivity().unbindService(mServiceConnection)
            }
        } catch (e: IllegalArgumentException) {

        }


    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentRootBinding

}