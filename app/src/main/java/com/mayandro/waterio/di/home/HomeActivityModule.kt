package com.mayandro.waterio.di.home

import android.app.Application
import android.content.Context
import com.mayandro.waterio.di.app.scope.ActivityScoped
import com.mayandro.waterio.ui.goals.GoalsActivity
import dagger.Module
import dagger.Provides
import dagger.android.support.DaggerAppCompatActivity
import com.mayandro.waterio.utils.ResourceProvider
import com.mayandro.waterio.utils.BaseResourceProvider


@Module
class HomeActivityModule {

    @Provides
    @ActivityScoped
    fun provideActivityContext(activity: GoalsActivity): Context {
        return activity
    }

    @Provides
    @ActivityScoped
    fun provideActivity(homeActivity: GoalsActivity): DaggerAppCompatActivity {
        return homeActivity
    }

    @Provides
    @ActivityScoped
    fun provideResourceProvider(context: Application): BaseResourceProvider {
        return ResourceProvider(context)
    }
}