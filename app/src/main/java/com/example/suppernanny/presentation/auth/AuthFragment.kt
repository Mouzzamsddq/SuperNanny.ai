package com.example.suppernanny.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.suppernanny.MainActivity
import com.example.suppernanny.R
import com.example.suppernanny.core.base.BaseFragment
import com.example.suppernanny.core.base.State
import com.example.suppernanny.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>(
  FragmentAuthBinding::inflate
) {

  private val viewModel: AuthViewModel by viewModels()
  private var errorDialog: AlertDialog? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    handleClicks()
    viewLifecycleOwner.lifecycleScope.launch {
      repeatOnLifecycle(Lifecycle.State.STARTED) {
        launch {
          viewModel.otpState.collect { state ->
            when (state) {
              is State.Loading -> showLoader()
              is State.Success -> {
                hideLoader()
                // Only show OTP input if this is a verificationId, not auto-verification
                if (state.data != "Auto verification completed") {
                  showOtpInput()
                } else {
                  // Optionally, you can show a message for auto-verification
                  showDialog("Auto verification completed", "Info", navigateHome = true)
                }
              }
              is State.Error -> {
                hideLoader()
                showDialog(state.message, "Error", navigateHome = false)
              }
              else -> Unit
            }
          }
        }

        launch {
          viewModel.verificationState.collect { state ->
            when (state) {
              is State.Loading -> showLoader()
              is State.Success -> {
                hideLoader()
                if (state.data == true) {
                  hideButton()
                  hideOTPInput()
                  showDialog("OTP Verified, User logged in!", "Success", navigateHome = true)
                } else {
                  showDialog("OTP verification failed.", "Error", navigateHome = false)
                }
              }
              is State.Error -> {
                hideLoader()
                showDialog(state.message, "Error", navigateHome = false)
              }
              else -> Unit
            }
          }
        }
      }
    }
  }

  private fun showOtpInput() {
    binding.otpInputLayout.isVisible = true
    binding.btnSendOtp.text = "Verify OTP"
  }

  private fun hideOTPInput() {
    binding.otpInputLayout.isVisible = false
  }

  private fun hideButton() {
    binding.btnSendOtp.isVisible = false
  }


  private fun showDialog(message: String, title: String, navigateHome: Boolean = true) {
    errorDialog?.dismiss()
    errorDialog = AlertDialog.Builder(requireContext())
      .setTitle(title)
      .setMessage(message)
      .setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
        if (navigateHome) {
          navigateToHome()
        }
      }
      .setCancelable(true)
      .create()

    errorDialog?.show()
  }

  private fun navigateToHome() {
    findNavController().navigate(
      R.id.action_nav_auth_fragment_to_nav_home_fragment,
      null,
      NavOptions.Builder()
        .setPopUpTo(R.id.nav_auth_fragment, true)
        .build()
    )
  }

  private fun showLoader() {
    binding.progressBar.isVisible = true
  }

  private fun hideLoader() {
    binding.progressBar.isVisible = false
  }

  private fun handleClicks() {
    binding.apply {
      btnSendOtp.setOnClickListener {
        val enteredPhoneNumber = etPhoneNumber.text.toString()
        if (enteredPhoneNumber.isBlank()) return@setOnClickListener
        if (activity !is MainActivity) return@setOnClickListener
        if (btnSendOtp.text == "Verify OTP") {
          viewModel.verifyOtp(etOtp.text.toString().trim())
        } else
          viewModel.sendOtp(enteredPhoneNumber, activity as MainActivity)
      }
    }
  }


}