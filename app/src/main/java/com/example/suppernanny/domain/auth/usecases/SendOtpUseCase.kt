package com.example.suppernanny.domain.auth.usecases

import android.app.Activity
import com.example.suppernanny.core.base.State
import com.example.suppernanny.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
  private val repository: AuthRepository
) {
  suspend operator fun invoke(
    phoneNumber: String,
    activity: Activity
  ): Flow<State<String>> {
    return repository.sendOtp(phoneNumber, activity)
  }
}