package com.mayandro.waterio.di.app.module

import android.app.Application
import androidx.room.Room
import com.mayandro.waterio.data.source.local.db.AppDatabase
import com.mayandro.waterio.data.source.local.db.dao.UserDao
import com.mayandro.waterio.data.source.local.db.dao.UserHistoryDao
import com.mayandro.waterio.di.app.scope.AppScoped
import com.mayandro.waterio.utils.AppConstants.ROOM_DB_STRING
import dagger.Module
import dagger.Provides

@Module
class RoomDbModule {
    @AppScoped
    @Provides
    fun provideDb(application: Application): AppDatabase {
        return Room.databaseBuilder<AppDatabase>(
            application,
            AppDatabase::class.java, ROOM_DB_STRING
        )
            .build()
    }

    @AppScoped
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @AppScoped
    @Provides
    fun provideUserHistoryDao(db: AppDatabase): UserHistoryDao {
        return db.userHistoryDao()
    }
}