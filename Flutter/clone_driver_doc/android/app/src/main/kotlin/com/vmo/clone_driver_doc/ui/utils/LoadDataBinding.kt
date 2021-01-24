package io.driverdoc.testapp.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.databinding.BindingAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.LatLng
import io.driverdoc.testapp.ui.main.tripsactive.MyLocationListener
import java.io.File
import android.os.Build
import android.os.Environment
import com.bumptech.glide.load.engine.Resource
import java.io.IOException
import android.R.attr.y
import android.R.attr.x
import android.util.DisplayMetrics
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.util.Log
import android.view.KeyEvent.KEYCODE_HOME
import android.view.KeyCharacterMap
import android.view.KeyEvent.KEYCODE_BACK
import android.view.KeyEvent
import io.driverdoc.testapp.R
import java.lang.NullPointerException


object LoadDataBinding {
    @BindingAdapter(value = ["bind:urlImage"], requireAll = false)
    fun AppCompatImageView.setUrlImage(urlImage: String?) {
        if (!StringUtils.isBlank(urlImage)) {
            io.driverdoc.testapp.ui.customview.GlideApp.with(this)
                    .load(urlImage)
                    .placeholder(R.drawable.ao_dai)
                    .error(R.drawable.ao_dai)
                    .into(this)
        }
    }

    fun getLocation(context: Context): LatLng {
        var latitude = 0.0
        var longitude = 0.0
        var mlocManager: LocationManager? = null;
        var mlocListener: LocationListener? = null
        mlocManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mlocListener = MyLocationListener(context)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            ) {

                mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0F, mlocListener);
                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    latitude = MyLocationListener.latitude
                    longitude = MyLocationListener.longitude
                }
            }
        }
        return LatLng(latitude, longitude)
    }

    class clearApplicationData(context: Context) : Thread() {
        private var ctx: Context? = null

        init {
            this.ctx = context
        }

        override fun run() {
            try {
                val path = Environment.getExternalStorageDirectory().toString() + "/DI-SDK"
                DeleteRecursive(path)
                deleteDirectory(ctx?.getExternalCacheDir()!!)
                val fileSink = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                DeleteRecursiveFileNotDir(fileSink)
            } catch (e: Exception) {

            }
        }
    }

    @Throws(IOException::class, InterruptedException::class)
    fun deleteDirectory(file: File): Boolean {
        if (file.exists()) {
            val deleteCommand = "rm -rf " + file.absolutePath
            val runtime = Runtime.getRuntime()
            val process = runtime.exec(deleteCommand)
            process.waitFor()
            return true
        }
        return false

    }

    fun getNameFolder(dir: File): ArrayList<String> {
        val list = ArrayList<String>()
        if (dir.isDirectory) {
            val children = dir.list()
            if (null != children && children.size > 0) {
                children.forEach {
                    list.add(it)
                }
            }
        }
        return list
    }

    fun deteteItem(dir: File) {
        Thread({
            try {
                dir.delete()
            }catch (e:NullPointerException){

            }
        }).start()
    }

    fun DeleteRecursive(strPath: String) {
        val fileOrDirectory = File(strPath)

        if (fileOrDirectory != null && fileOrDirectory.isDirectory) {
            fileOrDirectory.listFiles()?.let {
                for (child in it) {
                    DeleteRecursive(child.path)
                }
            }
            fileOrDirectory.delete()
        } else {
            fileOrDirectory.delete()
        }
    }

    fun DeleteRecursiveFileNotDir(strPath: String) {
        val fileOrDirectory = File(strPath)
        if (fileOrDirectory.isDirectory) {
            fileOrDirectory.listFiles()?.let {
                for (child in it)
                    if (!child.isDirectory) {
                        DeleteRecursive(child.path)
                    }
            }

        }
    }

    fun getHeightNavBar(context: Context): Int {
        val resources = context.getResources()
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    fun hasNavBar(): Boolean {
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)

        return !(hasBackKey && hasHomeKey)
    }

    fun openCHPlay(context: Context) {
        val appPackageName = context!!.getPackageName() // getPackageName() from Context or Activity object
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }

    fun getVersionOS(): String {
        return java.lang.Double.parseDouble(java.lang.String(Build.VERSION.RELEASE).replaceAll("(\\d+[.]\\d+)(.*)", "$1")).toString()
    }
}