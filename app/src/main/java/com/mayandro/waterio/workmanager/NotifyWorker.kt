package com.mayandro.waterio.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mayandro.waterio.R
import androidx.work.*
import com.mayandro.waterio.di.app.utils.ChildWorkerFactory
import com.mayandro.waterio.ui.home.HomeActivity
import com.mayandro.waterio.utils.SharedPreferenceManager
import com.mayandro.waterio.utils.notification.NotificationHelper
import javax.inject.Inject

// Refer for DI: https://medium.com/@neonankiti/how-to-use-dagger2-withworkmanager-bae3a5fb7dd3

class NotifyWorker(
    val context: Context,
    workerParams: WorkerParameters,
    val preferenceManager: SharedPreferenceManager,
    val notificationHelper: NotificationHelper
) :
    Worker(context, workerParams) {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_TEXT = "text"
        const val EXTRA_ID = "id"

        const val TITLE = "Hydration Time"
        const val TEXT = "Don't forget to drink some water, and add your consumption."
    }

    override fun doWork(): Result {
        val title = inputData.getString(EXTRA_TITLE)
        val text = inputData.getString(EXTRA_TEXT)
        notificationHelper.createNotificationChannel(
            context.getString(R.string.app_name), "App notification channel."
        )
        notificationHelper.createReminderNotification(
            title!!, text!!
        )
        return Result.success()
    }

    class Factory @Inject constructor(
        val preferenceManager: SharedPreferenceManager,
        val notificationHelper: NotificationHelper
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return NotifyWorker(
                appContext,
                params,
                preferenceManager,
                notificationHelper
            )
        }
    }
}