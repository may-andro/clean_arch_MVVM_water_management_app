package com.mayandro.waterio.ui.goals

import com.google.firebase.auth.PhoneAuthCredential
import com.mayandro.waterio.ui.base.ViewInteractor

interface GoalActivityViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)

    fun goToHomeActivity()
}