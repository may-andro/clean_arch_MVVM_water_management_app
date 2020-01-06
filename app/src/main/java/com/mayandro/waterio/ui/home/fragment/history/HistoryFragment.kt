package com.mayandro.waterio.ui.home.fragment.history

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mayandro.waterio.R
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.databinding.FragmentDashboardBinding
import com.mayandro.waterio.databinding.FragmentHistoryBinding
import com.mayandro.waterio.ui.base.BaseFragment
import com.mayandro.waterio.ui.home.HomeActivityViewModel
import com.mayandro.waterio.ui.home.fragment.history.adapter.HistoryPagerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryFragment: BaseFragment<FragmentHistoryBinding, HomeActivityViewModel>(),
    HistoryFragmentViewInteractor {

    companion object {
        fun newInstance(): HistoryFragment {
            val args = Bundle()
            val fragment = HistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModelClass(): Class<HomeActivityViewModel> = HomeActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_history

    private lateinit var historyPagerAdapter: HistoryPagerAdapter

    lateinit var historyFragmentViewModel: HistoryFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyFragmentViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(HistoryFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        historyFragmentViewModel.viewInteractor = this

        viewModel.currentUser.observe(requireActivity(), Observer<User>{
            binding.textViewCurrentStreakValue.text = "${it.currentStreak} days"
            binding.textViewLongestStreakValue.text = "${it.longestStreak} days"
        })

        historyFragmentViewModel.getUserHistoryForDuration()
    }

    override fun showSnackBarMessage(message: String) {
        showSnackBar(message)
    }

    override fun setUpPager(list: List<UserHistory>) {
        historyPagerAdapter = HistoryPagerAdapter(list)
        binding.viewPagerHistory.adapter = historyPagerAdapter
        TabLayoutMediator(binding.historyTabs, binding.viewPagerHistory) { tab: TabLayout.Tab, position: Int ->
            when(position) {
                0 -> tab.text = "Weekly"
                1 -> tab.text = "Monthly"
                else -> tab.text = "Unknown"
            }
        }.attach()
    }
}