package com.mayandro.waterio.di.app.module

import com.mayandro.waterio.di.app.scope.ActivityScoped
import com.mayandro.waterio.di.app.scope.FragmentScoped
import com.mayandro.waterio.di.auth.AuthActivityModule
import com.mayandro.waterio.di.auth.FragmentBindingModule
import com.mayandro.waterio.di.goal.GoalActivityModule
import com.mayandro.waterio.di.home.HomeActivityModule
import com.mayandro.waterio.di.home.HomeFragmentBindingModule
import com.mayandro.waterio.ui.auth.AuthActivity
import com.mayandro.waterio.ui.auth.fragments.OtpVerificationFragment
import com.mayandro.waterio.ui.auth.fragments.PhoneVerificationFragment
import com.mayandro.waterio.ui.goals.GoalsActivity
import com.mayandro.waterio.ui.home.HomeActivity
import com.mayandro.waterio.ui.onboarding.OnBoardingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun bindOnBoardingActivity(): OnBoardingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [AuthActivityModule::class, FragmentBindingModule::class])
    internal abstract fun bindAuthActivity(): AuthActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [GoalActivityModule::class])
    internal abstract fun bindGoalsActivity(): GoalsActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [HomeActivityModule::class, HomeFragmentBindingModule::class])
    internal abstract fun bindHomeActivity(): HomeActivity
}