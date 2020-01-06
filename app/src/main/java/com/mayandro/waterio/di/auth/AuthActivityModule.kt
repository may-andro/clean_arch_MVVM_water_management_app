package com.mayandro.waterio.di.auth

import com.mayandro.waterio.di.app.scope.ActivityScoped
import com.mayandro.waterio.ui.auth.AuthActivity
import com.mayandro.waterio.ui.auth.adapter.AuthPagerAdapter
import com.mayandro.waterio.utils.BaseResourceProvider
import com.mayandro.waterio.utils.FireBaseAuthProvider
import com.mayandro.waterio.utils.ResourceProvider
import dagger.Module
import dagger.Provides


@Module
class AuthActivityModule {
    @Provides
    @ActivityScoped
    fun provideResourceProvider(context: AuthActivity): BaseResourceProvider {
        return ResourceProvider(context)
    }

    @Provides
    @ActivityScoped
    fun provideAuthPagerAdapter(activity: AuthActivity): AuthPagerAdapter {
        return AuthPagerAdapter(activity)
    }
}