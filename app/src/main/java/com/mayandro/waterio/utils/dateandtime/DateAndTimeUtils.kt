package com.mayandro.waterio.utils.dateandtime

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateAndTimeUtils {
    companion object {
        fun getCurrentTime(): String {
            val time = Calendar.getInstance().time
            val timeFormat = SimpleDateFormat("hh:mm a")
            return timeFormat.format(time)
        }

        fun getCurrentDate(): String {
            val date = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("MMM d, yyyy")
            return dateFormat.format(date)
        }

        fun getDate(dateString: String): Date? {
            val format = SimpleDateFormat("MMM d, yyyy")
            try {
                return format.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun getTime(dateString: String): Date? {
            val format = SimpleDateFormat("hh:mm a")
            try {
                return format.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return null
        }

        fun getSpecifiedTime(calendar: Calendar): String {
            val time = calendar.time
            val timeFormat = SimpleDateFormat("hh:mm a")
            return timeFormat.format(time)
        }

        fun getHourAndMinFromTime(dateString: String): Pair<Int, Int>? {
            getTime(dateString)?.let {
                val calendar: Calendar = Calendar.getInstance()
                calendar.time = it
                val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
                val minute: Int = calendar.get(Calendar.MINUTE)
                return Pair(hour, minute)
            }
            return null
        }

        fun getHourAndMinFromCurrentTime(): Pair<Int, Int>? {
            val calendar: Calendar = Calendar.getInstance()
            val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
            val minute: Int = calendar.get(Calendar.MINUTE)
            return Pair(hour, minute)
        }

        fun getFrequencyHourFromNow(durationFrequency: Int): Int {
            val calendar: Calendar = Calendar.getInstance()
            return calendar.get(Calendar.HOUR_OF_DAY) + durationFrequency
        }
    }
}