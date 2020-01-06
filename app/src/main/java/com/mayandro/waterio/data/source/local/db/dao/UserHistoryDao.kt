package com.mayandro.waterio.data.source.local.db.dao

import androidx.room.*
import com.mayandro.waterio.data.model.UserHistory
import io.reactivex.Single

@Dao
interface UserHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserHistory(userHistory: UserHistory): Single<Long>

    @Update
    fun updateUserHistory(userHistory: UserHistory): Single<Int>

    @Query("SELECT * FROM user_history_table ")
    fun getAllUserHistory(): Single<List<UserHistory>>

    @Query("SELECT * FROM user_history_table WHERE history_date = :date")
    fun getUserHistoryByDate(date: String): Single<UserHistory>

    @Query("SELECT * FROM user_history_table limit :duration")
    fun getAllHistoryForDuration(duration: Int): Single<List<UserHistory>>

}