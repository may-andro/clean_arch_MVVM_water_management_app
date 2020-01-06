package com.mayandro.waterio.data.source.local.db.dao

import androidx.room.*
import com.mayandro.waterio.data.model.User
import io.reactivex.Single


@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Single<Long>

    @Update
    fun updateUser(user: User): Single<Int>

    @Query("SELECT * FROM user_table ")
    fun getAllUser(): Single<List<User>>

    @Query("SELECT * FROM user_table WHERE user_phone = :userPhone")
    fun getUserByPhone(userPhone: String): Single<User>

    @Query("DELETE FROM user_table WHERE user_phone = :userPhone")
    fun deleteUserByPhone(userPhone: String): Single<Int>
}