package com.mayandro.waterio.ui.home.fragment.setting

import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.ui.base.ViewInteractor

interface SettingFragmentViewInteractor: ViewInteractor {

    fun showSnackBarMessage(message: String)
}