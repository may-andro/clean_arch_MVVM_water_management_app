package com.mayandro.waterio.ui.home.fragment.setting

import androidx.lifecycle.MutableLiveData
import com.mayandro.waterio.ui.base.BaseViewModel
import com.mayandro.waterio.utils.SharedPreferenceManager
import javax.inject.Inject

class SettingFragmentViewModel @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager): BaseViewModel<SettingFragmentViewInteractor>() {


    var frequencyDuration = MutableLiveData<Int>()
    var streaksEnable = MutableLiveData<Boolean>()
    var endTime = MutableLiveData<String>()
    var startTime = MutableLiveData<String>()

    fun initializeViewData() {
        frequencyDuration.value = sharedPreferenceManager.settingDurationFrequency
        streaksEnable.value = sharedPreferenceManager.settingStreaksNotificationEnable
        endTime.value = sharedPreferenceManager.settingEndTime
        startTime.value = sharedPreferenceManager.settingStartTime
    }

    fun updateDurationFrequency(item: Int) {
        sharedPreferenceManager.settingDurationFrequency = item
        frequencyDuration.value = item
    }

    fun updateUserStreakValue(flag: Boolean) {
        sharedPreferenceManager.settingStreaksNotificationEnable = flag
        streaksEnable.value = flag
    }

    fun setNewTime(time: String, isStartTime: Boolean) {
        if(isStartTime) {
            sharedPreferenceManager.settingStartTime = time
            startTime.value = sharedPreferenceManager.settingStartTime
            return
        }
        sharedPreferenceManager.settingEndTime = time
        endTime.value = sharedPreferenceManager.settingEndTime
    }
}