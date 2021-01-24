package io.driverdoc.testapp.ui.start.verifi

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.customview.EditTextMedium
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import android.content.Context
import android.os.Handler
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import java.lang.ref.WeakReference
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.ui.start.StartActivity
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class VerifiFragment : BaseMvvmFragment<VerifiCallBack, VerifiModel>(), View.OnClickListener, VerifiCallBack, View.OnFocusChangeListener {
    var email = ""
    var cdl_number = ""
    var cdl_state = ""
    var exp_date = ""
    var last_name = ""
    var first_name = ""
    var phone_number = ""
    var ip = ""
    private var phone = ""
    private var countryCode = ""
    private var hide = false
    private var mLastClickTime: Long = 0
    private var showKeyboard = MutableLiveData<Boolean>()
    private lateinit var dialog: android.app.AlertDialog
    private var dialogBand: android.app.AlertDialog? = null

    companion object {
        var isShowDialog = MutableLiveData<Boolean>()
        var isShowDialogBand = MutableLiveData<Boolean>()
    }

    override fun createModel(): VerifiModel {
        val model = VerifiModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(VerifiModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_vetifi

    override fun setEvents() {
        getDataBinding().btnClose.setOnClickListener(this)
        addTextChange(getDataBinding().edt1, getDataBinding().edt2)
        addTextChange(getDataBinding().edt2, getDataBinding().edt3)
        addTextChange(getDataBinding().edt3, getDataBinding().edt4)
        keyCodeDelete(getDataBinding().edt4)
        keyCodeDelete(getDataBinding().edt3)
        keyCodeDelete(getDataBinding().edt2)
        forcus(getDataBinding().edt2)
        forcus(getDataBinding().edt3)
        forcus(getDataBinding().edt4)

        getDataBinding().btnResend.setOnClickListener(this)
    }

    private fun forcus(edt1: EditTextMedium?) {
        edt1!!.setOnFocusChangeListener(this)
    }

    private fun keyCodeDelete(edt2: EditTextMedium) {
        edt2.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    requestFocus()
                }
                return false
            }
        })
    }

    private fun addTextChange(edt1: EditTextMedium, edt2: EditTextMedium) {
        edt1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edt1.text.toString().length == 1) {
                    edt2.requestFocus()
                }
            }

        })

        edt2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!StringUtils.isEmpty(getDataBinding().edt1.text.toString())
                        &&
                        !StringUtils.isEmpty(getDataBinding().edt2.text.toString())
                        &&
                        !StringUtils.isEmpty(getDataBinding().edt3.text.toString())
                        &&
                        !StringUtils.isEmpty(getDataBinding().edt4.text.toString())
                ) {
                    mModel.verifiCode(countryCode, phone,
                            getDataBinding().edt1.text.toString() +
                                    getDataBinding().edt2.text.toString() +
                                    getDataBinding().edt3.text.toString() +
                                    getDataBinding().edt4.text.toString()
                            , LoadDataBinding.getVersionOS(), ip)
                }
            }
        })
    }

    fun showDialogResend() {
        val binding = io.driverdoc.testapp.databinding.DialogResendCodeBinding.inflate(LayoutInflater.from(context!!))
        dialog = android.app.AlertDialog.Builder(context!!).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setView(binding.getRoot())

        binding.btnResend.setOnClickListener {
            dialog.dismiss()
            mModel.login(phone, countryCode, LoadDataBinding.getVersionOS(), ip)
        }
        binding.btnTry.setOnClickListener {
            dialog.dismiss()
            requestFocus()
            val handler = Handler()
            handler.postDelayed({
                showKeyboard.postValue(true)
            }, 500)
        }

        val window: Window = dialog.window!!
        val wlp = window.getAttributes()

        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialog.setCancelable(false)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }


    private fun requestFocus() {
        getDataBinding().edt1.post(Runnable {
            getDataBinding().edt1.setText("")
            getDataBinding().edt2.setText("")
            getDataBinding().edt3.setText("")
            getDataBinding().edt4.setText("")
            getDataBinding().edt1.requestFocus()
            val imgr = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imgr.showSoftInput(getDataBinding().edt1, InputMethodManager.SHOW_IMPLICIT)
        })
    }

    @SuppressLint("SetTextI18n")
    override fun initComponents() {
        liveData.postValue(7)
        val bundle = arguments
        bundle?.let {
            phone = it.getString(Constants.PHONE)?.replace("(", "")?.replace(")", "")?.replace(" ", "")?.replace("-", "").toString()
            countryCode = it.getString(Constants.COUNTRY_CODE)?.replace("+", "").toString()
            getDataBinding().tv1.setText("Enter 4 digit code we sent to " + it.getString(Constants.PHONE))
        }
        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getIpPublic(context!!).toString())) {
            ip = SharedPfPermissionUtils.getIpPublic(context!!).toString()
        } else {
            val client = OkHttpClient()

            val request = Request.Builder()
                    .url(Constants.URL_GET_IP)
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {}
                override fun onResponse(call: okhttp3.Call, response: Response) {

                    response.body()?.string()?.let {
                        ip = it
                        SharedPfPermissionUtils.saveIpPublic(context!!, it)
                    }
                }
            })
        }
        if (!hide) {
            requestFocus()
        }
        getDataBinding().btnClose.setColorFilter(resources.getColor(R.color.black))
        getDataBinding().edt1.setFilters(arrayOf(InputFilter.LengthFilter(1)))
        getDataBinding().edt2.setFilters(arrayOf(InputFilter.LengthFilter(1)))
        getDataBinding().edt3.setFilters(arrayOf(InputFilter.LengthFilter(1)))
        getDataBinding().edt4.setFilters(arrayOf(InputFilter.LengthFilter(1)))
        getDataBinding().viewModel = mModel
        mModel.obVerifi.observe(this, Observer {
            if (mModel.obVerifi.value?.getSucces()!!) {
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
                if (null != it.getData()!!.first_name) {
                    first_name = it.getData()!!.first_name!!
                } else {
                    first_name = ""
                }
                if (null != it.getData()!!.phone_number) {
                    phone_number = it.getData()!!.phone_number!!
                } else {
                    phone_number = ""
                }
                SharedPfPermissionUtils.saveProfile(context!!, first_name,
                        last_name,
                        email,
                        phone_number,
                        cdl_number,
                        cdl_state,
                        exp_date)
                mModel.obVerifi.value?.getData()?.jwtToken?.let { it1 -> SharedPfPermissionUtils.saveJwtToken(context!!, it1) }
                mModel.obVerifi.value?.getData()?.id_driver?.let { it -> SharedPfPermissionUtils.saveUserId(context!!, it) }

                SharedPfPermissionUtils.saveLogin(context!!, true)
                if (it!!.getData()!!.is_complete != null && !it.getData()!!.is_complete!!) {
                    OpenFragmentUtils.openCreateAccountFragment(getBaseActivity().supportFragmentManager, VerifiFragment::class.java)
                } else {
                    (activity as StartActivity).openPermistionActivity()
                }

            } else {
                showDialogResend()
            }
        })
        mModel.obPhone.observe(this, Observer {
            if (mModel.obPhone.value?.success!!) {
                mModel.obPhone.value?.message?.let { it1 ->
                    showMessage(it1)
                    requestFocus()
                    val han = Handler()
                    han.postDelayed({
                        showKeyboard.postValue(true)
                    }, 500)
                }
            }
        })
        showKeyboard.observe(this, Observer {
            if (it!!) {
                val imgr = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imgr.showSoftInput(getDataBinding().edt1, InputMethodManager.SHOW_IMPLICIT)
            } else {
                hideKeyBoard()
            }
        })
        isShowDialog.observe(this, Observer {
            if (it!!) {
                showDialogResend()
                isShowDialog.postValue(false)
            }
        })
        isShowDialogBand.observe(this, Observer {
            if (SharedPfPermissionUtils.getLogin(context!!)) {
                if (it!!) {
                    if (!hide) {
                        showDialogBand()
                        isShowDialogBand.postValue(false)
                    }
                }
            } else {
                isShowDialogBand.postValue(false)
            }
        })
        mModel.callBack = WeakReference(this)
    }

    override fun onBackRoot() {
        hideKeyBoard()
        getDataBinding().edt1.clearFocus()
        getDataBinding().edt3.clearFocus()
        getDataBinding().edt2.clearFocus()
        getDataBinding().edt4.clearFocus()
        MVVMApplication.isStarted.postValue(true)
        super.onBackRoot()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        hide = hidden
        if (hidden) {
            showKeyboard.postValue(false)
        }
    }

    fun showDialogBand() {
        val binding = io.driverdoc.testapp.databinding.DialogAccBandBinding.inflate(LayoutInflater.from(context!!))
        dialogBand = android.app.AlertDialog.Builder(context!!).create()
        dialogBand!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogBand!!.setView(binding.getRoot())
//        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getMessageVerifyTokenFail(context!!))){
//            binding.tvContent.text = SharedPfPermissionUtils.getMessageVerifyTokenFail(context!!)
//        }else {
        binding.tvContent.setText("Phone number is invalid or locked. Please contact your company administrator.")
//        }
        binding.btnOk.setOnClickListener {
            SharedPfPermissionUtils.saveLogin(context!!, false)
            SharedPfPermissionUtils.saveJwtToken("")
            dialogBand!!.dismiss()
        }
        val window: Window = dialogBand!!.window!!
        val wlp = window.getAttributes()
        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        dialogBand!!.setCancelable(false)
        if (null != dialogBand && !dialogBand!!.isShowing) {
            dialogBand!!.show()
        }
    }

    override fun onPauseControl() {
        super.onPauseControl()
        dialogBand?.dismiss()
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v == getDataBinding().edt2) {
            if (hasFocus && StringUtils.isEmpty(getDataBinding().edt1.text.toString())) {
                requestFocus()
            }
        }
        if (v == getDataBinding().edt3) {
            if (hasFocus && StringUtils.isEmpty(getDataBinding().edt2.text.toString())) {
                getDataBinding().edt2.requestFocus()
            }
        }
        if (v == getDataBinding().edt4) {
            if (hasFocus && StringUtils.isEmpty(getDataBinding().edt3.text.toString())) {
                getDataBinding().edt3.requestFocus()
            }
        }

    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_resend -> {
                mModel.login(phone, countryCode, LoadDataBinding.getVersionOS(), ip)
            }
            R.id.btn_close -> {
                onBackRoot()
            }
        }
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentVetifiBinding
}