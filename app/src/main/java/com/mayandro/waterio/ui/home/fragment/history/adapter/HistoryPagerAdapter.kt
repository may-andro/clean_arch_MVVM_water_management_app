package com.mayandro.waterio.ui.home.fragment.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.anychart.AnyChart.column
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.mayandro.waterio.R
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.databinding.LayoutChartBinding


class HistoryPagerAdapter(private val userHistoryList: List<UserHistory>)  : RecyclerView.Adapter<HistoryPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_chart, parent,
            false
        )
        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data: MutableList<DataEntry> = ArrayList()

        when(position) {
            0 -> {
                userHistoryList.take(7).forEach{
                    data.add(ValueDataEntry(it.historyDate, it.historyQuantity))
                }
            }
            1 -> {
                userHistoryList.forEach{
                    data.add(ValueDataEntry(it.historyDate, it.historyQuantity))
                }
            }
            else -> data.add(ValueDataEntry("UNKNOWN", 0))
        }


        holder.render(data)
    }

    inner class ViewHolder(
        private val binding: LayoutChartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }

        fun render(data: MutableList<DataEntry>) {
            val cartesian: Cartesian = column()
            val column: Column = cartesian.column(data)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("{%Value}{groupsSeparator: } liter")

            cartesian.animation(false)
            cartesian.yScale().minimum(0.0)
            cartesian.yAxis(0).labels()
            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)
            cartesian.xAxis(0).title("Time")
            cartesian.yAxis(0).title("Water Consumption (in liters)")

            binding.anyChartView.setChart(cartesian)
        }
    }

}
