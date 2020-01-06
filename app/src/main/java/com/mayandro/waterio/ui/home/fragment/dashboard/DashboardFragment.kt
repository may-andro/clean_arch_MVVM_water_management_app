package com.mayandro.waterio.ui.home.fragment.dashboard

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mayandro.waterio.R
import com.mayandro.waterio.data.model.User
import com.mayandro.waterio.data.model.UserHistory
import com.mayandro.waterio.databinding.DialogAddWaterBinding
import com.mayandro.waterio.databinding.FragmentDashboardBinding
import com.mayandro.waterio.ui.base.BaseFragment
import com.mayandro.waterio.ui.home.HomeActivityViewModel
import com.mayandro.waterio.ui.home.fragment.dashboard.adapter.HistoryLogAdapter
import com.mayandro.waterio.ui.home.fragment.dashboard.adapter.setDivider


class DashboardFragment : BaseFragment<FragmentDashboardBinding, HomeActivityViewModel>(),
    DashboardFragmentViewInteractor {

    companion object {
        fun newInstance(): DashboardFragment {
            val args = Bundle()
            val fragment = DashboardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModelClass(): Class<HomeActivityViewModel> =
        HomeActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_dashboard

    lateinit var dashboardFragmentViewModel: DashboardFragmentViewModel

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dashboardFragmentViewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(DashboardFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dashboardFragmentViewModel.viewInteractor = this
        binding.cardViewMessage.visibility = View.GONE

        setUpBottomSheet()

        setCircularProgressView()
        setUpCardCupWaterConsumption()
        setUpCardMugWaterConsumption()
        setUpCardGlassWaterConsumption()
        setUpCardCustomWaterConsumption()
        setUpLogButton()

        activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(DashboardFragmentViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        dashboardFragmentViewModel.currentConsumptionPercentage.observe(
            requireActivity(),
            Observer<Int> {
                binding.circularProgressBar.setProgress(it)
                if (it in 1..100)
                    setWaveViewHeight(it * 0.01f) else if (it > 100) setWaveViewHeight(1f)
            })

        viewModel.currentUserHistory.observe(requireActivity(), Observer<UserHistory> {
            binding.textViewWaterQuantity.text =
                "${(it.historyQuantity * 1000).toInt()}ml of ${(it.historyGoal * 1000).toInt()}ml"
            dashboardFragmentViewModel.setConsumptionPercentage(it)
            setUpLogRecyclerView(it)
        })
    }

    private fun setUpCardCustomWaterConsumption() {
        binding.cardCustomWater.setOnClickListener {
            setAlertDailog()
        }
    }

    private fun setUpLogButton() {
        binding.buttonLog.setOnClickListener {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
    }

    private fun setUpBottomSheet() {
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.layoutLogBottomSheet.layoutLogBottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        binding.layoutLogBottomSheet.imageButtonClose.setOnClickListener {
            bottomSheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    private fun setUpLogRecyclerView(userHistory: UserHistory) {
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.layoutLogBottomSheet.recyclerViewLog.apply {
            adapter = HistoryLogAdapter(logList = userHistory.historyLog)
            layoutManager = linearLayoutManager
            clipToPadding = false
            clipChildren = false
            setDivider(R.drawable.recycler_view_divider)
        }
    }

    private fun setUpCardCupWaterConsumption() {
        binding.cardCupWater.setOnClickListener {
            viewModel.currentUser.value?.let {
                dashboardFragmentViewModel.addValueToCurrentConsumption(150, user = it)
                showMessageCard(150)
            } ?: run {
                showSnackBar("Empty User")
            }
        }
    }

    private fun setUpCardMugWaterConsumption() {
        binding.cardMugWater.setOnClickListener {
            viewModel.currentUser.value?.let {
                dashboardFragmentViewModel.addValueToCurrentConsumption(250, user = it)
                showMessageCard(250)
            } ?: run {
                showSnackBar("Empty User")
            }
        }
    }

    private fun setUpCardGlassWaterConsumption() {
        binding.cardGlassWater.setOnClickListener {
            viewModel.currentUser.value?.let {
                dashboardFragmentViewModel.addValueToCurrentConsumption(300, user = it)
                showMessageCard(300)
            } ?: run {
                showSnackBar("Empty User")
            }
        }
    }

    private fun showMessageCard(amount: Int) {
        setUpCardMessageText(amount)
        setUpCardMessageIcon(amount)
        setUpCardMessageUndo(amount)
        setUpCardMessageAnimation(amount)
    }

    private fun setUpCardMessageIcon(amount: Int) {
        when (amount) {
            150 -> binding.textViewMessageImage.setImageResource(R.drawable.ic_tea)
            250 -> binding.textViewMessageImage.setImageResource(R.drawable.ic_mug)
            300 -> binding.textViewMessageImage.setImageResource(R.drawable.ic_glass)
            else -> binding.textViewMessageImage.setImageResource(R.drawable.ic_glass)
        }
    }

    private fun setUpCardMessageText(amount: Int) {
        binding.textViewMessage.text = "${amount}ml of water added"
    }

    private fun setUpCardMessageUndo(amount: Int) {
        binding.textViewUndo.setOnClickListener {
            viewModel.currentUser.value?.let {
                dashboardFragmentViewModel.addValueToCurrentConsumption(-amount, it)
                binding.cardViewMessage.visibility = View.GONE
                binding.cardViewMessage.clearAnimation()
            } ?: run {
                showSnackBar("Empty User")
            }
        }
    }

    private fun setUpCardMessageAnimation(amount: Int) {
        binding.cardViewMessage.apply {
            alpha = 0f
            clearAnimation()
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}
                    override fun onAnimationEnd(p0: Animator?) {
                        println("DashboardFragment.onAnimationEnd")
                        dashboardFragmentViewModel.delayForASecond(1)
                    }

                    override fun onAnimationCancel(p0: Animator?) {}

                    override fun onAnimationStart(p0: Animator?) {}
                })
        }
    }

    private fun setCircularProgressView() {
        binding.circularProgressBar.setProgress(0)
        binding.circularProgressBar.setProgressColor(Color.BLUE)
    }

    private fun setWaveViewHeight(scalePercentage: Float) {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels * scalePercentage
        slideView(binding.waveView, binding.waveView.layoutParams.height, height.toInt())
    }

    private fun slideView(view: View, currentHeight: Int, newHeight: Int) {
        val slideAnimator = ValueAnimator
            .ofInt(currentHeight, newHeight)
            .setDuration(500)

        slideAnimator.addUpdateListener { animation1: ValueAnimator ->
            val value = animation1.animatedValue as Int
            view.layoutParams.height = value
            view.requestLayout()
        }

        val animationSet = AnimatorSet()
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.play(slideAnimator)
        animationSet.start()
    }

    private fun setAlertDailog() {
        val dialogBuilder: AlertDialog = AlertDialog.Builder(requireContext()).create()
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_water, null)
        val dialogBinding: DialogAddWaterBinding? = DataBindingUtil.bind(dialogView)
        dialogBinding?.buttonCancel?.setOnClickListener { dialogBuilder.dismiss() }
        dialogBinding?.buttonSubmit?.setOnClickListener {
            val isValid = dashboardFragmentViewModel.checkIfQuantityIsValid(dialogBinding.textInputEditText.text.toString())
            if(isValid) {
                viewModel.currentUser.value?.let {
                    dashboardFragmentViewModel.addValueToCurrentConsumption(dialogBinding.textInputEditText.text.toString().toInt(), it)
                    binding.cardViewMessage.visibility = View.GONE
                    binding.cardViewMessage.clearAnimation()
                } ?: run {
                    showSnackBar("Empty User")
                }
                dialogBuilder.dismiss()
            } else {
                dialogBinding.textInputLayout.error = "Invalid Amount: Please enter valid value"
            }
        }
        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    override fun showSnackBarMessage(message: String) {
        showSnackBar(message)
    }

    override fun manageCardMessageVisibility(visible: Boolean) {
        if (visible) binding.cardViewMessage.visibility =
            View.VISIBLE else binding.cardViewMessage.visibility = View.GONE
    }

    override fun updateUserHistory(userHistory: UserHistory) {
        viewModel.currentUserHistory.value = userHistory
    }

    override fun updateUser(user: User) {
        viewModel.currentUser.value = user
    }
}