package com.mayandro.waterio.ui.home.fragment.setting.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.BottomSheetDurationFrequencyBinding
import com.mayandro.waterio.databinding.LayoutDurationFrequencyItemBinding
import com.mayandro.waterio.ui.home.fragment.setting.SettingFragmentViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DurationFrequencyBottomDialogFragment : BottomSheetDialogFragment(), OnClickListener {

    companion object {
        const val TAG = "ActionBottomDialog"
        val frequencyList = listOf(2, 4, 6)
        fun newInstance(): DurationFrequencyBottomDialogFragment {
            return DurationFrequencyBottomDialogFragment()
        }
    }

    lateinit var binding: BottomSheetDurationFrequencyBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            dialog!!.layoutInflater,
            R.layout.bottom_sheet_duration_frequency,
            null,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(SettingFragmentViewModel::class.java)

        setUpLogRecyclerView()
    }

    private fun setUpLogRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewFrequency.apply {
            adapter = HistoryLogAdapter(
                frequencyList = frequencyList,
                listener = this@DurationFrequencyBottomDialogFragment
            )
            layoutManager = linearLayoutManager
            clipToPadding = false
            clipChildren = false
        }
    }

    override fun onClick(item: Int) {
        //viewModel.updateDurationFrequency(item)
    }
}

class HistoryLogAdapter(
    private val frequencyList: List<Int>,
    private val listener: OnClickListener
) :
    RecyclerView.Adapter<HistoryLogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_duration_frequency_item, parent,
            false
        )
        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = frequencyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(frequencyList[position])
    }

    inner class ViewHolder(
        private val binding: LayoutDurationFrequencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.onClick(frequencyList[adapterPosition])
            }
        }

        fun render(value: Int) {
            binding.textViewFrequency.text = "$value hours"
        }
    }
}

interface OnClickListener {
    fun onClick(item: Int)
}
