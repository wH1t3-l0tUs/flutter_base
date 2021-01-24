package io.driverdoc.testapp.ui.firebasefcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.driverdoc.testapp.R
import io.driverdoc.testapp.ui.splash.SplashActivity
import android.os.Build
import androidx.annotation.RequiresApi
import io.driverdoc.testapp.common.MVVMApplication.Companion.liveDataNotifi
import org.json.JSONObject
import java.util.concurrent.Executors
import kotlin.random.Random
import android.os.Bundle
import android.util.Log
import io.driverdoc.testapp.data.model.DataNotifi
import io.driverdoc.testapp.ui.utils.permision.SharedPfPermissionUtils


class FCMService : FirebaseMessagingService() {
    private val TAG = "MyFirebaseToken"
    private lateinit var notificationManager: NotificationManager
    private val ADMIN_CHANNEL_ID = "driverDOC"
    private var data = ""
    private var tripNumber = ""
    private var dataNoti: DataNotifi? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val intent = Intent(this, SplashActivity::class.java)
        val json = JSONObject(remoteMessage.data as Map<*, *>)
        val itr = json.keys()

        while (itr.hasNext()) {
            val key = itr.next() as String
            val service = Executors.newSingleThreadExecutor()
            service.submit(Runnable {
                run {
                    key.let {
                        if (it.equals("data")) {
                            data = json.getString("data")
                        }
                        if (it.equals("tripNumber")) {
                            tripNumber = json.getString("tripNumber")
                        }
                    }

                    dataNoti = DataNotifi()
                    dataNoti?.data = data
                    dataNoti?.tripNumber = tripNumber

                    liveDataNotifi.postValue(dataNoti)
                    Log.d("FCMService", json.getString(key))
                    Log.d("FCMService...", key)
                }
            })
            val bundle = Bundle()
            bundle.putString("NotificationMessage", json.getString(key));
            intent.putExtras(bundle)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Setting up Notification channels for android O and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupNotificationChannels()
        }
        val notificationId = Random.nextInt(60000)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_PROMO)
                .setSmallIcon(R.drawable.ic_app2)  //a resource for your custom small icon
                .setContentTitle(resources.getString(R.string.app_name)) //the "title" value you sent in your notification
                .setContentText(remoteMessage.notification?.body) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setPriority(Notification.VISIBILITY_PUBLIC)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build())


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels() {
        val adminChannelName = getString(R.string.app_name)
        val adminChannelDescription = getString(R.string.app_name)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }

    /*Add notification method use for add icon and title*/

}