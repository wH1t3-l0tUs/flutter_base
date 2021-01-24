package io.driverdoc.testapp.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmail.samehadar.iosdialog.IOSDialog
import io.driverdoc.testapp.BR
import io.driverdoc.testapp.data.model.base.error.ResponseException
import io.driverdoc.testapp.ui.base.model.BaseViewModel
import io.driverdoc.testapp.ui.base.callback.BaseCallBack
import java.util.*

abstract class BaseMvvmFragment<
        CallBack : BaseCallBack,
        Model : BaseViewModel<CallBack>
        > : BaseFragment(), BaseCallBack {
    protected var mFirstLoad: Long = 0
    protected lateinit var mModel: Model
    protected lateinit var loading: IOSDialog
    override fun onCreateViewControl(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateViewControl(inflater, container, savedInstanceState)
        mModel = createModel()
        getBindingVariable()
        return view

    }

    override fun onViewCreatedControl(view: View, savedInstanceState: Bundle?) {
        mFirstLoad = Date().time
        super.onViewCreatedControl(view, savedInstanceState)

        loading = IOSDialog.Builder(context!!)
                .setCancelable(false)
                .build()
        mModel.isLoading().observe(this, androidx.lifecycle.Observer {
            if (it!!) {
                if (loading.isShowing) {
                    loading.dismiss()
                }
                loading.show()
            } else {
                loading.dismiss()
            }
        })
    }

    abstract fun createModel(): Model
    fun getBindingVariable() = BR.viewModel
    private fun performDataBinding() {
        mBinding.setVariable(getBindingVariable(), mModel)
        mBinding.executePendingBindings()
    }

    override fun onError(id: String, error: ResponseException) {
        if (mIsDestroyView) {
            return
        }
        if (error.message != null) {
            showMessage(error.message)
        }
    }

    override fun onErrorList(id: String, error: ResponseException) {
        if (mIsDestroyView) {
            return
        }
        if (error.message != null) {
            showMessage(error.message)
        }
    }

    override fun onPauseControl() {
        super.onPauseControl()
        if (loading.isShowing) {
            loading.dismiss()
        }
    }
}