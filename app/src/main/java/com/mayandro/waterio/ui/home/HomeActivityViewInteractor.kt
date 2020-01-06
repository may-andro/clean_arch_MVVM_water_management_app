package com.mayandro.waterio.ui.home

import com.google.firebase.auth.PhoneAuthCredential
import com.mayandro.waterio.ui.base.ViewInteractor

interface HomeActivityViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)

}