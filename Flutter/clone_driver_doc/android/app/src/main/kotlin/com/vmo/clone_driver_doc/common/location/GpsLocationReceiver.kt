package io.driverdoc.testapp.common.location

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context


class GpsLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action!!.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
//            Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
//                    Toast.LENGTH_SHORT).show()
        }
    }
}