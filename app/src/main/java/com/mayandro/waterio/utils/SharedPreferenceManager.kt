package com.mayandro.waterio.utils

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val PREFS_KEY_USER_VISITED_ONBOARDING = "user_visited_onboarding"
        private const val PREFS_KEY_SETTING_DURATION_FREQUENCY = "setting_duration_frequency"
        private const val PREFS_KEY_SETTING_STREAK_ENABLE = "setting_streak_break"

        private const val PREFS_KEY_SETTING_START_TIME = "setting_start_time"
        private const val PREFS_KEY_SETTING_END_TIME = "setting_end_time"
    }

    var userVisistedOnBoarding: Boolean
        get(): Boolean {
            return sharedPreferences.getBoolean(PREFS_KEY_USER_VISITED_ONBOARDING, false)
        }
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_KEY_USER_VISITED_ONBOARDING, value).apply()
        }

    var settingDurationFrequency: Int
        get(): Int {
            return sharedPreferences.getInt(PREFS_KEY_SETTING_DURATION_FREQUENCY, 2)
        }
        set(value) {
            sharedPreferences.edit().putInt(PREFS_KEY_SETTING_DURATION_FREQUENCY, value).apply()
        }

    var settingStreaksNotificationEnable: Boolean
        get(): Boolean {
            return sharedPreferences.getBoolean(PREFS_KEY_SETTING_STREAK_ENABLE, true)
        }
        set(value) {
            sharedPreferences.edit().putBoolean(PREFS_KEY_SETTING_STREAK_ENABLE, value).apply()
        }

    var settingStartTime: String?
        get(): String? {
            return sharedPreferences.getString(PREFS_KEY_SETTING_START_TIME, "8:00 AM")
        }
        set(value) {
            sharedPreferences.edit().putString(PREFS_KEY_SETTING_START_TIME, value).apply()
        }

    var settingEndTime: String?
        get(): String? {
            return sharedPreferences.getString(PREFS_KEY_SETTING_END_TIME, "10:00 PM")
        }
        set(value) {
            sharedPreferences.edit().putString(PREFS_KEY_SETTING_END_TIME, value).apply()
        }

}