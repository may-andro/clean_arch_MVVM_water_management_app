package com.mayandro.waterio.ui.home.fragment.history

import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.ui.base.ViewInteractor

interface HistoryFragmentViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)

    fun setUpPager(list: List<UserHistory>)
}