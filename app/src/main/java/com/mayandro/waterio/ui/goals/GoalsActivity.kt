package com.mayandro.waterio.ui.goals

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.ActivityGoalBinding
import com.mayandro.waterio.ui.base.BaseActivity
import com.mayandro.waterio.ui.goals.adapter.GoalPagerAdapter
import com.mayandro.waterio.ui.home.HomeActivity
import com.mayandro.waterio.utils.ScreenUtils
import com.mayandro.waterio.utils.SliderLayoutManager
import javax.inject.Inject


class GoalsActivity: BaseActivity<ActivityGoalBinding, GoalActivityViewModel>(), GoalActivityViewInteractor {


    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, GoalsActivity::class.java)
        }
    }

    override fun getViewModelClass(): Class<GoalActivityViewModel> = GoalActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.activity_goal

    @Inject
    lateinit var goalPagerAdapter: GoalPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.viewInteractor = this

        setUpToolbar()
        setUpPager()
        setupFab()

        viewModel.selectedGoal.value = 2.0f
        viewModel.checkIfUserExists()
        viewModel.selectedGoal.observe(
            this, Observer<Float?> { value ->
                binding.textViewResult.text = "$value"
            }
        )
    }

    private fun setupFab() {
        binding.floatingActionButton.setOnClickListener{
           viewModel.createUser()
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbarGoal)
        supportActionBar?.title = resources.getString(R.string.goal_header)
    }

    private fun setUpPager() {
        val padding: Int = ScreenUtils.getScreenWidth(this)/2 - ScreenUtils.dpToPx(this, 45)

        val linearLayoutManager = SliderLayoutManager(this).apply {
            callback = object : SliderLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    viewModel.selectedGoal.value = GoalPagerAdapter.waterConsumptionList[layoutPosition]
                }
            }
        }

        goalPagerAdapter.callback = object : GoalPagerAdapter.Callback{
            override fun onItemClicked(view: View) {
                binding.sliderGoal.smoothScrollToPosition(binding.sliderGoal.getChildLayoutPosition(view))
            }
        }

        binding.sliderGoal.apply {
            adapter = goalPagerAdapter
            layoutManager = linearLayoutManager
            clipToPadding = false
            clipChildren = false
            setPadding(padding, 0, padding, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.goal_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.goal_info -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun showSnackBarMessage(message: String) {
        super.showSnackBar(message)
    }

    override fun goToHomeActivity() {
        startActivity(HomeActivity.getIntent(this))
        finish()
    }
}