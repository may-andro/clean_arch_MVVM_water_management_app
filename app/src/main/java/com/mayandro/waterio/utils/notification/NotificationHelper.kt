package com.mayandro.waterio.utils.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mayandro.waterio.R
import com.mayandro.waterio.broadcastreceiver.AlarmReceiver
import com.mayandro.waterio.ui.home.HomeActivity
import java.util.*
import javax.inject.Inject

class NotificationHelper @Inject constructor(private val application: Application) {

    fun createNotificationChannel(
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${application.packageName}-waterio"
            val channel = NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = description
            channel.setShowBadge(true)
            val notificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createReminderNotification(
        title: String,
        message: String
    ) {
        val channelId = "${application.packageName}-${application.getString(R.string.app_name)}"

        val intent = Intent(application, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(application, channelId).apply {
            setSmallIcon(R.drawable.ic_glass)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(message))
            priority = NotificationCompat.PRIORITY_HIGH
            setAutoCancel(true)
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(application)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    private val ADMINISTER_REQUEST_CODE = 2019

    fun createNotificationForGroup(name: String, description: String) {
        // create a group notification
        val groupBuilder = buildGroupNotification(name, description)

        // create the notification
        val notificationBuilder = buildNotificationForPet(name, description)

        // call notify for both the group and the pet notification
        val notificationManager = NotificationManagerCompat.from(application)
        notificationManager.notify(Calendar.getInstance().get(Calendar.MINUTE), groupBuilder.build())
        notificationManager.notify(Calendar.getInstance().get(Calendar.MINUTE), notificationBuilder.build())
    }

    private fun buildGroupNotification(name: String, description: String): NotificationCompat.Builder {
        val channelId = "${application.packageName}-waterio"
        return NotificationCompat.Builder(application, channelId).apply {
            setSmallIcon(R.drawable.ic_glass)
            setContentTitle(name)
            setContentText(description)
            setStyle(NotificationCompat.BigTextStyle().bigText(description))
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(name)
        }
    }


    private fun buildNotificationForPet(name: String, description: String): NotificationCompat.Builder {
        val channelId = "${application.packageName}-waterio"

        return NotificationCompat.Builder(application, channelId).apply {
            setSmallIcon(R.drawable.ic_glass)
            setContentTitle(name)
            setAutoCancel(true)
            setLargeIcon(BitmapFactory.decodeResource(application.resources, R.drawable.ic_glass))
            setContentText(description)
            setGroup(name)

            // Launches the app to open the reminder edit screen when tapping the whole notification
            val intent = Intent(application, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(application, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }



    private fun createPendingIntentForAction(): PendingIntent? {
        val administerIntent = Intent(application, AlarmReceiver::class.java).apply {
            action = application.getString(R.string.app_name)
        }
        return PendingIntent.getBroadcast(application, ADMINISTER_REQUEST_CODE, administerIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}