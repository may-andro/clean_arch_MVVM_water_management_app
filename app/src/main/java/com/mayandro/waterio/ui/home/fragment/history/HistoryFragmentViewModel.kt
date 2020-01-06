package com.mayandro.waterio.ui.home.fragment.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.data.repo.Repository
import com.mayandro.waterio.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HistoryFragmentViewModel @Inject constructor(private val repository: Repository) : BaseViewModel<HistoryFragmentViewInteractor>() {

    var currentConsumptionPercentage = MutableLiveData<Int>()

    fun getUserHistoryForDuration() {
        val disposable = repository.getUserHistoryForDuration(30)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewInteractor?.setUpPager(it)
            }, {
                viewInteractor?.showSnackBarMessage("Something went wrong")
            })


        compositeDisposable?.add(disposable)
    }
}