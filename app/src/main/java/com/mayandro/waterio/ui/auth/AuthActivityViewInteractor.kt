package com.mayandro.waterio.ui.auth

import com.google.firebase.auth.PhoneAuthCredential
import com.mayandro.waterio.ui.base.ViewInteractor

interface AuthActivityViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)

    fun goToGoalActivity()

    fun startSMSListener()

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)
}