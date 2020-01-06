package com.mayandro.waterio

import androidx.work.Configuration
import androidx.work.WorkManager
import com.mayandro.waterio.di.app.component.DaggerAppComponent
import com.mayandro.waterio.di.app.utils.WorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    @Inject lateinit var factory: WorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        WorkManager.initialize(this,
            Configuration.Builder().setWorkerFactory(factory).build()
        )
    }
}