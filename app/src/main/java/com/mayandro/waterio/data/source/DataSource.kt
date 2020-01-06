package com.mayandro.waterio.data.source

import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import io.reactivex.Single

interface DataSource {

    fun insertUserToDatabase(user: User): Single<Long>

    fun updateUser(user: User): Single<Int>

    fun getUserWithPhone(phone: String): Single<User>

    fun getAllHistory(): Single<List<UserHistory>>

    fun getAllHistoryForDuration(duration: Int): Single<List<UserHistory>>

    fun getHistoryOnSpecifiedDate(date: String): Single<UserHistory>

    fun updateHistory(history: UserHistory): Single<Int>

    fun insertUserHistoryToDatabase(history: UserHistory): Single<Long>
}