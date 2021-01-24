package io.driverdoc.testapp.ui.start.acctype

import android.os.SystemClock
import android.view.View
import androidx.lifecycle.ViewModelProviders
import io.driverdoc.testapp.R
import io.driverdoc.testapp.databinding.FragmentAccountTypeBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory

class AccountTypeFragment : BaseMvvmFragment<AccountTypeCallBack, AccountTypeModel>(), AccountTypeCallBack, View.OnClickListener {
    private var mLastClickTime: Long = 0
    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_driver -> {
//                OpenFragmentUtils.openPermisstionFragment(getBaseActivity().supportFragmentManager, AccountTypeFragment::class.java)

            }
            R.id.btn_owner -> {
//                OpenFragmentUtils.openPermisstionFragment(getBaseActivity().supportFragmentManager, AccountTypeFragment::class.java)
            }
        }
    }

    override fun createModel(): AccountTypeModel {
        val model = AccountTypeModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(AccountTypeModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_account_type

    override fun setEvents() {
        getDataBiding().btnDriver.setOnClickListener(this)
        getDataBiding().btnOwner.setOnClickListener(this)
    }

    override fun initComponents() {
    }

    fun getDataBiding() = mBinding as FragmentAccountTypeBinding
}