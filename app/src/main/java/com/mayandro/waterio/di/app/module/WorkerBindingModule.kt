package com.mayandro.waterio.di.app.module

import com.mayandro.waterio.di.app.utils.ChildWorkerFactory
import com.mayandro.waterio.di.app.utils.WorkerKey
import com.mayandro.waterio.workmanager.NotifyWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerBindingModule {
    @Binds
    @IntoMap
    @WorkerKey(NotifyWorker::class)
    fun bindNotifyWorker(factory: NotifyWorker.Factory): ChildWorkerFactory
}
