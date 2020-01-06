package com.mayandro.waterio.ui.home.fragment.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.FragmentSettingBinding
import com.mayandro.waterio.ui.base.BaseFragment
import com.mayandro.waterio.ui.home.HomeActivityViewModel
import com.mayandro.waterio.ui.home.fragment.setting.dialog.DurationFrequencyBottomDialogFragment
import com.mayandro.waterio.ui.home.fragment.setting.dialog.TimePagerAdapter
import com.mayandro.waterio.ui.home.fragment.setting.dialog.TimeSelectedListener
import com.mayandro.waterio.utils.SharedPreferenceManager
import javax.inject.Inject


class SettingFragment : BaseFragment<FragmentSettingBinding, HomeActivityViewModel>(),
    SettingFragmentViewInteractor, TimeSelectedListener {

    companion object {
        fun newInstance(): SettingFragment {
            val args = Bundle()
            val fragment = SettingFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModelClass(): Class<HomeActivityViewModel> =
        HomeActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_setting

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    @Inject
    lateinit var timePagerAdapter: TimePagerAdapter

    lateinit var settingFragmentViewModel: SettingFragmentViewModel

    var durationFrequencyBottomDialogFragment: DurationFrequencyBottomDialogFragment? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingFragmentViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(SettingFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        settingFragmentViewModel.viewInteractor = this
        settingFragmentViewModel.initializeViewData()

        setUpStreaksSwitch()

        binding.textViewFrequencyValue.setOnClickListener {
            showBottomSheet()
        }

        settingFragmentViewModel.frequencyDuration.observe(this, Observer {
            durationFrequencyBottomDialogFragment?.dismiss()
            setDurationFrequencyTextView(it)
        })

        settingFragmentViewModel.streaksEnable.observe(this, Observer {
            binding.switchStreakValue.isChecked = it
        })

        setUpPager()
    }

    private fun setUpPager() {
        binding.viewPagerTime.adapter = timePagerAdapter
        timePagerAdapter.timeSelectedListener = this
        TabLayoutMediator(binding.tabsTime, binding.viewPagerTime) { tab: TabLayout.Tab, position: Int ->
            when(position) {
                0 -> tab.text = "Start At"
                1 -> tab.text = "End At"
                else -> tab.text = "Unknown"
            }
        }.attach()
    }

    private fun setUpStreaksSwitch() {
        binding.switchStreakValue.setOnCheckedChangeListener { compoundButton, b ->
            settingFragmentViewModel.updateUserStreakValue(b)
        }
    }

    private fun showBottomSheet() {
        durationFrequencyBottomDialogFragment = DurationFrequencyBottomDialogFragment.newInstance()
        durationFrequencyBottomDialogFragment?.show(
            requireActivity().supportFragmentManager,
            DurationFrequencyBottomDialogFragment.TAG
        )
    }

    override fun showSnackBarMessage(message: String) {
        showSnackBar(message)
    }

    private fun setDurationFrequencyTextView(item: Int) {
        binding.textViewFrequencyValue.text = binding.root.resources.getString(
            R.string.frequency_duration,
            (item).toString()
        )
    }

    override fun onTimeSelected(time: String, isStartTime: Boolean) {
        settingFragmentViewModel.setNewTime(time, isStartTime)
    }
}