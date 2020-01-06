package com.mayandro.waterio.utils.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.mayandro.waterio.broadcastreceiver.AlarmReceiver
import com.mayandro.waterio.utils.SharedPreferenceManager
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import java.util.*
import javax.inject.Inject


class AlarmScheduler @Inject constructor(private val application: Application, private val preferenceManager: SharedPreferenceManager) {

    companion object {
        const val DAILY_REMINDER_REQUEST_CODE = 100
    }

    private val TAG = AlarmScheduler::class.java.simpleName

    private fun scheduleAlarmsForNotification(hour: Int, min: Int = 0) {
        val calendar: Calendar = Calendar.getInstance()

        val setcalendar: Calendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hour)
        setcalendar.set(Calendar.MINUTE, min)
        setcalendar.set(Calendar.SECOND, 0)
        // cancel already scheduled reminders
        cancelReminder()
        if (setcalendar.before(calendar)) setcalendar.add(Calendar.DATE, 1)
        enableReceiver()

        println("AlarmScheduler.scheduleAlarmsForNotification hour = [${hour}], min = [${min}] setcalendar. =[${setcalendar.get(Calendar.DATE)}]")


        val alarmIntent = createPendingIntent()
        val alarmManager = application.getSystemService(ALARM_SERVICE) as AlarmManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                setcalendar.timeInMillis,
                alarmIntent)
        }
        else{
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                setcalendar.timeInMillis,
                alarmIntent)
        }
    }

    private fun enableReceiver() {
        // Enable a receiver
        val receiver = ComponentName(application, AlarmReceiver::class.java)
        application.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun disableReceiver() {
        // Enable a receiver
        val receiver = ComponentName(application, AlarmReceiver::class.java)
        application.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(application, AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(application, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun cancelReminder() { // Disable a receiver
        disableReceiver()
        val alarmIntent = createPendingIntent()
        val am = application.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    private fun isAlarmSet(): Boolean {
        val intent = Intent(application, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(application, DAILY_REMINDER_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        return pendingIntent != null //True: alarm is set
    }

    fun setNextAlarm() {
        println("AlarmScheduler.setNextAlarm isAlarmSet ${isAlarmSet()}")
        if (!isAlarmSet()) {
            val startTimePair =
                DateAndTimeUtils.getHourAndMinFromTime(preferenceManager.settingStartTime!!)
            val endTimePair =
                DateAndTimeUtils.getHourAndMinFromTime(preferenceManager.settingEndTime!!)

            val currentTimePair = DateAndTimeUtils.getHourAndMinFromCurrentTime()

            println("AlarmScheduler.setNextAlarm startTimePair ${startTimePair!!.first} endTimePair ${endTimePair!!.first} currentTimePair ${currentTimePair!!.first}")
            if(startTimePair!!.first < currentTimePair!!.first && endTimePair!!.first > currentTimePair.first && (endTimePair.first - currentTimePair.first > preferenceManager.settingDurationFrequency)) {
                scheduleAlarmsForNotification(DateAndTimeUtils.getFrequencyHourFromNow(preferenceManager.settingDurationFrequency))
            } else {
                scheduleAlarmsForNotification(startTimePair.first)
            }
        }

    }
}