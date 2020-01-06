package com.mayandro.waterio.ui.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mayandro.waterio.R
import com.mayandro.waterio.data.model.OnBoardingModel
import com.mayandro.waterio.databinding.LayoutOnboardingBinding

class OnBoardingPagerAdapter :
    RecyclerView.Adapter<OnBoardingPagerAdapter.ViewHolder>() {

    companion object{
        val onBoardingList: List<OnBoardingModel> = listOf(
            OnBoardingModel(title = "Goals", subtitle = "Set your daily goals to keep your body hydrated", image = R.drawable.water_glass),
            OnBoardingModel(title = "Reminders", subtitle = "Timely reminders to make sure you full fill the daily needs", image = R.drawable.clock),
            OnBoardingModel(title = "History", subtitle = "Check your progress any time to see how are you doing", image = R.drawable.history)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_onboarding, parent,
            false
        )

        return ViewHolder(DataBindingUtil.bind(view)!!)
    }

    override fun getItemCount(): Int = onBoardingList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(onBoardingList[position])
    }

    inner class ViewHolder(
        private val binding: LayoutOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }

        fun render(onBoardingModel: OnBoardingModel) {
            binding.textOnboardingTitle.text = onBoardingModel.title
            binding.textOnboardingDescription.text = onBoardingModel.subtitle
            Glide.with(binding.imageOnboarding.context)
                .asGif()
                .load(onBoardingModel.image)
                .centerCrop()
                .into(binding.imageOnboarding)
        }
    }

}
