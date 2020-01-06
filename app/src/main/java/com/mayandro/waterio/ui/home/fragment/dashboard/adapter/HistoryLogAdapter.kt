package com.mayandro.waterio.ui.home.fragment.dashboard.adapter

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.mayandro.waterio.R
import com.mayandro.waterio.data.model.UserLog
import com.mayandro.waterio.databinding.LayoutHistoryLogItemBinding


class HistoryLogAdapter(private val logList: List<UserLog>) :
    RecyclerView.Adapter<HistoryLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_history_log_item, parent,
            false
        )
        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = logList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(logList[position])
    }

    inner class ViewHolder(
        private val binding: LayoutHistoryLogItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun render(userLog: UserLog) {

            when (Math.abs(userLog.logQuantity).toInt()) {
                150 -> binding.imageViewQuantity.setImageResource(R.drawable.ic_tea)
                250 -> binding.imageViewQuantity.setImageResource(R.drawable.ic_mug)
                300 -> binding.imageViewQuantity.setImageResource(R.drawable.ic_glass)
                else -> binding.imageViewQuantity.setImageResource(R.drawable.ic_glass)
            }
            binding.textViewTime.text = userLog.logTime

            if (userLog.logQuantity < 0) {
                binding.textViewQuantity.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.red_300
                    )
                )

                binding.textViewTime.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.red_300
                    )
                )

                ImageViewCompat.setImageTintList(
                    binding.imageViewQuantity,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red_300
                        )
                    )
                )
                binding.textViewQuantity.text = binding.root.resources.getString(R.string.water_removed, (Math.abs(userLog.logQuantity) * 1000).toString())
            } else {
                binding.textViewQuantity.text = binding.root.resources.getString(R.string.water_added, (userLog.logQuantity * 1000).toString())
                ImageViewCompat.setImageTintList(
                    binding.imageViewQuantity,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.white
                        )
                    )
                )

            }
        }
    }
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}