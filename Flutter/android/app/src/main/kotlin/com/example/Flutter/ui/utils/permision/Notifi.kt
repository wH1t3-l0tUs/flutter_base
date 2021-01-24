package io.driverdoc.testapp.ui.utils.permision

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.main.MainActivity


class Notifi {
    companion object {
        fun sendNotiFi(ctx: Context, content: String) {
            val intent = Intent(ctx, MainActivity::class.java)
            val contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val b = NotificationCompat.Builder(ctx)

            b.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.ic_app2)
                    .setTicker("driverDOC")
                    .setContentTitle(content)
                    .setContentText("driverDOC")
                    .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent)
                    .setContentInfo("Info")


            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, b.build())
        }
    }
}