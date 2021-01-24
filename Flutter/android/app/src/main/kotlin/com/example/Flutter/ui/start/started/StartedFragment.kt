package io.driverdoc.testapp.ui.start.started

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.driverdoc.testapp.R
import io.driverdoc.testapp.data.model.CCPCountry
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.utils.StringUtils
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.RelativeLayout
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import android.os.SystemClock
import android.view.*
import android.widget.FrameLayout
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication.Companion.isStarted
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.databinding.DialogAuthBinding
import io.driverdoc.testapp.ui.start.verifi.VerifiFragment
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference


class StartedFragment : BaseMvvmFragment<StartedCallBack, StartedModel>(), StartedCallBack, CountryCodeAdapter.IStoreAdapter, View.OnClickListener, View.OnFocusChangeListener {
    private var leghtText = 0
    private var hide = false
    var dialog: BottomSheetDialog? = null
    private var mLastClickTime: Long = 0
    var ip = ""

    override fun createModel(): StartedModel {
        val model = StartedModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(StartedModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_started

    override fun count() = StringUtils.getLibraryMasterCountriesEnglish().size

    override fun getData(position: Int): CCPCountry {
        return StringUtils.getLibraryMasterCountriesEnglish().get(position)
    }

    override fun onClickItem(position: Int) {
        dialog!!.dismiss()
        getDataBinding().tvNumber.setText("+" + StringUtils.getLibraryMasterCountriesEnglish().get(position).phoneCode)
        getDataBinding().img.setImageResource(StringUtils.getFlagMasterResID(StringUtils.getLibraryMasterCountriesEnglish().get(position).nameCode.toLowerCase()))
    }

    override fun initComponents() {
        getDataBinding().btnSend.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
        getDataBinding().viewModel = mModel
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

        mModel.obPhone.observe(this, Observer {
            if (mModel.obPhone.value?.success!!) {
                OpenFragmentUtils.openVerifiFragment(getBaseActivity().supportFragmentManager, StartedFragment::class.java, getDataBinding().edtPhone.text.toString(), getDataBinding().tvNumber.text.toString())
                isStarted.postValue(true)
            }
        })

        VerifiFragment.isShowDialogBand.observe(this, Observer {
            if (it!!) {
                if (!hide) {
                    showDialogAuth()
                    VerifiFragment.isShowDialogBand.postValue(false)
                }
            }

        })
        mModel.callBack = WeakReference(this)
//        val countryName = (getBaseActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).networkCountryIso
//        getDataBinding().tvNumber.setText("+" + PhoneNumberUtil.createInstance(getContext()).getCountryCodeForRegion(countryName.trim().toUpperCase()))
//        getDataBinding().img.setImageResource(StringUtils.getFlagMasterResID(context!!.getResources().getConfiguration().locale.getCountry().toLowerCase()))
    }


    override fun setEvents() {
        getDataBinding().btnFla.setOnClickListener(this)
        getDataBinding().edtPhone.setOnFocusChangeListener(this)
        getDataBinding().btnSend.setOnClickListener(this)
        getDataBinding().edtPhone.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (getDataBinding().edtPhone.text.toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").length == 10) {
                    getDataBinding().btnSend.setBackgroundResource(R.drawable.bg_btn_radius_4dp)
                } else {
                    getDataBinding().btnSend.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
                    liveData.postValue(7)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                leghtText = s.length
            }

            override fun afterTextChanged(s: Editable) {
                if (leghtText < s.length) {
                    sumtext()
                } else {
                    subText()
                }

            }
        })
    }

    private fun subText() {
        val text = getDataBinding().edtPhone.text.toString()
        if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(")") || text.endsWith("(")) {
            getDataBinding().edtPhone.setText(text.substring(0, text.length - 1))
            getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.text.toString().length)
        }

    }

    private fun sumtext() {
        val text = getDataBinding().edtPhone.getText().toString()
        val textLength = getDataBinding().edtPhone.getText()!!.length
        if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
            return
        if (textLength == 1) {
            if (!text.contains("(")) {
                getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, "(").toString())
                getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
            }
        } else if (textLength == 5) {
            if (!text.contains(")")) {
                getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, ")").toString())
                getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
            }
        } else if (textLength == 6) {
            getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, " ").toString())
            getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
        } else if (textLength == 10) {
            if (!text.contains("-")) {
                getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
            }
        } else if (textLength == 15) {
            if (text.contains("-")) {
                getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
            }
        } else if (textLength == 18) {
            if (text.contains("-")) {
                getDataBinding().edtPhone.setText(StringBuilder(text).insert(text.length - 1, "-").toString())
                getDataBinding().edtPhone.setSelection(getDataBinding().edtPhone.getText()!!.length)
            }
        }
    }

//    private fun showBottomSheet() {
////        val view = layoutInflater.inflate(R.layout.bottom_sheet_phone, null)
//        val binding = io.driverdoc.uatapp.databinding.BottomSheetPhoneBinding.inflate(layoutInflater)
//        dialog = BottomSheetDialog(context!!, R.style.BottomSheetDialog)
//        dialog!!.setContentView(binding.root)
//        binding.rcView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context!!)
//        binding.rcView.adapter = CountryCodeAdapter(this)
//        dialog!!.show()
//    }

    override fun onPauseControl() {
        super.onPauseControl()
        dialogAu?.dismiss()
    }

    private var dialogAu: android.app.AlertDialog? = null
    private fun showDialogAuth() {
        if (null != dialogAu && dialogAu!!.isShowing) {
            return
        }
        val binding = DialogAuthBinding.inflate(LayoutInflater.from(context!!))
        dialogAu = android.app.AlertDialog.Builder(context!!).create()
        dialogAu!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialogAu!!.setView(binding.getRoot())
//        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getMessageVerifyTokenFail(context!!))) {
//            binding.tvMessage.text = SharedPfPermissionUtils.getMessageVerifyTokenFail(context!!)
//        }
        binding.btnOk.setOnClickListener {
            SharedPfPermissionUtils.saveLogin(context!!, false)
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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        hide = hidden
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            getDataBinding().rl1.setBackgroundResource(R.drawable.bg_edt_sl)
            getDataBinding().btnSend.visibility = View.VISIBLE
            val param = getDataBinding().rl1.layoutParams as RelativeLayout.LayoutParams

            param.setMargins(StringUtils.convertDpToPixel(context!!, 20f),
                    0, StringUtils.convertDpToPixel(context!!, 20f)
                    , StringUtils.convertDpToPixel(context!!, 96f))
            getDataBinding().rl1.layoutParams = param
            val paramImg = getDataBinding().rl2.layoutParams as RelativeLayout.LayoutParams
            paramImg.setMargins(StringUtils.convertDpToPixel(context!!, 38f),
                    0, StringUtils.convertDpToPixel(context!!, 37f)
                    , StringUtils.convertDpToPixel(context!!, 300f))
            getDataBinding().rl2.layoutParams = paramImg


        } else {
            getDataBinding().rl1.setBackgroundResource(R.drawable.bg_edt_select)
            val han = Handler()
            try {
                han.postDelayed({
                    if (StringUtils.isEmpty(getDataBinding().edtPhone.text.toString())) {
                        getDataBinding().btnSend.visibility = View.GONE
                        val param = getDataBinding().rl1.layoutParams as RelativeLayout.LayoutParams
                        param.setMargins(StringUtils.convertDpToPixel(context!!, 20f),
                                0, StringUtils.convertDpToPixel(context!!, 20f)
                                , StringUtils.convertDpToPixel(context!!, 36f))
                        getDataBinding().rl1.layoutParams = param
                    } else {
                        val param = getDataBinding().rl1.layoutParams as RelativeLayout.LayoutParams
                        param.setMargins(StringUtils.convertDpToPixel(context!!, 20f),
                                0, StringUtils.convertDpToPixel(context!!, 20f)
                                , StringUtils.convertDpToPixel(context!!, 96f))
                        getDataBinding().rl1.layoutParams = param
                    }

                    val paramImg = getDataBinding().rl2.layoutParams as RelativeLayout.LayoutParams
                    paramImg.setMargins(StringUtils.convertDpToPixel(context!!, 38f),
                            0, StringUtils.convertDpToPixel(context!!, 37f)
                            , StringUtils.convertDpToPixel(context!!, 420f))
                    getDataBinding().rl2.layoutParams = paramImg
                }, 200)
            } catch (e: KotlinNullPointerException) {

            }
        }
    }

    override fun onBackRoot() {
        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getDataBinding().edtPhone.clearFocus()
        getDataBinding().rl1.setBackgroundResource(R.drawable.bg_edt_select)
        val han = Handler()
        try {
            han.postDelayed({
                if (StringUtils.isEmpty(getDataBinding().edtPhone.text.toString())) {
                    getDataBinding().btnSend.visibility = View.GONE
                    val param = getDataBinding().rl1.layoutParams as RelativeLayout.LayoutParams
                    param.setMargins(StringUtils.convertDpToPixel(context!!, 20f),
                            0, StringUtils.convertDpToPixel(context!!, 20f)
                            , StringUtils.convertDpToPixel(context!!, 36f))
                    getDataBinding().rl1.layoutParams = param
                } else {
                    val param = getDataBinding().rl1.layoutParams as RelativeLayout.LayoutParams
                    param.setMargins(StringUtils.convertDpToPixel(context!!, 20f),
                            0, StringUtils.convertDpToPixel(context!!, 20f)
                            , StringUtils.convertDpToPixel(context!!, 96f))
                    getDataBinding().rl1.layoutParams = param
                }


                val paramImg = getDataBinding().rl2.layoutParams as RelativeLayout.LayoutParams
                paramImg.setMargins(StringUtils.convertDpToPixel(context!!, 38f),
                        0, StringUtils.convertDpToPixel(context!!, 37f)
                        , StringUtils.convertDpToPixel(context!!, 420f))
                getDataBinding().rl2.layoutParams = paramImg
            }, 200)
        } catch (e: KotlinNullPointerException) {

        }
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.btn_fla -> {
//                showBottomSheet()
            }
            R.id.btn_send -> {
                if (getDataBinding().edtPhone.text.toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").length == 10) {
                    hideKeyBoard()
                    liveData.postValue(-1)
                }
                if (getDataBinding().edtPhone.text.toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", "").length == 10) {
                    mModel.login(getDataBinding().edtPhone.text.toString(), getDataBinding().tvNumber.text.toString(), LoadDataBinding.getVersionOS(), ip)
                }
            }
        }
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentStartedBinding
}