package com.mayandro.waterio.ui.goals

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.repo.Repository
import com.mayandro.waterio.ui.base.BaseViewModel
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.*
import javax.inject.Inject


class GoalActivityViewModel @Inject constructor(val repository: Repository, val firebaseAuth: FirebaseAuth): BaseViewModel<GoalActivityViewInteractor>() {

    var selectedGoal = MutableLiveData<Float>()

    fun checkIfUserExists() {
        val disposable = repository.getCurrentUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ user ->
                viewInteractor?.goToHomeActivity()
            }, { error ->
                // First time
            })

        compositeDisposable?.add(disposable)
    }

    fun createUser() {
        val user = User(
            phone = repository.getUserPhone()!!,
            createdAt = DateAndTimeUtils.getCurrentDate(),
            dailyGoal = selectedGoal.value?.toFloat()?: 2f,
            id = repository.getUserPhone()!!,
            currentStreak = 0,
            longestStreak = 0,
            lastGaolReached = ""
        )
        val disposable = repository.insertUserToDatabase(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                viewInteractor?.goToHomeActivity()
            }, { error ->
                viewInteractor?.showSnackBarMessage("Something went wrong")
            })

        compositeDisposable?.add(disposable)

    }
}