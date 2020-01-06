package com.mayandro.waterio.data.source.local.db

import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.source.DataSource
import com.mayandro.waterio.data.source.local.db.dao.UserDao
import com.mayandro.waterio.data.source.local.db.dao.UserHistoryDao
import com.mayandro.waterio.di.app.scope.AppScoped
import io.reactivex.Single
import javax.inject.Inject

@AppScoped
class LocalDataSource @Inject constructor(private val userDao: UserDao, private val userHistoryDao: UserHistoryDao): DataSource {

    override fun insertUserToDatabase(user: User): Single<Long> {
        return userDao.insertUser(user)
    }

    override fun updateUser(user: User): Single<Int> {
        return userDao.updateUser(user)
    }

    override fun getUserWithPhone(phone: String): Single<User> {
        return userDao.getUserByPhone(phone)
    }

    override fun getAllHistory(): Single<List<UserHistory>> {
        return userHistoryDao.getAllUserHistory()
    }

    override fun getHistoryOnSpecifiedDate(date: String): Single<UserHistory> {
        return userHistoryDao.getUserHistoryByDate(date)
    }

    override fun updateHistory(history: UserHistory): Single<Int> {
        return userHistoryDao.updateUserHistory(history)
    }

    override fun insertUserHistoryToDatabase(history: UserHistory): Single<Long> {
        return userHistoryDao.insertUserHistory(history)
    }

    override fun getAllHistoryForDuration(duration: Int): Single<List<UserHistory>> {
        return userHistoryDao.getAllHistoryForDuration(duration)
    }
}