package com.mayandro.waterio.di.home

import com.mayandro.waterio.di.app.scope.FragmentScoped
import com.mayandro.waterio.ui.home.fragment.dashboard.DashboardFragment
import com.mayandro.waterio.ui.home.fragment.history.HistoryFragment
import com.mayandro.waterio.ui.home.fragment.setting.SettingFragment
import com.mayandro.waterio.ui.home.fragment.setting.dialog.DurationFrequencyBottomDialogFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeFragmentBindingModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun bindDashboardFragment(): DashboardFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun bindHistoryFragment(): HistoryFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun bindSettingFragment(): SettingFragment
}