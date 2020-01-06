package com.mayandro.waterio.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.ActivityHomeBinding
import com.mayandro.waterio.ui.base.BaseActivity
import com.mayandro.waterio.workmanager.NotifyWorker
import java.util.*
import java.util.concurrent.TimeUnit


class HomeActivity: BaseActivity<ActivityHomeBinding, HomeActivityViewModel>(), HomeActivityViewInteractor {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun getViewModelClass(): Class<HomeActivityViewModel> = HomeActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.activity_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewInteractor = this
        viewModel.getUserAndAllHistory()

        val navController = Navigation.findNavController(this, R.id.container)
        binding.bottomNavigation.setupWithNavController(navController)

        viewModel.setNextAlarm()
        //setUpWorkManager()
    }

    private fun setUpWorkManager() {
        //Generate notification string tag
        val tag: String = UUID.randomUUID().toString()
        val random = (Math.random() * 50 + 1).toInt()
        //Data
        val data: Data = createWorkInputData(NotifyWorker.TITLE, NotifyWorker.TEXT, random)
        scheduleReminder(data, tag)
    }

    private fun scheduleReminder(data: Data, tag: String?) {
        val hourOfTheDay = 5 // When to run the job
        val repeatInterval: Long = 1 // In days
        val flexTime: Long = calculateFlex(hourOfTheDay, repeatInterval.toInt())

        val hms = String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(flexTime),
            TimeUnit.MILLISECONDS.toMinutes(flexTime) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(flexTime)
            ),
            TimeUnit.MILLISECONDS.toSeconds(flexTime) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(flexTime)
            )
        )

        println("data = [${data}], tag = [${tag}], hms = $hms")

        val notificationWork =
            PeriodicWorkRequest.Builder(NotifyWorker::class.java, repeatInterval, TimeUnit.DAYS, flexTime, TimeUnit.MILLISECONDS)
                .addTag(tag!!)
                .setInputData(data)
                .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(tag,ExistingPeriodicWorkPolicy.REPLACE,notificationWork)
    }

    private fun calculateFlex(
        hourOfTheDay: Int,
        periodInDays: Int
    ): Long { // Initialize the calendar with today and the preferred time to run the job.
        val requiredCalender = Calendar.getInstance()
        requiredCalender[Calendar.HOUR_OF_DAY] = hourOfTheDay
        requiredCalender[Calendar.MINUTE] = 0
        requiredCalender[Calendar.SECOND] = 0
        // Initialize a calendar with now.
        val currentCalender = Calendar.getInstance()

        if (currentCalender.timeInMillis < requiredCalender.timeInMillis) { // Add the worker periodicity.
            currentCalender.timeInMillis = currentCalender.timeInMillis + TimeUnit.DAYS.toMillis(periodInDays.toLong())
        }
        val delta = currentCalender.timeInMillis - requiredCalender.timeInMillis
        return if (delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) delta else PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS
    }

    fun cancelReminder(tag: String?) {
        WorkManager.getInstance(this).cancelAllWorkByTag(tag!!)
    }

    private fun createWorkInputData(
        title: String,
        text: String,
        id: Int
    ): Data {
        return Data.Builder()
            .putString(NotifyWorker.EXTRA_TITLE, title)
            .putString(NotifyWorker.EXTRA_TEXT, text)
            .putInt(NotifyWorker.EXTRA_ID, id)
            .build()
    }



    override fun showSnackBarMessage(message: String) {
        super.showSnackBar(message)
    }
}