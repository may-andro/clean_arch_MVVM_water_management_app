package com.mayandro.waterio.ui.home.fragment.setting.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.LayoutTimePickerBinding
import com.mayandro.waterio.di.app.scope.ActivityScoped
import com.mayandro.waterio.utils.SharedPreferenceManager
import com.mayandro.waterio.utils.dateandtime.DateAndTimeUtils
import java.util.*
import javax.inject.Inject

@ActivityScoped
class TimePagerAdapter @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager) :
    RecyclerView.Adapter<TimePagerAdapter.ViewHolder>() {

    var timeSelectedListener:TimeSelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_time_picker, parent,
            false
        )
        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val time: String? = when (position) {
            0 -> {
                sharedPreferenceManager.settingStartTime
            }
            1 -> {
                sharedPreferenceManager.settingEndTime
            }
            else -> null
        }
        time?.let {
            holder.render(it)
        }
    }

    inner class ViewHolder(private val binding: LayoutTimePickerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.timePicker.setOnTimeChangedListener { timePicker, hourofday, minute ->
                val calendar: Calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourofday)
                calendar.set(Calendar.MINUTE, minute)
                timeSelectedListener?.onTimeSelected(DateAndTimeUtils.getSpecifiedTime(calendar), adapterPosition == 0)
            }
        }

        fun render(time: String) {
            val pair = DateAndTimeUtils.getHourAndMinFromTime(time)

            pair?.let {
                binding.timePicker.currentHour = it.first
                binding.timePicker.currentMinute = it.second
            }

        }
    }

}


interface TimeSelectedListener{
    fun onTimeSelected(time: String, isStartTime: Boolean)
}
