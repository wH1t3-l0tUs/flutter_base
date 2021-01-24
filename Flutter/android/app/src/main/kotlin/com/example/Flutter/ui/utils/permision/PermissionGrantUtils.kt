package io.driverdoc.testapp.ui.utils.permision

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import android.text.TextUtils
import io.driverdoc.testapp.R
import java.util.ArrayList

object PermissionGrantUtils {
    @JvmStatic
    fun checkPermissionReadWriteExternalStore(fragment: Fragment, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        if (ActivityCompat.checkSelfPermission(fragment.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.context!!, Manifest.permission.READ_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(fragment, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.activity!!, Manifest.permission.READ_EXTERNAL_STORAGE, 0)
        }

        if (ActivityCompat.checkSelfPermission(fragment.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(fragment, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0)
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        fragment.requestPermissions(arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(activity: Activity, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE, 0)
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) > 0) {
                    showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                    return false
                }
            }
            pers.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            SharedPfPermissionUtils.saveNumberDeniedPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0)
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        ActivityCompat.requestPermissions(activity, arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(activity: Activity, perCheck: Array<String>, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        for (per in perCheck) {
            if (ActivityCompat.checkSelfPermission(activity, per) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, per)) {
                    if (SharedPfPermissionUtils.getNumberDeniedPermission(activity, per) > 0) {
                        showDialogConfirmOpenSetting(activity, requestCode, backCancelancelIf)
                        return false
                    }
                }
                pers.add(per)
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, per, 0)
            }
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        ActivityCompat.requestPermissions(activity, arrs, requestCode)
        return false
    }

    @JvmStatic
    fun checkPermissionReadWriteExternalStore(fragment: Fragment, perCheck: Array<String>, requestCode: Int, backCancelancelIf: Boolean): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val pers = ArrayList<String>()
        for (per in perCheck) {
            if (ActivityCompat.checkSelfPermission(fragment.activity!!, per) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(fragment.activity!!, per)) {
                    if (SharedPfPermissionUtils.getNumberDeniedPermission(fragment.activity!!, per) > 0) {
                        showDialogConfirmOpenSetting(fragment.activity!!, requestCode, backCancelancelIf)
                        return false
                    }
                }
                pers.add(per)
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(fragment.activity!!, per, 0)
            }
        }

        if (pers.size == 0) {
            return true
        }
        val arrs = arrayOfNulls<String>(pers.size)
        var index = 0
        for (per in pers) {
            arrs[index] = per
            index++
        }
        fragment.requestPermissions(arrs, requestCode)
        return false
    }


    @JvmStatic
    private fun showDialogConfirmOpenSetting(fragment: Fragment, requestCode: Int, backCancelancelIf: Boolean) {
        val dialog = ConfirmDialog(fragment.activity!!, R.string.You_need_open_setting_to_grant_permission, object : ConfirmDialog.IConfirmDialog {
            override fun onClickCancel() {
                if (backCancelancelIf) {
                    fragment.activity!!.onBackPressed()
                }
            }

            override fun onClickOk() {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + fragment.activity!!.packageName)
                fragment.startActivityForResult(intent, requestCode)
            }
        })
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    @JvmStatic
    private fun showDialogConfirmOpenSetting(activity: Activity, requestCode: Int, backCancelancelIf: Boolean) {
        val dialog = ConfirmDialog(activity, R.string.You_need_open_setting_to_grant_permission, object : ConfirmDialog.IConfirmDialog {
            override fun onClickCancel() {
                if (backCancelancelIf) {
                    activity.onBackPressed()
                }
            }

            override fun onClickOk() {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.data = Uri.parse("package:" + activity.packageName)
                activity.startActivityForResult(intent, requestCode)
            }
        })
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

    }

    @JvmStatic
    fun checkPermissionFinish(activity: Activity, pernissions: Array<out String>, granted: IntArray): Boolean {
        var result = true
        for (i in granted.indices) {
            if (granted[i] == PackageManager.PERMISSION_DENIED) {
                result = false
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, pernissions[i])) {
                    SharedPfPermissionUtils.increaseNumberDeniedPermission(activity, pernissions[i], 1)
                }
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, pernissions[i], 0)
            }
        }

        return result
    }

    @JvmStatic
    fun checkPermissionOnResult(activity: Activity, permissions: Array<out String>): Boolean {
        var isSuccess = true
        for (i in permissions.indices) {
            if (ActivityCompat.checkSelfPermission(activity, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                isSuccess = false
            } else {
                SharedPfPermissionUtils.saveNumberDeniedPermission(activity, permissions[i], 0)
            }
        }
        return isSuccess
    }
    @JvmStatic
    fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)

            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF

        } else {
            locationProviders = Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)
            return !TextUtils.isEmpty(locationProviders)
        }


    }
}