package io.driverdoc.testapp.ui.main.documentview

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.databinding.FragmentDocumentViewBinding
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.lifecycle.ViewModelProviders
import com.example.looprecycleview.ItemViewMode
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.ui.detectdocument.util.RuntimePermissions
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.driverdoc.testapp.data.model.DocumentLocal
import io.driverdoc.testapp.ui.base.fragment.BaseMvvmFragment
import io.driverdoc.testapp.ui.base.model.ViewModelProviderFactory
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import java.lang.ref.WeakReference
import java.util.*


class DocumentViewFragment : BaseMvvmFragment<DocumentViewCallBack, DocumentModel>(), DocumentViewAdapter.IStoreAdapter, View.OnClickListener, DocumentViewCallBack {

    private var id: Int? = null
    private var mLastClickTime: Long = 0
    private var currentItem = 0
    private var totalSize = 0
    private var isLocal = false
    private var isDelete = true
    private var listUrl: MutableList<String>? = null
    private lateinit var dialog: android.app.AlertDialog

    override fun createModel(): DocumentModel {
        val model = DocumentModel(appDatabase(), interactCommon(), schedule())
        return ViewModelProviders.of(this, ViewModelProviderFactory(model)).get(DocumentModel::class.java)
    }
    override fun getLayoutMain() = R.layout.fragment_document_view

    override fun setEvents() {
        getDataBinding().btnClose.setOnClickListener(this)
        getDataBinding().btnLeft.setOnClickListener(this)
        getDataBinding().btnRight.setOnClickListener(this)
        getDataBinding().btnRecy.setOnClickListener(this)
    }

    override fun initComponents() {
        listUrl = mutableListOf()
        getDataBinding().btnClose.setColorFilter(resources.getColor(R.color.black))
        val bundle = arguments
        bundle?.let {
            id = it.getInt(Constants.ID_TRIP)
            isLocal = it.getBoolean(Constants.LOCAL)
            if (isLocal) {
                listUrl = it.getStringArrayList(Constants.LIST_URL)
                listUrl?.sort()
                initRecycleview()
            } else {
                mModel.getPresign(id.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
            }
            isDelete = it.getBoolean(Constants.IS_DELETE)
            if (!isDelete){
                getDataBinding().btnRecy.visibility = View.GONE
            }else {
                getDataBinding().btnRecy.visibility = View.VISIBLE
            }
        }

        mModel.deleteDocument.observe(this, androidx.lifecycle.Observer {
            liveData.postValue(6)
            onBackRoot()

        })
        mModel.getPresign.observe(this, androidx.lifecycle.Observer {
            if (it.getSucces()) {
                it.getData()?.let {
                    for (i in it.size - 1 downTo 0)
                        it.get(i).url?.let {
                            listUrl!!.add(it)
                        }
                }
                initRecycleview()
            }
        })

        mModel.callBack = WeakReference(this)
    }

    private fun showDialog() {
        val binding = io.driverdoc.testapp.databinding.DialogDeleteDocumentBinding.inflate(LayoutInflater.from(context!!))
        dialog = android.app.AlertDialog.Builder(context!!).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(binding.getRoot())
        binding.btnNo.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnYes.setOnClickListener {
            dialog.dismiss()
            if (isLocal) {
                LoadDataBinding.clearApplicationData(context!!).start()
                onBackRoot()
                liveData.postValue(0)
            } else {
                listUrl = null
                mModel.deleteDocument(id!!.toString(), SharedPfPermissionUtils.getToken(context!!).toString())
            }
        }
        val window: Window = dialog.window!!
        val wlp = window.getAttributes()
        wlp.gravity = Gravity.CENTER
        window.setAttributes(wlp)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun count(): Int {
        if (null == listUrl) {
            return 0
        }
        return listUrl!!.size
    }

    override fun getData(position: Int) = listUrl!!.get(position)


    private fun initRecycleview() {
        val snapHelper = androidx.recyclerview.widget.PagerSnapHelper()
        getDataBinding().rcView.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
        getDataBinding().rcView.adapter = DocumentViewAdapter(this, context!!)
        snapHelper.attachToRecyclerView(getDataBinding().rcView)
        totalSize = getDataBinding().rcView.adapter!!.itemCount
        if (listUrl!!.size == 0) {
            getDataBinding().tv1.setText("Page 0 of " + totalSize)
        } else {
            getDataBinding().tv1.setText("Page 1 of " + totalSize)
        }
        getDataBinding().rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                currentItem = (getDataBinding().rcView.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                getDataBinding().tv1.setText("Page " + currentItem + " of " + totalSize)
                getDataBinding().rcView.adapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_close -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                onBackRoot()
            }
            R.id.btn_left -> {
                if (listUrl!!.size != 0) {
                    if ((getDataBinding().rcView.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition() >= 1) {
                        getDataBinding().rcView.smoothScrollToPosition((getDataBinding().rcView.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition() - 1)
                    } else {
                        getDataBinding().rcView.smoothScrollToPosition(totalSize - 1)
                    }
                }

            }
            R.id.btn_right -> {
                if (listUrl!!.size != 0) {
                    if ((getDataBinding().rcView.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition() < totalSize - 1) {
                        getDataBinding().rcView.smoothScrollToPosition((getDataBinding().rcView.getLayoutManager() as LinearLayoutManager).findFirstVisibleItemPosition() + 1)
                    } else {
                        getDataBinding().rcView.smoothScrollToPosition(0)
                    }
                }
            }
            R.id.btn_recy -> {
                if (isLocal) {
                    RuntimePermissions.instance().runWithPermission(getBaseActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            R.string.permission_query_write_storage) { permission, granted ->
                        if (granted) {
                            showDialog()
                        }
                    }
                } else {
                    showDialog()
                }

            }
        }
    }

    fun getDataBinding() = mBinding as FragmentDocumentViewBinding
}