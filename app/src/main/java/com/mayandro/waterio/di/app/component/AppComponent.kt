package com.mayandro.waterio.di.app.component

import android.app.Application
import com.mayandro.waterio.BaseApplication
import com.mayandro.waterio.di.app.module.*
import com.mayandro.waterio.di.app.scope.AppScoped
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@AppScoped
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        AppModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class,
        FirebaseModule::class,
        PersistanceModule::class,
        RoomDbModule::class,
        WorkerBindingModule::class,
        BroadcastReceiverModule::class,
        RepoModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}