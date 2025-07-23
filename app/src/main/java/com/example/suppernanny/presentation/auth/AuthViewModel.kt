package com.example.suppernanny.presentation.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suppernanny.core.base.State
import com.example.suppernanny.domain.auth.usecases.SendOtpUseCase
import com.example.suppernanny.domain.auth.usecases.VerifyOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
  private val sendOtpUseCase: SendOtpUseCase,
  private val verifyOtpUseCase: VerifyOtpUseCase
) : ViewModel() {
  private val _otpState = MutableStateFlow<State<String>>(State.Default)
  val otpState: StateFlow<State<String>> = _otpState

  private val _verificationState =
    MutableStateFlow<State<Boolean>>(State.Default)
  val verificationState: StateFlow<State<Boolean>> = _verificationState

  private var storedVerificationId: String? = null

  fun sendOtp(phoneNumber: String, activity: Activity) {
    viewModelScope.launch {
      sendOtpUseCase(phoneNumber, activity).collect { result ->
        when (result) {
          is State.Success -> {
            storedVerificationId = result.data
            _otpState.value = result
          }

          is State.Error -> {
            _otpState.value = result
          }

          is State.Loading -> {
            _otpState.value = result
          }

          else -> Unit
        }
      }
    }
  }

  fun verifyOtp(otp: String) {
    val verificationId = storedVerificationId ?: return
    viewModelScope.launch {
      verifyOtpUseCase(verificationId, otp).collect { result ->
        _verificationState.value = result
      }
    }
  }

  fun resetOtpState() {
    _otpState.value = State.Default
  }

  fun resetVerificationState() {
    _verificationState.value = State.Default
  }
}