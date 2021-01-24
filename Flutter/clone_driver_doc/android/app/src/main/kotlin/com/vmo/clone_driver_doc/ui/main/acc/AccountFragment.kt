package io.driverdoc.testapp.ui.main.acc

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.databinding.BotomSheetDatePickerBinding
import io.driverdoc.testapp.databinding.DialogNewTripBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.main.MainActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.IndexOutOfBoundsException
import java.lang.ref.WeakReference
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AccountFragment : BaseMvvmFragment<AccountCallBack, AccountModel>(), View.OnClickListener, AccountCallBack, View.OnFocusChangeListener, TextWatcher {
    private var isHide = false
    private var str = ""
    private var isOpen = false
    private var state = ""
    private var dateRespone = ""
    var list: ArrayList<String>? = null
    override fun afterTextChanged(s: Editable?) {
        str = getDataBinding().edtPhone.text.toString()
        StringUtils.formatPhone(getDataBinding().edtPhone)
    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (v == getDataBinding().edtFirstName) {
            if (hasFocus) {
                getDataBinding().view1.visibility = View.VISIBLE
                getDataBinding().view2.visibility = View.GONE
                getDataBinding().btnSave.visibility = View.GONE
            } else {
                getDataBinding().view1.visibility = View.GONE
                getDataBinding().view2.visibility = View.GONE
                getDataBinding().edtFirstName.setText(getDataBinding().edtFirstName.text.toString().trim())
                getDataBinding().btnSave.visibility = View.VISIBLE

            }

        }
        if (v == getDataBinding().edtLastName) {
            if (hasFocus) {
                getDataBinding().view1.visibility = View.GONE
                getDataBinding().view2.visibility = View.VISIBLE
                getDataBinding().btnSave.visibility = View.GONE
            } else {
                getDataBinding().view1.visibility = View.GONE
                getDataBinding().view2.visibility = View.GONE
                getDataBinding().edtLastName.setText(getDataBinding().edtLastName.text.toString().trim())
                getDataBinding().btnSave.visibility = View.VISIBLE

            }
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_close -> {
                onBackRoot()
            }
            R.id.btn_save -> {
                if (!StringUtils.isValidEmailAddress(getDataBinding().edtEmail.text.toString())) {
                    showMessage("Invalid email address.")
                } else if (getDataBinding().edtPhone.text.toString().length < 14) {
                    showMessage("Invalid phone number.")
                } else {
                    mModel.updateUser(getDataBinding().edtPhone.text.toString().replace("(", "").replace(")", "").replace(" ", "").replace("-", ""),
                            getDataBinding().edtEmail.text.toString(),
                            getDataBinding().edtFirstName.text.toString(),
                            getDataBinding().edtLastName.text.toString(),
                            dateRespone,
                            getDataBinding().edtCdl.text.toString(),
                            state,
                            SharedPfPermissionUtils.getToken(context!!).toString())
                }
            }

            R.id.tv_date -> {
                showDatePicker()
//                showBottomSheetMonth()
            }
        }
    }

    private fun showDatePicker() {
        try {
            if (StringUtils.isEmpty(getDataBinding().tvDate.text.toString())) {
                showPickerNoData()
            } else {
                showPickerHasData()
            }
        } catch (e: IndexOutOfBoundsException) {
            showPickerNoData()
        }


    }

    private fun showPickerNoData() {
        val newCalendar = Calendar.getInstance()
        val startTime = DatePickerDialog(context!!, R.style.Picker, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                var m = ""
                var d = ""
                if (p2 + 1 > 9) {
                    m = (p2 + 1).toString()
                } else {
                    m = "0" + (p2 + 1).toString()
                }
                if (p3 > 9) {
                    d = p3.toString()
                } else {
                    d = "0" + p3.toString()
                }
                dateRespone = m + "/" + d + "/" + p1.toString()
                val dateShow = dateRespone.substring(0, dateRespone.lastIndexOf("/")) + "/" + dateRespone.substring(dateRespone.length - 2)
                getDataBinding().tvDate.setText(dateShow)
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        startTime.show()
    }

    private fun showPickerHasData() {
        val startTime = DatePickerDialog(context!!, R.style.Picker, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                var m = ""
                var d = ""
                if (p2 + 1 > 9) {
                    m = (p2 + 1).toString()
                } else {
                    m = "0" + (p2 + 1).toString()
                }
                if (p3 > 9) {
                    d = p3.toString()
                } else {
                    d = "0" + p3.toString()
                }
                dateRespone = m + "/" + d + "/" + p1.toString()
                val dateShow = dateRespone.substring(0, dateRespone.lastIndexOf("/")) + "/" + dateRespone.substring(dateRespone.length - 2)
                getDataBinding().tvDate.setText(dateShow)
            }

        }, dateRespone.substring(dateRespone.lastIndexOf("/") + 1).toInt(), dateRespone.substring(0, 2).toInt() - 1, dateRespone.substring(3, 5).toInt());
        startTime.show()
    }


    override fun onBackRoot() {
        hideKeyBoard()
        liveData.postValue(Constants.RELOAD_TRIP)
        super.onBackRoot()
    }

    override fun createModel(): AccountModel {
        val model = AccountModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(AccountModel::class.java)

    }

    override fun getLayoutMain() = R.layout.fragment_acc
    private fun checkFocus(edt: EditText, v1: View) {
        edt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v1.visibility = View.VISIBLE
            } else {
                checksStatusBtn()
                v1.visibility = View.GONE
                edt.setText(edt.text.toString().trim())
            }
        }
    }

    private fun checksStatusBtn() {
        if (
                !StringUtils.isEmpty(getDataBinding().edtCdl.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtPhone.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtLastName.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtFirstName.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtEmail.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().tvState.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().tvDate.text.toString().trim())
        ) {
            getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_radius_4dp)
            getDataBinding().btnSave.isEnabled = true
        } else {
            getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
            getDataBinding().btnSave.isEnabled = false
        }
    }

    override fun setEvents() {
        getDataBinding().tvDate.setOnClickListener(this)
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().btnSave.setOnClickListener(this)
        getDataBinding().edtPhone.addTextChangedListener(this)
        checkFocus(getDataBinding().edtFirstName, getDataBinding().view1)
        checkFocus(getDataBinding().edtLastName, getDataBinding().view2)
        checkFocus(getDataBinding().edtPhone, getDataBinding().view3)
        checkFocus(getDataBinding().edtEmail, getDataBinding().view4)
        checkFocus(getDataBinding().edtCdl, getDataBinding().view6)
        getDataBinding().edtState.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event!!.getAction() == MotionEvent.ACTION_UP) {
                    isOpen = false

                }
                return false
            }


        })
        getDataBinding().tvDate.setOnClickListener(this)
    }

    private fun initSpiner(curentState: String) {
        list = ArrayList<String>()
        list!!.add("AL")
        list!!.add("AK")
        list!!.add("AZ")
        list!!.add("AR")
        list!!.add("CA")
        list!!.add("CO")
        list!!.add("CT")
        list!!.add("DE")
        list!!.add("FL")
        list!!.add("GA")
        list!!.add("HI")
        list!!.add("ID")
        list!!.add("IL")
        list!!.add("IN")
        list!!.add("IA")
        list!!.add("KS")
        list!!.add("KY")
        list!!.add("LA")
        list!!.add("ME")
        list!!.add("MD")
        list!!.add("MA")
        list!!.add("MI")
        list!!.add("MN")
        list!!.add("MS")
        list!!.add("MO")
        list!!.add("MT")
        list!!.add("NE")
        list!!.add("NV")
        list!!.add("NH")
        list!!.add("NJ")
        list!!.add("NM")
        list!!.add("NY")
        list!!.add("NC")
        list!!.add("ND")
        list!!.add("OH")
        list!!.add("OK")
        list!!.add("OR")
        list!!.add("PA")
        list!!.add("RI")
        list!!.add("SC")
        list!!.add("SC")
        list!!.add("TN")
        list!!.add("TX")
        list!!.add("UT")
        list!!.add("VT")
        list!!.add("VA")
        list!!.add("WA")
        list!!.add("WV")
        list!!.add("WI")
        list!!.add("WY")
        list!!.add("DC")
        list!!.add("MH")
        list!!.add("AE")
        list!!.add("AA")
        list!!.add("AP")
        state = list!!.get(0)
        val adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, list!!)
        getDataBinding().edtState.setAdapter(adapter)
        getDataBinding().edtState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isOpen) {
                    checksStatusBtn()
                    state = list!!.get(position)
                    getDataBinding().view6.visibility = View.GONE
                    isOpen = true
                }
            }
        })
        for (i in 0 until list!!.size) {
            if (list!!.get(i).equals(curentState)) {
                getDataBinding().edtState.setSelection(i)
            }
        }
    }


    override fun initComponents() {
        liveData.postValue(-1)
        Handler().postDelayed({
            SharedPfPermissionUtils.getToken(context!!)?.let { mModel.getInfor(it) }
        }, 300)
        mModel.obInfor.observe(this, Observer {
            it.getData()?.first_name?.let {
                getDataBinding().edtFirstName.setText(it)
            }
            it.getData()?.last_name?.let {
                getDataBinding().edtLastName.setText(it)
            }
            it.getData()?.phone_number?.let {
                for (i in 2 until it.length) {
                    getDataBinding().edtPhone.append(it.get(i).toString())
                }
            }
            getDataBinding().tvNum.visibility=View.VISIBLE
            StringUtils.formatPhone(getDataBinding().edtPhone)

            it.getData()?.cdl_number?.let {
                fillData(getDataBinding().edtCdl, it.toString())
            }
            it.getData()?.email?.let {
                fillData(getDataBinding().edtEmail, it.toString())
            }
            try {
                dateRespone = it.getData()?.exp_date.toString()
                val dateShow = dateRespone.substring(0, dateRespone.lastIndexOf("/")) + "/" + dateRespone.substring(dateRespone.length - 2)
                getDataBinding().tvDate.setText(dateShow)
            } catch (e: IndexOutOfBoundsException) {

            }
            if (null != it.getData()?.cdl_state) {
                initSpiner(it.getData()?.cdl_state!!)
            } else {
                initSpiner("AL")
            }
        })

        getDataBinding().btnClose.setColorFilter(resources.getColor(R.color.white))
        mModel.obUser.observe(this, Observer {
            if (mModel.obUser.value!!.getSucces()) {
                showMessage(it.getMessage())
//                SharedPfPermissionUtils.saveProfile(context!!, it.getData()?.first_name!!,
//                        it.getData()?.last_name!!,
//                        it.getData()?.email!!,
//                        it.getData()?.phone_number!!,
//                        it.getData()?.cdl_number!!,
//                        it.getData()?.cdl_state!!,
//                        it.getData()?.exp_date!!)

            }
        })

        getDataBinding().btnSave.isEnabled = false

        getDataBinding().view6.visibility = View.GONE
        getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
        mModel.callBack = WeakReference(this)
    }

    private fun fillData(edtCdl: EditText, cdlNumber: String) {
        edtCdl.setText(cdlNumber)
    }

    private var dialog: android.app.AlertDialog? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isHide = hidden
    }

    fun getDataBinding() = mBinding as io.driverdoc.testapp.databinding.FragmentAccBinding
}