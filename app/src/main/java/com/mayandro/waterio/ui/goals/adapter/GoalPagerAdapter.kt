package com.mayandro.waterio.ui.goals.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.LayoutGoalBinding
import javax.inject.Inject

class GoalPagerAdapter @Inject constructor(): RecyclerView.Adapter<GoalPagerAdapter.ViewHolder>() {

    companion object{
        val waterConsumptionList: List<Float> = listOf(2f,2.5f,3f,3.5f,4f,4.5f,5f)
    }

    var callback: Callback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_goal, parent,
            false
        )

        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = waterConsumptionList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(waterConsumptionList[position])
    }

    inner class ViewHolder(
        private val binding: LayoutGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                callback?.onItemClicked(it)
            }
        }

        fun render(value: Float) {
            binding.textViewValue.text = "$value"
        }
    }

    interface Callback {
        fun onItemClicked(view: View)
    }
}