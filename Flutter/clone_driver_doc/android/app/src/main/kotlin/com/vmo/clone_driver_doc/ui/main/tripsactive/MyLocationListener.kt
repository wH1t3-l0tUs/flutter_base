package io.driverdoc.testapp.ui.main.tripsactive

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat

import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import io.driverdoc.testapp.databinding.DialogOpenGpsBinding
import android.util.Log


class MyLocationListener internal constructor(internal var ctx: Context) : LocationListener, com.google.android.gms.location.LocationListener {
    internal var location: Location? = null
    internal var locationManager: LocationManager? = null
    internal var isGPSEnabled = false
    internal var isNetworkEnabled = false
    val action = Settings.ACTION_LOCATION_SOURCE_SETTINGS

    private fun turnGPSOn() {

        val provider = android.provider.Settings.Secure.getString(
                ctx.getContentResolver(),
        android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { // if gps is disabled
            val poke =  Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            ctx.sendBroadcast(poke);
        }
    }
    init {
        try {
            locationManager = ctx.getSystemService(LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            Log.d("MyLocation....", isGPSEnabled.toString())
            if (!isGPSEnabled) {
                turnGPSOn()
                Log.d("MyLocation....", isGPSEnabled.toString())
            }

            if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(ctx,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            if (isGPSEnabled == true) {
                locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, 0f, this)
                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (isNetworkEnabled == true) {
                locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0f, this)
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            latitude = location!!.latitude
            longitude = location!!.longitude
            // Toast.makeText(ctx,"latitude: "+latitude+" longitude: "+longitude,Toast.LENGTH_LONG).show();


        } catch (ex: Exception) {

        }

    }

    override fun onLocationChanged(loc: Location) {
        loc.latitude
        loc.longitude
        latitude = loc.latitude
        longitude = loc.longitude
    }

    override fun onProviderDisabled(provider: String) {
        //print "Currently GPS is Disabled";
        turnGPSOn()
    }

    override fun onProviderEnabled(provider: String) {
        //print "GPS got Enabled";
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    companion object {

        var latitude: Double = 0.toDouble()
        var longitude: Double = 0.toDouble()
    }
}
