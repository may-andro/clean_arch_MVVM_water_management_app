package com.mayandro.waterio.di.app.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayandro.waterio.di.app.scope.AppScoped
import com.mayandro.waterio.di.app.utils.ViewModelFactory
import com.mayandro.waterio.di.app.utils.ViewModelKey
import com.mayandro.waterio.ui.auth.AuthActivityViewModel
import com.mayandro.waterio.ui.goals.GoalActivityViewModel
import com.mayandro.waterio.ui.home.HomeActivityViewModel
import com.mayandro.waterio.ui.home.fragment.dashboard.DashboardFragmentViewModel
import com.mayandro.waterio.ui.home.fragment.history.HistoryFragmentViewModel
import com.mayandro.waterio.ui.home.fragment.setting.SettingFragmentViewModel
import com.mayandro.waterio.ui.onboarding.OnBoardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @AppScoped
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun bindOnBoardingViewModel(onBoardingViewModel: OnBoardingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthActivityViewModel::class)
    abstract fun bindAuthActivityViewModel(authActivityViewModel: AuthActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoalActivityViewModel::class)
    abstract fun bindGoalActivityViewModel(goalActivityViewModel: GoalActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    abstract fun bindHomeActivityViewModel(homeActivityViewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DashboardFragmentViewModel::class)
    abstract fun bindDashboardFragmentViewModel(dashboardFragmentViewModel: DashboardFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingFragmentViewModel::class)
    abstract fun bindSettingFragmentViewModel(settingFragmentViewModel: SettingFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryFragmentViewModel::class)
    abstract fun bindHistoryFragmentViewModel(historyFragmentViewModel: HistoryFragmentViewModel): ViewModel
}
