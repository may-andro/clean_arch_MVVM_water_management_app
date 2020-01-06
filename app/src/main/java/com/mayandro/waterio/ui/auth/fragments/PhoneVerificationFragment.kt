package com.mayandro.waterio.ui.auth.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import com.mayandro.waterio.R
import com.mayandro.waterio.databinding.FragmentPhoneVerificationBinding
import com.mayandro.waterio.ui.auth.AuthActivityViewModel
import com.mayandro.waterio.ui.base.BaseFragment


class PhoneVerificationFragment : BaseFragment<FragmentPhoneVerificationBinding, AuthActivityViewModel>() {

    companion object {
        fun newInstance(): PhoneVerificationFragment {
            val args = Bundle()
            val fragment = PhoneVerificationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getViewModelClass(): Class<AuthActivityViewModel> = AuthActivityViewModel::class.java

    override fun layoutId(): Int = R.layout.fragment_phone_verification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedPhoneNumber.observe(
            requireActivity(),
            Observer<String?> { value ->
                binding.textInputEditTextPhone.setText(value ?: "")
            })

        binding.buttonVerifyPhone.setOnClickListener {
            activity?.hideKeyboard()
            if (viewModel.checkIfPhoneIsValid(binding.textInputEditTextPhone.text.toString())) {
                viewModel.sendOtpToPhone(binding.textInputEditTextPhone.text.toString())
            } else {
                binding.textInputLayoutPhone.error = "Invalid Phone: Please enter phone number with country code"
            }
        }
    }
}