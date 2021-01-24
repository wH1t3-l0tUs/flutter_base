package io.driverdoc.testapp.ui.detectdocument

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.gmail.samehadar.iosdialog.IOSDialog
import io.driverdoc.testapp.R
import io.driverdoc.testapp.common.Constants
import io.driverdoc.testapp.common.MVVMApplication
import io.driverdoc.testapp.common.MVVMApplication.Companion.listText
import io.driverdoc.testapp.common.MVVMApplication.Companion.listTextCheck
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveData
import io.driverdoc.testapp.data.model.document.DeleteDocumentRespone
import io.driverdoc.testapp.data.remote.ApiHelp
import io.driverdoc.testapp.data.remote.RestApi
import io.driverdoc.testapp.ui.detectdocument.util.RuntimePermissions
import io.driverdoc.testapp.ui.utils.LoadDataBinding
import io.driverdoc.testapp.ui.utils.StringUtils
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UpdateDocument(acti: AppCompatActivity) {
    companion object {
        private var listUrlSchedule: ArrayList<String>? = null
        private var restApi: RestApi
        val obDocument = MutableLiveData<DeleteDocumentRespone>()
            get
        protected lateinit var loading: IOSDialog

        init {
            restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME).create(RestApi::class.java)


        }

        fun getListUrl(activity: AppCompatActivity): ArrayList<String> {

            RuntimePermissions.instance().runWithPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    R.string.permission_query_write_storage,
                    RuntimePermissions.Callback { permission, granted ->
                        if (granted) {
                            // Common routine to start camera
                            val dir = File(activity.getExternalCacheDir(), SharedPfPermissionUtils.getIDTrip(activity).toString())
                            if (dir != null && dir.isDirectory()) {
                                val children = dir.list()
                                listUrlSchedule = ArrayList<String>()
                                for (i in 0..children.size - 1) {
                                    if (children.get(i).substring(children.get(i).lastIndexOf(".")).equals(".jpg")) {
                                        listUrlSchedule!!.add(Uri.parse(dir.getAbsolutePath() + "/" + children[i]).toString())
                                    }
                                }

                            }
                        }
                    })


            return listUrlSchedule!!

        }


        fun upDocument(document_type: String, activity: DocumentActivity, id_trip: String, filePaths: ArrayList<String>, auth: String): Disposable? {
            loading = IOSDialog.Builder(activity)
                    .setCancelable(false)
                    .build()

            val headerPare = Pair(io.driverdoc.testapp.common.Constants.AUTHORIZATION, Constants.BEARER + auth)
            val header = mutableListOf<Pair<String, String>>()
            header.add(headerPare)
            val builder = MultipartBody.Builder();
            builder.setType(MultipartBody.FORM)
            Collections.sort(filePaths)

            for (i in 0 until filePaths.sortedWith(compareBy({ it })).size) {
                val file = File(filePaths[i])
                builder.addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
            }

            builder.addFormDataPart("idTrip", id_trip)
            if (listText.size > 0) {
                val listText: MutableList<String> = mutableListOf()
                try {
                    for (i in 0 until listTextCheck.size) {
                        if (listTextCheck[i]) {
                            listText.add(listText[i])
                        }
                    }
                } catch (e: IndexOutOfBoundsException) {

                }

                if (listText.size > 0) {
                    for (i in 0 until listText.size) {
                        builder.addFormDataPart("content", listText[i])
                        if (i == 0) {
                            if (listText[i].toUpperCase(Locale.ROOT).contains("BILL OF LADING")) {
                                builder.addFormDataPart("documentType", "Bill of Lading")
                            } else if (listText[i].toLowerCase(Locale.ROOT).contains("delivery")) {
                                builder.addFormDataPart("documentType", "Proof of Delivery")
                            } else {
                                builder.addFormDataPart("documentType", "Trip Document")
                            }
                        }
                    }
                } else {
                    builder.addFormDataPart("content", "")
                    builder.addFormDataPart("documentType", "Trip Document")
                }

            } else {
                builder.addFormDataPart("content", "")
                builder.addFormDataPart("documentType", "Trip Document")
            }
            val requestBody = builder.build()

            restApi = ApiHelp.createRetrofit(endpoint = Constants.BASE_URL, formatDate = Constants.LIST_FORMAT_TIME, headers = header)
                    .create(RestApi::class.java)
            return subscribeHasResultDispose(
                    restApi.updateDocument(requestBody)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()))
            {
                obDocument.value = it
                activity.onBackPressed()
                liveData.postValue(2)
                loading.dismiss()
            }
        }


        protected inline fun <reified T> subscribeHasResultDispose(observable: Observable<T>?, crossinline onNext: (T) -> Unit): Disposable? {
            if (observable == null) {
                return null
            }
            loading.show()
            val className = T::class.java.name
            return observable.subscribe(
                    {
                        onNext(it)
                    },
                    {
                        run {
                            if (Constants.DEBUG) {
                                it.printStackTrace()
                            }

                        }
                    })
        }
    }

}