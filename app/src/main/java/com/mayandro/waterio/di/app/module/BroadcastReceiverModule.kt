package com.mayandro.waterio.di.app.module

import com.mayandro.waterio.broadcastreceiver.AlarmReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadcastReceiverModule {
    @ContributesAndroidInjector
    abstract fun contributesAlarmReceiver() : AlarmReceiver

}