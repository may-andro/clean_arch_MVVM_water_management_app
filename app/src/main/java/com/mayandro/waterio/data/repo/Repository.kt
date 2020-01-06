package com.mayandro.waterio.data.repo

import androidx.room.EmptyResultSetException
import com.google.firebase.auth.FirebaseAuth
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.model.UserLog
import com.mayandro.waterio.data.source.DataSource
import com.mayandro.waterio.di.app.qualifiers.Local
import com.mayandro.waterio.di.app.scope.AppScoped
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AppScoped
class Repository @Inject constructor(@Local val localDataSource: DataSource, val firebaseAuth: FirebaseAuth) {
    fun insertUserToDatabase(user: User): Single<Long> {
        return localDataSource.insertUserToDatabase(user)
    }

    fun getUserPhone(): String? {
        return firebaseAuth.currentUser?.phoneNumber
    }

    fun getCurrentUser(): Single<User> {
        getUserPhone()?.let {
            return localDataSource.getUserWithPhone(it)
        }
        return localDataSource.getUserWithPhone("")
    }

    fun updateUser(user: User): Single<User> {
        return localDataSource.updateUser(user)
            .flatMap {
                Single.just(user)
            }
    }

    fun getUserHistoryAndInsertIfNotAvailable(user: User): Single<UserHistory> {
        val currentDate = DateAndTimeUtils.getCurrentDate()
        return doesHistoryDateEntryExists(currentDate)
            .flatMap {
                if (it.first)
                    return@flatMap Single.just(it.second)
                else
                    return@flatMap createHistoryForTheDate(currentDate, user)
                        .flatMap { history ->
                            insertHistoryForDate(history)
                        }
            }
    }

    private fun doesHistoryDateEntryExists(date: String): Single<Pair<Boolean, UserHistory>> {
        return getHistoryForDate(date = date)
            .flatMap {
                return@flatMap Single.just(Pair(true, it))
            }
            .onErrorReturn {
                Pair(false, UserHistory("", 0f, 0f, "", mutableListOf()))
            }
    }

    private fun createHistoryForTheDate(date: String, user: User): Single<UserHistory> {
        val userHistory = UserHistory(
            historyDate = date,
            historyGoal = user.dailyGoal,
            historyLog = mutableListOf(),
            historyQuantity = 0f,
            historyUserPhone = user.phone
        )
        return Single.just(userHistory)
    }

    private fun insertHistoryForDate(userHistory: UserHistory): Single<UserHistory> {
        return localDataSource.insertUserHistoryToDatabase(userHistory)
            .flatMap {
                Single.just(userHistory)
            }
    }


    fun addWaterQuantityToHistory(quantity: Float, user: User): Single<UserHistory> {
        return getUserHistoryAndInsertIfNotAvailable(user)
            .flatMap {
                updateHistoryLog(it, quantity)
            }.flatMap { history ->
                localDataSource.updateHistory(history)
                    .flatMap {
                        Single.just(history)
                    }
            }
    }

    private fun getHistoryForDate(date: String): Single<UserHistory> {
        return localDataSource.getHistoryOnSpecifiedDate(date)
    }


    private fun updateHistoryLog(userHistory: UserHistory, quantity: Float): Single<UserHistory> {
        return Single.create<UserHistory> {
            val currentTime = DateAndTimeUtils.getCurrentTime()
            val userLog = UserLog(logTime = currentTime, logQuantity = quantity)
            userHistory.historyLog.add(userLog)
            userHistory.historyQuantity = userHistory.historyQuantity + quantity
            it.onSuccess(userHistory)
        }
    }


    fun getUserHistoryForDuration(duration: Int): Single<List<UserHistory>> {
        return localDataSource.getAllHistoryForDuration(duration)
    }

    fun checkIfStreakNeedsToUpdate(
        userHistory: UserHistory,
        user: User
    ): Single<Triple<Boolean, UserHistory, User>> {
        return Single.just(userHistory)
            .flatMap {
                if (it.historyQuantity < it.historyGoal || it.historyDate == user.lastGaolReached) {
                    return@flatMap Single.just(Triple(false, it, user))
                }

                if (user.lastGaolReached.isEmpty()) {
                    user.currentStreak = 1
                    user.longestStreak = 1
                    user.lastGaolReached = userHistory.historyDate
                    return@flatMap Single.just(Triple(true, it, user))
                }

                DateAndTimeUtils.getDate(user.lastGaolReached)?.let { lastGaol ->
                    val date = Calendar.getInstance().time
                    val diff: Long = date.time - lastGaol.time
                    val dayDifference = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()

                    when (dayDifference) {
                        1 -> {
                            user.currentStreak = user.currentStreak + 1
                            if (user.currentStreak > user.longestStreak) user.longestStreak =
                                user.currentStreak
                        }
                        else -> user.currentStreak = 1
                    }
                    user.lastGaolReached = userHistory.historyDate
                    return@flatMap Single.just(Triple(true, it, user))
                } ?: kotlin.run {
                    return@flatMap Single.just(Triple(false, it, user))
                }
            }
    }

    fun updateUser(userHistory: UserHistory, user: User): Single<Pair<User, UserHistory>> {
        return Single.just(user)
            .flatMap {
                updateUser(user)
            }.flatMap {
                return@flatMap Single.just(Pair(it, userHistory))
            }
    }

}