package com.mayandro.waterio.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mayandro.waterio.R
import com.mayandro.waterio.utils.alarm.AlarmScheduler
import com.mayandro.waterio.utils.notification.NotificationHelper
import dagger.android.AndroidInjection
import java.util.*
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        Log.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]")

        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            // Reschedule every alarm here
            alarmScheduler.setNextAlarm()
            return
        }

        notificationHelper.createNotificationChannel(context?.getString(R.string.app_name)!!, "App notification channel.")
        notificationHelper.createReminderNotification("Hydration Time", "Don't forget to drink some water, and add your consumption.")
        //notificationHelper.createNotificationForGroup("Hydration Time", "Don't forget to drink some water, and add your consumption.")
        alarmScheduler.cancelReminder()
        alarmScheduler.setNextAlarm()
    }
}