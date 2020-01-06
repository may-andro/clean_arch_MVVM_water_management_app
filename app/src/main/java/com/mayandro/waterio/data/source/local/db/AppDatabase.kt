package com.mayandro.waterio.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mayandro.waterio.data.model.DataConverter
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.source.local.db.dao.UserDao
import com.mayandro.waterio.data.source.local.db.dao.UserHistoryDao

@Database(entities = [User::class, UserHistory::class], version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase: RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun userHistoryDao(): UserHistoryDao
}