package com.mayandro.waterio.ui.home.fragment.dashboard

import androidx.lifecycle.MutableLiveData
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.repo.Repository
import com.mayandro.waterio.ui.base.BaseViewModel
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class DashboardFragmentViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<DashboardFragmentViewInteractor>() {

    var currentConsumptionPercentage = MutableLiveData<Int>()

    fun addValueToCurrentConsumption(
        currentConsumptionAmount: Int,
        user: User
    ) {
        val disposable =
            repository.addWaterQuantityToHistory(currentConsumptionAmount * 0.001f, user)
                .flatMap {
                    return@flatMap repository.checkIfStreakNeedsToUpdate(user = user, userHistory = it)
                }.flatMap {
                    if (!it.first) {
                        return@flatMap Single.just(Pair(it.third, it.second))
                    }
                    return@flatMap repository.updateUser(it.second, it.third)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewInteractor?.updateUserHistory(it.second)
                    viewInteractor?.updateUser(it.first)
                }, {
                    viewInteractor?.showSnackBarMessage("Something went wrong")
                })

        compositeDisposable?.add(disposable)
    }



    fun setConsumptionPercentage(userHistory: UserHistory) {
        currentConsumptionPercentage.value =
            if (userHistory.historyQuantity > 0) (userHistory.historyQuantity / userHistory.historyGoal * 100).toInt() else 0
    }

    fun delayForASecond(second: Long) {
        val disposable =
            Observable.just(second).delay(second, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    viewInteractor?.manageCardMessageVisibility(false)
                }, {
                    viewInteractor?.manageCardMessageVisibility(false)
                })

        compositeDisposable?.add(disposable)
    }

    fun checkIfQuantityIsValid(amount: String): Boolean {
        return amount.let {
            !it.isBlank() && (it.length in 3..4)
        }
    }
}