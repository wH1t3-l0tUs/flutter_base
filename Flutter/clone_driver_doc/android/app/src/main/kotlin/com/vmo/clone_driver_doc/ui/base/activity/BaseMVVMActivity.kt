package io.driverdoc.testapp.ui.base.activity

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import io.driverdoc.testapp.data.model.base.error.ResponseException
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.driverdoc.testapp.ui.base.callback.BaseCallBack
import java.util.*

abstract class BaseMVVMActivity<
        CallBack : BaseCallBack,
        Model : BaseViewModel<CallBack>> : BaseActivity(), BaseCallBack {
    protected lateinit var mModel: Model
    protected var mFirstLoad: Long = 0

    override fun onCreateControl(savedInstanceState: Bundle?) {
        if (!mIsClearMemoryActivity) {
            mFirstLoad = Date().time
            mBinding = DataBindingUtil.setContentView(this, getLayoutMain())
            mModel = createModel()
            performDataBinding()
            setEvents()
            initComponents()
        }
        super.onCreateControl(savedInstanceState)
    }


    abstract fun createModel(): Model
    fun getBindingVariable() = io.driverdoc.testapp.BR.viewModel
    private fun performDataBinding() {
        mBinding.setVariable(getBindingVariable(), mModel)
        mBinding.executePendingBindings()
    }

    protected fun <T> finishLoad(t: T, action: (T) -> Unit) {
        if (mIsDestroyView) {
            return
        }
        if (mFirstLoad == (-1).toLong()) {
            action(t)
        } else {
            val currentTime = Date().time
            if (currentTime - mFirstLoad >= io.driverdoc.testapp.common.Constants.DURATION_ANIMATION) {
                action(t)
            } else {
                Handler().postDelayed({
                    if (mIsDestroyView) {
                        return@postDelayed
                    }
                    action(t)
                }, io.driverdoc.testapp.common.Constants.DURATION_ANIMATION - (currentTime - mFirstLoad))
            }
            mFirstLoad = -1
        }
    }

    override fun onError(id: String, error: ResponseException) {
        if (mIsDestroyView){
            return
        }
        if (error.message != null ){
            showMessage(error.message)
        }
    }

    override fun onErrorList(id: String, error: ResponseException) {
        if (mIsDestroyView){
            return
        }
        if (error.message != null ){
            showMessage(error.message)
        }
    }
}