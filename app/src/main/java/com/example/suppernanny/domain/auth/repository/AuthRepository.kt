package com.example.suppernanny.domain.auth.repository

import android.app.Activity
import com.example.suppernanny.core.base.State
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
  suspend fun sendOtp(mobileNumber: String, activity: Activity): Flow<State<String>>
  suspend fun verifyOtp(verificationId: String, otp: String): Flow<State<Boolean>>
}