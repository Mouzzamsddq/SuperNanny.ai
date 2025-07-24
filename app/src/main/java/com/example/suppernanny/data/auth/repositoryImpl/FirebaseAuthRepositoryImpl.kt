package com.example.suppernanny.data.auth.repositoryImpl

import android.app.Activity
import com.example.suppernanny.core.base.State
import com.example.suppernanny.domain.auth.repository.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseAuthRepositoryImpl @Inject constructor(
  private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

  override suspend fun sendOtp(
    mobileNumber: String,
    activity: Activity
  ): Flow<State<String>> = callbackFlow {
    trySend(State.Loading)

    val callbacks =
      object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
          trySend(State.Success("Auto verification completed"))
          // Add delay to allow loader to show
          GlobalScope.launch {
            delay(400)
            close()
          }
        }

        override fun onVerificationFailed(e: FirebaseException) {
          trySend(State.Error("Verification failed ${e.message}", e))
          GlobalScope.launch {
            delay(400)
            close()
          }
        }

        override fun onCodeSent(
          verificationId: String,
          token: PhoneAuthProvider.ForceResendingToken
        ) {
          trySend(State.Success(verificationId))
          GlobalScope.launch {
            delay(400)
            close()
          }
        }
      }

    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
      .setPhoneNumber("+91"+mobileNumber)
      .setTimeout(60L, TimeUnit.SECONDS)
      .setActivity(activity)
      .setCallbacks(callbacks)
      .build()

    PhoneAuthProvider.verifyPhoneNumber(options)

    awaitClose {

    }
  }

  override suspend fun verifyOtp(
    verificationId: String,
    otp: String
  ): Flow<State<Boolean>> = callbackFlow {
    trySend(State.Loading)
    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
    firebaseAuth.signInWithCredential(credential)
      .addOnCompleteListener { task ->
        if (task.isSuccessful) {
          trySend(State.Success(true))
        } else {
          trySend(State.Error("OTP verification failed ${task.exception?.message}", task.exception))
        }
        GlobalScope.launch {
          delay(400)
          close()
        }
      }
    awaitClose {
    }
  }
}
