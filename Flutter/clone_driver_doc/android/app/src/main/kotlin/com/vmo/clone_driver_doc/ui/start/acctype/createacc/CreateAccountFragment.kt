package io.driverdoc.testapp.ui.start.acctype.createacc

import android.app.DatePickerDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.databinding.BotomSheetDatePickerBinding
import io.driverdoc.testapp.databinding.FragmentCreateAccBinding
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.start.StartActivity
import io.driverdoc.testapp.ui.utils.OpenFragmentUtils
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.IndexOutOfBoundsException
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

class CreateAccountFragment : BaseMvvmFragment<CreateAccountCallBack, CreateAccountModel>(), CreateAccountCallBack, View.OnClickListener {
    private var isOpen = false
    private var state = ""
    private var dateRespone = ""
    var list: ArrayList<String>? = null


    override fun createModel(): CreateAccountModel {
        val model = CreateAccountModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(CreateAccountModel::class.java)
    }

    override fun getLayoutMain() = R.layout.fragment_create_acc
    override fun setEvents() {
        getDataBinding().tvDate.setOnClickListener(this)
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().btnSave.setOnClickListener(this)
        checkFocus(getDataBinding().edtFirstName, getDataBinding().view1)
        checkFocus(getDataBinding().edtLastName, getDataBinding().view2)
        checkFocus(getDataBinding().edtPhone, getDataBinding().view3)
        checkFocus(getDataBinding().edtEmail, getDataBinding().view4)
        checkFocus(getDataBinding().edtCdl, getDataBinding().view5)
        checkTextChangePhone(getDataBinding().edtPhone)
        getDataBinding().edtState.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event!!.getAction() == MotionEvent.ACTION_UP) {
                    isOpen = false
                    if (list!!.size < 1) {
                        initSpiner()
                    }

                }
                return false
            }

        })
        getDataBinding().tvDate.setOnClickListener(this)
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
                checksStatusBtn()

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
                checksStatusBtn()

            }

        }, dateRespone.substring(dateRespone.lastIndexOf("/") + 1).toInt(), dateRespone.substring(0, 2).toInt() - 1, dateRespone.substring(3, 5).toInt());
        startTime.show()
    }




    private fun checkFocus(edt: EditText, v1: View) {
        edt.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v1.visibility = View.VISIBLE
            } else {
                checksStatusBtn()
                edt.setText(edt.text.toString().trim())
                v1.visibility = View.GONE
            }
        }
    }

    private fun checkTextChangePhone(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                StringUtils.formatPhone(getDataBinding().edtPhone)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun initComponents() {
        list = ArrayList<String>()
        MVVMApplication.liveData.postValue(-1)
        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getState(context!!))) {
            initSpiner()
            for (i in 0 until list!!.size) {
                if (list!!.get(i).equals(SharedPfPermissionUtils.getState(context!!))) {
                    isOpen = true
                    getDataBinding().edtState.setSelection(i)
                    break
                }
            }
        } else {
            initSpinerNoData()
        }
        getDataBinding().btnSave.isEnabled = false
        getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
        getDataBinding().btnClose.setColorFilter(resources.getColor(R.color.black))
        mModel.obUser.observe(this, androidx.lifecycle.Observer {
            if (it.getSucces()) {
                showMessage(it.getMessage())
                SharedPfPermissionUtils.saveProfile(context!!, it.getData()?.first_name!!,
                        it.getData()?.last_name!!,
                        it.getData()?.email!!,
                        it.getData()?.phone_number!!,
                        it.getData()?.cdl_number!!,
                        it.getData()?.cdl_state!!,
                        it.getData()?.exp_date!!)
                (activity as StartActivity).openPermistionActivity()
            }
        })
        getDataBinding().edtFirstName.setText(SharedPfPermissionUtils.getFirstName(context!!))
        getDataBinding().edtLastName.setText(SharedPfPermissionUtils.getLastName(context!!))
        if (!StringUtils.isEmpty(SharedPfPermissionUtils.getPhone(context!!))) {
            for (i in 2 until SharedPfPermissionUtils.getPhone(context!!)!!.length) {
                getDataBinding().edtPhone.append(SharedPfPermissionUtils.getPhone(context!!)?.get(i).toString())
            }
        }
        fillData(getDataBinding().edtCdl, SharedPfPermissionUtils.getCdlNumber(context!!).toString())
        fillData(getDataBinding().edtEmail, SharedPfPermissionUtils.getEmail(context!!).toString())
        try {
            dateRespone = SharedPfPermissionUtils.getDate(context!!).toString()
            val dateShow = dateRespone.substring(0, dateRespone.lastIndexOf("/")) + "/" + dateRespone.substring(dateRespone.length - 2)
            getDataBinding().tvDate.setText(dateShow)
        } catch (e: IndexOutOfBoundsException) {

        }
        StringUtils.formatPhone(getDataBinding().edtPhone)
        getDataBinding().view6.visibility = View.GONE
        mModel.callBack = WeakReference(this)
        checksStatusBtn()
    }


    private fun fillData(edtCdl: EditText, cdlNumber: String) {
        edtCdl.setText(cdlNumber)
    }

    private fun initSpiner() {
        list!!.clear()
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
        val adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, list!!);
        getDataBinding().edtState.setAdapter(adapter)
        getDataBinding().edtState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (!isOpen) {
                    checksStatusBtn()
                    state = list!!.get(position)
                    getDataBinding().imgState.setImageResource(R.drawable.ic_keyboard_arrow_down_gray_24dp)
                    getDataBinding().view6.visibility = View.GONE
                    isOpen = true
                }
            }

        })

    }

    private fun initSpinerNoData() {
        val adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, list!!);
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
    }


    private fun checksStatusBtn() {
        if (
                !StringUtils.isEmpty(getDataBinding().edtCdl.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtPhone.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtLastName.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtFirstName.text.toString().trim()) &&
                !StringUtils.isEmpty(getDataBinding().edtEmail.text.toString().trim()) &&
                (null != list && list!!.size > 0) &&
                !StringUtils.isEmpty(getDataBinding().tvDate.text.toString().trim())
        ) {
            getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_radius_4dp)
            getDataBinding().btnSave.isEnabled = true
        } else {
            getDataBinding().btnSave.setBackgroundResource(R.drawable.bg_btn_gray_4dp)
            getDataBinding().btnSave.isEnabled = false
        }
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_close -> {
                hideKeyBoard()
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
            }
        }
    }
    fun getDataBinding() = mBinding as FragmentCreateAccBinding
}