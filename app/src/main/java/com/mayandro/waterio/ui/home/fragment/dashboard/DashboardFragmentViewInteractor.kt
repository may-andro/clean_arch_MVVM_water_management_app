package com.mayandro.waterio.ui.home.fragment.dashboard

import com.google.firebase.auth.PhoneAuthCredential
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.ui.base.ViewInteractor

interface DashboardFragmentViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)

    fun manageCardMessageVisibility(visible: Boolean)

    fun updateUserHistory(userHistory: UserHistory)

    fun updateUser(user: User)
}