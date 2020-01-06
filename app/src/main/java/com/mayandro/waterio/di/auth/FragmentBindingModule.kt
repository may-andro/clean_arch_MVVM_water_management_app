package com.mayandro.waterio.di.auth

import com.mayandro.waterio.di.app.scope.ActivityScoped
import com.mayandro.waterio.di.app.scope.FragmentScoped
import com.mayandro.waterio.di.auth.AuthActivityModule
import com.mayandro.waterio.di.goal.GoalActivityModule
import com.mayandro.waterio.ui.auth.AuthActivity
import com.mayandro.waterio.ui.auth.fragments.OtpVerificationFragment
import com.mayandro.waterio.ui.auth.fragments.PhoneVerificationFragment
import com.mayandro.waterio.ui.goals.GoalsActivity
import com.mayandro.waterio.ui.onboarding.OnBoardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun bindOtpVerificationFragment(): OtpVerificationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun bindPhoneVerificationFragment(): PhoneVerificationFragment

}