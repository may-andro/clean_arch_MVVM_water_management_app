package com.mayandro.waterio.ui.onboarding

import android.graphics.Color
import android.os.Bundle
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.ActivityOnboardingBinding
import com.mayandro.waterio.ui.base.BaseActivity
import com.mayandro.waterio.ui.onboarding.adapter.OnBoardingPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import android.os.Build
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.mayandro.waterio.ui.auth.AuthActivity
import com.mayandro.waterio.utils.SharedPreferenceManager
import javax.inject.Inject



class OnBoardingActivity : BaseActivity<ActivityOnboardingBinding, OnBoardingViewModel>() {

    @Inject
    lateinit var sharedPreferenceManager: SharedPreferenceManager

    override fun getViewModelClass(): Class<OnBoardingViewModel> = OnBoardingViewModel::class.java

    override fun layoutId(): Int = R.layout.activity_onboarding

    private lateinit var onBoardingPagerAdapter: OnBoardingPagerAdapter

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.TRANSPARENT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        super.onCreate(savedInstanceState)

        setUpPager()

        setUpNextButton()

        setUpGetStartedButton()

        setUpSkipButton()

        if(sharedPreferenceManager.userVisistedOnBoarding) {
            startActivity(AuthActivity.getIntent(this))
            finish()
        }
    }

    private fun setUpSkipButton() {
        binding.onboardingSkip.setOnClickListener {
            position = OnBoardingPagerAdapter.onBoardingList.size - 1
            binding.onboardingPager.currentItem = position
            showGetStarted()
        }
    }

    private fun setUpGetStartedButton() {
        binding.onboardingGetStarted.setOnClickListener {
            sharedPreferenceManager.userVisistedOnBoarding = true
            startActivity(AuthActivity.getIntent(this))
            finish()
        }
    }

    private fun setUpNextButton() {
        binding.onboardingNextButton.setOnClickListener {
            position = binding.onboardingPager.currentItem
            if (position < OnBoardingPagerAdapter.onBoardingList.size) {
                position += 1
                binding.onboardingPager.currentItem = position
            }

            if (position == OnBoardingPagerAdapter.onBoardingList.size - 1) {
                showGetStarted()
            }
        }
    }

    private fun setUpPager() {
        onBoardingPagerAdapter = OnBoardingPagerAdapter()
        binding.onboardingPager.adapter = onBoardingPagerAdapter
        TabLayoutMediator(binding.onboardingTabs, binding.onboardingPager) { _, _ ->
        }.attach()
        binding.onboardingPager.isUserInputEnabled = false
        binding.onboardingPager.setPageTransformer(ZoomOutPageTransformer())
    }

    private fun showGetStarted() {
        binding.onboardingGetStarted.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .translationYBy(100f)
                .setDuration(700)
                .setListener(null)
        }
        binding.onboardingTabs.visibility = View.INVISIBLE
        binding.onboardingNextButton.visibility = View.INVISIBLE
        binding.onboardingSkip.visibility = View.INVISIBLE
    }
}


class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    companion object{
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}