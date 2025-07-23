package com.example.suppernanny.domain.auth.usecases

import com.example.suppernanny.core.base.State
import com.example.suppernanny.domain.auth.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyOtpUseCase @Inject constructor(
  private val repository: AuthRepository
) {
  suspend operator fun invoke(
    verificationId: String,
    otp: String
  ): Flow<State<Boolean>> {
    return repository.verifyOtp(verificationId, otp)
  }
}
