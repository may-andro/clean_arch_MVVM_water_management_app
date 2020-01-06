package com.mayandro.waterio.ui.home

import androidx.lifecycle.MutableLiveData
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.repo.Repository
import com.mayandro.waterio.ui.base.BaseViewModel
import com.mayandro.waterio.utils.SharedPreferenceManager
import com.mayandro.waterio.utils.alarm.AlarmScheduler
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class HomeActivityViewModel @Inject constructor(
    private val repository: Repository,
    private val preferenceManager: SharedPreferenceManager,
    private val alarmScheduler: AlarmScheduler
) :
    BaseViewModel<HomeActivityViewInteractor>() {

    var currentUser = MutableLiveData<User>()
    var currentUserHistory = MutableLiveData<UserHistory>()

    fun getUserAndAllHistory() {
        val disposable = repository.getCurrentUser()
            .flatMap { user ->
                repository.getUserHistoryAndInsertIfNotAvailable(user).flatMap { userHistory ->
                    Single.just(Pair(user, userHistory))
                }
            }
            .flatMap {
                repository.checkIfStreakNeedsToUpdate(it.second, it.first)
            }
            .flatMap {
                if (!it.first) {
                    return@flatMap Single.just(Pair(it.third, it.second))
                }
                return@flatMap repository.updateUser(it.second, it.third)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ pair ->
                currentUserHistory.value = pair.second
                currentUser.value = pair.first
            }, { error ->
                viewInteractor?.showSnackBarMessage("No history found")
            })

        compositeDisposable?.add(disposable)
    }

    fun setNextAlarm() {
        alarmScheduler.setNextAlarm()
    }
}