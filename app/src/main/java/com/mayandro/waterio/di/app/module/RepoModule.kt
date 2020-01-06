package com.mayandro.waterio.di.app.module

import com.mayandro.waterio.data.source.DataSource
import com.mayandro.waterio.data.source.local.db.LocalDataSource
import com.mayandro.waterio.data.source.local.db.dao.UserDao
import com.mayandro.waterio.data.source.local.db.dao.UserHistoryDao
import com.mayandro.waterio.di.app.qualifiers.Local
import com.mayandro.waterio.di.app.scope.AppScoped
import dagger.Module
import dagger.Provides


@Module
class RepoModule {

    @Provides
    @Local
    @AppScoped
    fun provideLocalDataSource(userDao: UserDao, userHistoryDao: UserHistoryDao): DataSource {
        return LocalDataSource(userDao, userHistoryDao)
    }
}