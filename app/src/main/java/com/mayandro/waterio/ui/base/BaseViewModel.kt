package com.mayandro.waterio.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel<VI: ViewInteractor>: ViewModel() {

    val compositeDisposable: CompositeDisposable? = null

    var viewInteractor: VI? = null
        set

    override fun onCleared() {
        compositeDisposable?.dispose()
        super.onCleared()
    }
}